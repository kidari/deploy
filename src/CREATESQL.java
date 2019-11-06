

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class CREATESQL {
public final static String encode_GBK = "GBK";
	
	public static void CreateNewFile(String filePath,ArrayList<String> lineTxt){
		try{
			File file= new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}else{
				System.out.println("生成文件已存在");
			}
			FileWriter resultFile= new FileWriter(file);
			PrintWriter NewFile= new PrintWriter(resultFile);
			for(int i=0;i<lineTxt.size();i++){
			    String temp= deploy.getData("SC");
			    temp= temp.replace("#sql#", lineTxt.get(i));//修改行数据部分
				NewFile.println(temp);
			}
			resultFile.close();
			System.out.println(filePath+"文件生成成功");
	     }catch(Exception e){
		System.out.println("无法创建新文件");
		e.printStackTrace();
	  }
    }
	public static void SqlToTxt(String filePath,ArrayList<String> lineTxt){
		try{
			File file= new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}else{
				System.out.println("生成文件已存在");
			}
			FileWriter resultFile= new FileWriter(file);
			PrintWriter NewFile= new PrintWriter(resultFile);
			for(int i=0;i<lineTxt.size();i++){
			    String temp=  lineTxt.get(i);//修改行数据部分
			    temp= temp.trim();
				String test[]= temp.split("\"");
				int len= test.length;
				String dataValue= "";
				for(int j=1;j<len;j++){//第一个"前忽略
					if(j== (len-1)&&(test[j].trim()).equals("+")){
					}else{
						dataValue+= test[j];
					}
					//System.out.println(test[j]);
				}
				NewFile.println(dataValue);
			}
			resultFile.close();
			System.out.println(filePath+"文件生成成功");
	     }catch(Exception e){
		System.out.println("无法创建新文件");
		e.printStackTrace();
	  }
    }
	public static void main(String[] args){
		String date= deploy.getLimitTime();
		String filePath= deploy.getData("SQLINPUT");
		filePath= filePath.replace("#date#", date);
		ArrayList<String> lineTxt= new ArrayList<String>();
		lineTxt= deploy.ReadTxt(filePath, encode_GBK);
		String outputfile= deploy.getData("SQLOUTPUT");
		outputfile= outputfile.replace("#date#", date);
		 System.out.println("请选择\n======");
		   System.out.println("-->按( 1 )生成");
		   System.out.println("-->按( 2 )还原\n");
		   String way="";
	       BufferedReader input = new BufferedReader(new InputStreamReader(System.
	               in));
	           try {
				way = input.readLine();
				if (way.equals("1")){
					//生成SQL
					CreateNewFile(outputfile,lineTxt);
					}
				else if(way.equals("2")){
					//还原SQL
					SqlToTxt(outputfile,lineTxt);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
		
	}
}
