
import java.io.File;
import java.util.Properties;
import java.util.Vector;
public class SftpClient {


  private ChannelSftp sftp = null;
  private String rootPath = "/home/bea/update/zw";
  private SftpClient sftpClient=null;


  
  /**
   * ������
   */
  public SftpClient(){
	
  }
  public SftpClient(String hostName,String user,String password,int port) throws Exception{
    SftpClient f=new SftpClient();
    sftp =f.connect(hostName, user, password,port);
  }

  /**
   * ����ftp����
   * @param hostName  ftp������������ַ
   * @param user  ftp��¼�û���
   * @param password  ftp��¼����
   * @throws Exception
   */
  public ChannelSftp connect(String hostName,String user,String password,int port) throws Exception{
    try{
    	JSch jsch = new JSch();
		jsch.getSession(user, hostName, port);
		Session sshSession = jsch.getSession(user, hostName, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		sftp.setFilenameEncoding("utf-8");
		rootPath=sftp.getHome();
		if(rootPath==""||"".equals(rootPath)){
			rootPath="/";
		}
    }catch(Exception e){
      System.out.printf("�Է������޷�����",e);
      sftp = null;
      throw new Exception("�Է������޷�����");
    }
    return sftp;
  }

  /**
   * �ر�ftp����
   * @throws Exception
   */
  public void disconnect() throws Exception{
    try{
      if(sftp != null){
          sftp.disconnect();
          if(sftp.getSession()!=null){
        	  sftp.getSession().disconnect();
          }
      }
    }catch(Exception e){
      sftp=null;
      System.out.printf("�ر�sftp����ʧ��",e);
      throw new Exception("�ر�sftp����ʧ��");
    }
  }

  /**
   * ��sftp��������ȡָ�����ļ��������䱣���ڱ��ػ���ָ�����ļ��С�
   * @param localFileName  �����ļ��ľ���·���ļ���
   * @param remoteFilePath  Զ�������ļ���ŵľ���·��
   * @param remoteFileName  Զ�������ļ����ļ�����������·����
   * @throws Exception
   */
  public void getFile(String localFileName,String remoteFilePath,String remoteFileName) throws Exception{
    try {
    	sftp.cd(rootPath);
        sftp.cd(remoteFilePath);
        sftp.get(remoteFilePath + "/" + remoteFileName ,localFileName);
    }catch (Exception e) {
      System.out.printf("ȡԶ�������ļ�����", e);
      throw new Exception("ȡԶ�������ļ�����");
    }catch(Throwable t){
      t.printStackTrace();
      System.out.printf(t.getMessage());
      throw new Exception("ȡԶ�������ļ�����");
    }
  }

  /**
   * ��ftp��������ָ��·����ȡ�����ļ����������Ǳ����ڱ��ػ���ָ����·���¡�
   * @param localFilePath  �����ļ���ž���·��
   * @param remoteFilePath  Զ�������ļ���ž���·��
   * @throws Exception
   */
  public void getMFile(String localFilePath,String remoteFilePath) throws Exception{
    try{
    	sftp.cd(rootPath);
		  //�жϱ���Ŀ¼�Ƿ���ڣ�������������
		  File fileM=new File(localFilePath);
		  if(!fileM.exists()){
			  fileM.mkdir();
		  }
		  sftp.cd(remoteFilePath);
	       Vector vt = sftp.ls(remoteFilePath);
	        if (vt!=null){
	             for(int i=0;i<vt.size();i++){
	                 Object obj=vt.elementAt(i);
	                 if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
	                        SftpATTRS arr = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getAttrs();
	                        String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry )obj).getFilename();
	                         if (!fileName.equals("..") && !fileName.equals(".")) {    //�������� . �� .. ��������
	                             if (!arr.isDir()){             //����Ŀ¼�Ŵ���
	                                 sftp.get(remoteFilePath + "/" + fileName ,localFilePath + File.separator+ fileName );
	                              }
	                          }
	                                   arr = null;
	                   }
	                       obj = null;
	              }
	        }
    }catch(Exception e){
        System.out.printf("ȡԶ�������ļ�����",e);
        throw new Exception("ȡԶ�������ļ�����");
    }
  }

  /**
   * �����ػ���ָ�����ļ���ָ�����ļ������͵�Զ�̻����ϣ�������ֻ���ڴ���һ���ļ�
   * @param localFileName  ���ػ����ļ���
   * @param remoteFileName  Զ�������ļ���
   * @throws Exception
   */
  public void putFile(String localFileName,String remoteFileName) throws Exception{
	  try {
	    	 sftp.cd(rootPath);
	    	 File localFile = new File(localFileName);
	         if (!localFile.exists()) {
	           System.out.println("�����ļ�������");
	           return;
	         }
	         sftp.cd(rootPath);
	   	  sftp.put(localFileName, remoteFileName);
	      System.out.println("�������"+sftp.getSession().getHost()+"�����ļ���"+remoteFileName);
	    }
    catch (Exception e) {
      throw new Exception("����Զ�������ļ�����");
    }
  }

  /**
   * �����ػ���ָ����·���������ļ����͵�Զ�̻���ָ����·���£��������ɴ��Ͷ���ļ�
   * @param localFilePath  �����ļ�·��
   * @param remoteFilePath  Զ�̻����ļ����·��
   * @param directory ��Ŀ¼���ͱ�־
 * @throws  
   * @throws Exception
   */
  public void putMFile(String localFilePath,String remoteFilePath,boolean directory) throws Exception {
	  try{    	
		    sftp.cd(rootPath);
		  	File path = new File(localFilePath);
		  	File[] files = path.listFiles();
		      //�жϷ�����Ŀ¼�Ƿ���ڣ�������������Ŀ¼
			    if(!this.changeWorkingDirectory(remoteFilePath)&&files.length>0){
			    	sftp.mkdir(remoteFilePath);
			    	System.out.printf("Ŀ¼"+remoteFilePath+"�����ɹ�");
			    }
		      if(files!=null){
				       for(int i=0;i<files.length ;i++){
				    	   File file = files[i];
				    	   if(directory){
				    		      if(file.isDirectory()){
				    			    remoteFilePath=remoteFilePath+"/"+file.getName();
				    		        putMFile(file.getAbsolutePath(),remoteFilePath,true);
				    		        sftp.cd("../");
				    		        remoteFilePath=sftp.pwd();
				    		   }else{
				    			   sftp.put(localFilePath+"\\"+file.getName(),remoteFilePath +"/"+file.getName());
				    		   }
				    	   }
				    	   else {
				    		   if(!file.isDirectory()){
				    		     sftp.put(localFilePath+"\\"+file.getName(),remoteFilePath +"/"+file.getName());
							   }
				    	   }
				       }
		      }
		   System.out.printf("�����������"+sftp.getSession().getHost()+":"+remoteFilePath+"Ŀ¼�·����ļ�");
    }catch(Exception e){
        System.out.printf("����Զ�������ļ�����",e);
        throw new Exception("����Զ�������ļ�����");
    }
  }

  /**
   * ɾ��Զ��������ָ�����ļ�
   * @param remoteFileName  Զ�������ľ���·���ļ���
   * @throws Exception
   */
  public void deleteFile(String remoteFileName) throws Exception{
	  try{
	    	 sftp.cd(rootPath);
	         sftp.rm(remoteFileName);
	      System.out.printf("ɾ��������"+sftp.getSession().getHost()+"���ļ�:"+remoteFileName);
	    }catch(Exception e){
      System.out.printf("ɾ��Զ���ļ�ʧ��",e);
      throw new Exception("ɾ��Զ���ļ�ʧ��");
      
    }
  }

  /**
   * ɾ��Զ������ָ��·���µ������ļ�
   * @param remoteFilePath  Զ��������·��
   * @param pathDelete  ɾ��Ŀ¼��־��true��ʾɾ��Ŀ¼�Լ����µ��ļ���false��ʾֻɾ�����µ��ļ�
   * @param subDirectDelete ɾ����Ŀ¼��־��true��ʾɾ����Ŀ¼��false��ʾ��ɾ����Ŀ¼
   * @throws Exception
   */
  public void deleteFiles(String remoteFilePath,boolean pathDelete,boolean subDirectDelete) throws Exception{
	  try{
		  sftp.cd(rootPath);
		  sftp.cd(remoteFilePath);
	      Vector vt = sftp.ls(remoteFilePath);
	      if (vt!=null){
	        for(int i=0;i<vt.size();i++){
	            Object obj=vt.elementAt(i);
	            if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
	                   SftpATTRS arr = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getAttrs();
	                   String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry )obj).getFilename();
	                   //�������� . �� .. ��������
	                   if (!fileName.equals("..") && !fileName.equals(".")) {
	                 	       if(subDirectDelete){
	                 	    	   if(arr.isDir()){
	                     	    	   deleteFiles(remoteFilePath+"/"+fileName, true, true);
	                 	    	   }
	                 	    	   else{
	                 	    	      sftp.rm(fileName);
	                 	    	   }
	                 	       }
	                 	       else{
	                 	    	   if(!arr.isDir()){
	                 			        sftp.rm(fileName);
	                 	    	   }
	                 		   }
	                 		  
	                   }
	              arr = null;
	              }
	          obj = null;
	        }
	        if(pathDelete){
	     	   sftp.cd(remoteFilePath);
	     	   sftp.cd("../");
	     	   sftp.rmdir(remoteFilePath);
	        }
	      }
	      System.out.printf("ɾ��������"+sftp.getSession().getHost()+":"+remoteFilePath+"Ŀ¼�µ��ļ�");
	    }catch(Exception e){
      System.out.printf("ɾ��Զ���ļ�ʧ��",e);
      throw new Exception("ɾ��Զ���ļ�ʧ��");
    }
  }
  
  
  //�����ļ������뷽ʽ
  private void setFilenameEncoding(String fileNameEncoding) throws Exception{
	  try{
       this.sftp.setFilenameEncoding(fileNameEncoding);}
	  catch(Exception e){
		  System.out.printf("�����ļ������뷽ʽ����",e);
	      throw new Exception("�����ļ������뷽ʽ����");
	  }
  }
  
  //�ж�Ŀ¼�Ƿ����
  public boolean  changeWorkingDirectory(String workingDirectory){
 	 boolean flag=false;
 	 try{
 		 this.sftp.cd(workingDirectory);
 		 flag=true;
 	 }
 	 catch(Exception e){
 		System.out.println("Ŀ¼:"+workingDirectory+"������");
 		return false;
 	 }
 	 return flag;
   }
	/**
   * Զ������ָ��·���µ��ļ��ƶ�����·����
   * @param oldPath  Զ���������ļ�����·��
   * @param newPath  Զ���������ļ���Ҫ�ƶ�����·��
   * @param fileName  Զ��������Ҫ�ƶ����ļ���
   * @throws Exception
   * add by liwh
   */
  public void renameFile(String oldPath, String newPath, String fileName) throws Exception{
	  try {
			sftp.cd(rootPath);
			sftp.cd(oldPath);
			Vector fileNames = sftp.ls(fileName);
			if(fileNames != null && fileNames.size()>0){
				for(int i=0; i<fileNames.size(); i++){
					 Object obj=fileNames.elementAt(i);
			            if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
			                   SftpATTRS arr = ((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getAttrs();
			                   String file = ((com.jcraft.jsch.ChannelSftp.LsEntry )obj).getFilename();
			                   //�������� . �� .. ��������
			                   if (!file.equals("..") && !file.equals(".")) {
			                 	   sftp.rename(oldPath+"/"+file, newPath+"/"+file);
			                 	  System.out.printf("��"+newPath+"�ƶ��ļ��ɹ�");
						          break;
			                   }
			                   arr = null;
			              }
			          obj = null;
			        }
				}
		} catch (Exception e) {
			e.printStackTrace();
        	throw new Exception("�ļ��ƶ���������");
		}
      
  }

}