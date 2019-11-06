import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Shell {
 
    private Session session;
    private ChannelShell channel;
    private static Expect4j expect = null;
    private static final long defaultTimeOut = 1000;
    private StringBuffer buffer=new StringBuffer();
    
    public static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
    public static final String BACKSLASH_R = "\r";
    public static final String BACKSLASH_N = "\n";
    public static final String COLON_CHAR = ":";
    public static String ENTER_CHARACTER = BACKSLASH_R;
    public static final int SSH_PORT = 22;
    
    //����ƥ�䣬���ڴ�����������صĽ��
    public static String[] linuxPromptRegEx = new String[] { "~]#", "~#", "#",
            ":~#", "/$", ">" };
    
    public static String[] errorMsg=new String[]{"could not acquire the config lock "};
    
    //ssh��������ip��ַ
    private String ip;
    //ssh�������ĵ���˿�
    private int port;
    //ssh�������ĵ����û���
    private String user;
    //ssh�������ĵ�������
    private String password;
    
    public Shell(String ip,int port,String user,String password) {
        this.ip=ip;
        this.port=port;
        this.user=user;
        this.password=password;
        expect = getExpect();
    }
    
    /**
     * �ر�SSHԶ������
     */
    public void disconnect(){
        if(channel!=null){
            channel.disconnect();
        }
        if(session!=null){
            session.disconnect();
        }
        System.out.println("�ر�SSHԶ�����ӣ�");
    }
    
    /**
     * ��ȡ���������ص���Ϣ
     * @return ����˵�ִ�н��
     */
    public String getResponse(){
        return buffer.toString();
    }
    
    //���Expect4j���󣬸ö��ÿ�����SSH������������
    private Expect4j getExpect() {
        try {
          
            JSch jsch = new JSch();
            session = jsch.getSession(user, ip, port);
            session.setPassword(password);
            Hashtable<String, String> config = new Hashtable<String, String>();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            localUserInfo ui = new localUserInfo();
            session.setUserInfo(ui);
            session.connect();
            channel = (ChannelShell) session.openChannel("shell");
            Expect4j expect = new Expect4j(channel.getInputStream(), channel
                    .getOutputStream());
            channel.connect();
            
            return expect;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * ִ����������
     * @param commands Ҫִ�е����Ϊ�ַ�����
     * @return ִ���Ƿ�ɹ�
     */
    public boolean executeCommands(String[] commands) {
        //���expect����Ϊ0��˵������û�гɹ�
        if(expect==null){
            return false;
        }else{
        	System.out.println("����SSH���ӳɹ���");
        }        
        Closure closure = new Closure() {
            public void run(ExpectState expectState) throws Exception {
                buffer.append(expectState.getBuffer());// buffer is string
                                                        // buffer for appending
                                                        // output of executed
                                                        // command
                expectState.exp_continue();
                
            }
        };
        List<Match> lstPattern = new ArrayList<Match>();
        String[] regEx = linuxPromptRegEx;
        if (regEx != null && regEx.length > 0) {
            synchronized (regEx) {
                for (String regexElement : regEx) {// list of regx like, :>, />
                                                    // etc. it is possible
                                                    // command prompts of your
                                                    // remote machine
                    try {
                        RegExpMatch mat = new RegExpMatch(regexElement, closure);
                        lstPattern.add(mat);
                   
                    } catch (Exception e) {
                        return false;
                    }
                }
                lstPattern.add(new EofMatch(new Closure() { // should cause
                                                            // entire page to be
                                                            // collected
                            public void run(ExpectState state) {
                            }
                        }));
                lstPattern.add(new TimeoutMatch(defaultTimeOut, new Closure() {
                    public void run(ExpectState state) {
                    }
                }));
            }
        }
        try {
            boolean isSuccess = true;
            for (String strCmd : commands){
                isSuccess = isSuccess(lstPattern, strCmd);
            }
            //��ֹ���һ������ִ�в���
            isSuccess = !checkResult(expect.expect(lstPattern));
            
            //�Ҳ���������Ϣ��ʾ�ɹ�
            String response=buffer.toString().toLowerCase();
            for(String msg:errorMsg){
                if(response.indexOf(msg)>-1){
                    return false;
                }
            }
            
            return isSuccess;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //���ִ���Ƿ�ɹ�
    private boolean isSuccess(List<Match> objPattern, String strCommandPattern) {
        try {
            boolean isFailed = checkResult(expect.expect(objPattern));
            if (!isFailed) {
                expect.send(strCommandPattern);
                expect.send("\r");
                return true;
            }
            return false;
      
        } catch (Exception ex) {
            return false;
        }
    }

    //���ִ�з��ص�״̬
    private boolean checkResult(int intRetVal) {
        if (intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE) {
            return true;
        }
        return false;
    }
    
    //����SSHʱ�Ŀ�����Ϣ
    //���ò���ʾ�������롢����ʾ������Ϣ��
    public static class localUserInfo implements UserInfo {
        String passwd;

        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {
            return true;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            return true;
        }

        public void showMessage(String message) {
            //Super Implementation(Don't move!)
        }
    }
  
}