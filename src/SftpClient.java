
import java.io.File;
import java.util.Properties;
import java.util.Vector;
public class SftpClient {


  private ChannelSftp sftp = null;
  private String rootPath = "/home/bea/update/zw";
  private SftpClient sftpClient=null;


  
  /**
   * 构造器
   */
  public SftpClient(){
	
  }
  public SftpClient(String hostName,String user,String password,int port) throws Exception{
    SftpClient f=new SftpClient();
    sftp =f.connect(hostName, user, password,port);
  }

  /**
   * 创建ftp连接
   * @param hostName  ftp服务器主机地址
   * @param user  ftp登录用户名
   * @param password  ftp登录口令
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
      System.out.printf("对方机器无法连接",e);
      sftp = null;
      throw new Exception("对方机器无法连接");
    }
    return sftp;
  }

  /**
   * 关闭ftp连接
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
      System.out.printf("关闭sftp连接失败",e);
      throw new Exception("关闭sftp连接失败");
    }
  }

  /**
   * 从sftp服务器上取指定的文件，并将其保存在本地机器指定的文件中。
   * @param localFileName  本机文件的绝对路径文件名
   * @param remoteFilePath  远程主机文件存放的绝对路径
   * @param remoteFileName  远程主机文件的文件名（不包括路径）
   * @throws Exception
   */
  public void getFile(String localFileName,String remoteFilePath,String remoteFileName) throws Exception{
    try {
    	sftp.cd(rootPath);
        sftp.cd(remoteFilePath);
        sftp.get(remoteFilePath + "/" + remoteFileName ,localFileName);
    }catch (Exception e) {
      System.out.printf("取远程主机文件错误", e);
      throw new Exception("取远程主机文件错误");
    }catch(Throwable t){
      t.printStackTrace();
      System.out.printf(t.getMessage());
      throw new Exception("取远程主机文件错误");
    }
  }

  /**
   * 从ftp服务器的指定路径上取所有文件，并将它们保存在本地机器指定的路径下。
   * @param localFilePath  本地文件存放绝对路径
   * @param remoteFilePath  远程主机文件存放绝对路径
   * @throws Exception
   */
  public void getMFile(String localFilePath,String remoteFilePath) throws Exception{
    try{
    	sftp.cd(rootPath);
		  //判断本地目录是否存在，若不存在则建立
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
	                         if (!fileName.equals("..") && !fileName.equals(".")) {    //档案名称 . 和 .. 不做处理
	                             if (!arr.isDir()){             //不是目录才处理
	                                 sftp.get(remoteFilePath + "/" + fileName ,localFilePath + File.separator+ fileName );
	                              }
	                          }
	                                   arr = null;
	                   }
	                       obj = null;
	              }
	        }
    }catch(Exception e){
        System.out.printf("取远程主机文件错误",e);
        throw new Exception("取远程主机文件错误");
    }
  }

  /**
   * 将本地机器指定的文件以指定的文件名传送到远程机器上，本方法只用于传送一个文件
   * @param localFileName  本地机器文件名
   * @param remoteFileName  远程主机文件名
   * @throws Exception
   */
  public void putFile(String localFileName,String remoteFileName) throws Exception{
	  try {
	    	 sftp.cd(rootPath);
	    	 File localFile = new File(localFileName);
	         if (!localFile.exists()) {
	           System.out.println("本地文件不存在");
	           return;
	         }
	         sftp.cd(rootPath);
	   	  sftp.put(localFileName, remoteFileName);
	      System.out.println("向服务器"+sftp.getSession().getHost()+"放置文件："+remoteFileName);
	    }
    catch (Exception e) {
      throw new Exception("放置远程主机文件错误");
    }
  }

  /**
   * 将本地机器指定的路径下所有文件传送到远程机器指定的路径下，本方法可传送多个文件
   * @param localFilePath  本地文件路径
   * @param remoteFilePath  远程机器文件存放路径
   * @param directory 子目录传送标志
 * @throws  
   * @throws Exception
   */
  public void putMFile(String localFilePath,String remoteFilePath,boolean directory) throws Exception {
	  try{    	
		    sftp.cd(rootPath);
		  	File path = new File(localFilePath);
		  	File[] files = path.listFiles();
		      //判断服务器目录是否存在，若不存在则建立目录
			    if(!this.changeWorkingDirectory(remoteFilePath)&&files.length>0){
			    	sftp.mkdir(remoteFilePath);
			    	System.out.printf("目录"+remoteFilePath+"创建成功");
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
		   System.out.printf("批量向服务器"+sftp.getSession().getHost()+":"+remoteFilePath+"目录下放置文件");
    }catch(Exception e){
        System.out.printf("放置远程主机文件错误",e);
        throw new Exception("放置远程主机文件错误");
    }
  }

  /**
   * 删除远程主机上指定的文件
   * @param remoteFileName  远程主机的绝对路径文件名
   * @throws Exception
   */
  public void deleteFile(String remoteFileName) throws Exception{
	  try{
	    	 sftp.cd(rootPath);
	         sftp.rm(remoteFileName);
	      System.out.printf("删除服务器"+sftp.getSession().getHost()+"的文件:"+remoteFileName);
	    }catch(Exception e){
      System.out.printf("删除远程文件失败",e);
      throw new Exception("删除远程文件失败");
      
    }
  }

  /**
   * 删除远程主机指定路径下的所有文件
   * @param remoteFilePath  远程主机的路径
   * @param pathDelete  删除目录标志，true表示删除目录以及其下的文件，false表示只删除其下的文件
   * @param subDirectDelete 删除子目录标志，true表示删除子目录，false表示不删除子目录
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
	                   //档案名称 . 和 .. 不做处理
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
	      System.out.printf("删除服务器"+sftp.getSession().getHost()+":"+remoteFilePath+"目录下的文件");
	    }catch(Exception e){
      System.out.printf("删除远程文件失败",e);
      throw new Exception("删除远程文件失败");
    }
  }
  
  
  //设置文件名编码方式
  private void setFilenameEncoding(String fileNameEncoding) throws Exception{
	  try{
       this.sftp.setFilenameEncoding(fileNameEncoding);}
	  catch(Exception e){
		  System.out.printf("设置文件名编码方式出错",e);
	      throw new Exception("设置文件名编码方式出错");
	  }
  }
  
  //判断目录是否存在
  public boolean  changeWorkingDirectory(String workingDirectory){
 	 boolean flag=false;
 	 try{
 		 this.sftp.cd(workingDirectory);
 		 flag=true;
 	 }
 	 catch(Exception e){
 		System.out.println("目录:"+workingDirectory+"不存在");
 		return false;
 	 }
 	 return flag;
   }
	/**
   * 远程主机指定路径下的文件移动到新路径下
   * @param oldPath  远程主机的文件所在路径
   * @param newPath  远程主机的文件所要移动到的路径
   * @param fileName  远程主机中要移动的文件名
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
			                   //档案名称 . 和 .. 不做处理
			                   if (!file.equals("..") && !file.equals(".")) {
			                 	   sftp.rename(oldPath+"/"+file, newPath+"/"+file);
			                 	  System.out.printf("往"+newPath+"移动文件成功");
						          break;
			                   }
			                   arr = null;
			              }
			          obj = null;
			        }
				}
		} catch (Exception e) {
			e.printStackTrace();
        	throw new Exception("文件移动发生错误");
		}
      
  }

}