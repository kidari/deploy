

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
				System.out.println("�����ļ��Ѵ���");
			}
			FileWriter resultFile= new FileWriter(file);
			PrintWriter NewFile= new PrintWriter(resultFile);
			for(int i=0;i<lineTxt.size();i++){
			    String temp= deploy.getData("SC");
			    temp= temp.replace("#sql#", lineTxt.get(i));//�޸������ݲ���
				NewFile.println(temp);
			}
			resultFile.close();
			System.out.println(filePath+"�ļ����ɳɹ�");
	     }catch(Exception e){
		System.out.println("�޷��������ļ�");
		e.printStackTrace();
	  }
    }
	public static void SqlToTxt(String filePath,ArrayList<String> lineTxt){
		try{
			File file= new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}else{
				System.out.println("�����ļ��Ѵ���");
			}
			FileWriter resultFile= new FileWriter(file);
			PrintWriter NewFile= new PrintWriter(resultFile);
			for(int i=0;i<lineTxt.size();i++){
			    String temp=  lineTxt.get(i);//�޸������ݲ���
			    temp= temp.trim();
				String test[]= temp.split("\"");
				int len= test.length;
				String dataValue= "";
				for(int j=1;j<len;j++){//��һ��"ǰ����
					if(j== (len-1)&&(test[j].trim()).equals("+")){
					}else{
						dataValue+= test[j];
					}
					//System.out.println(test[j]);
				}
				NewFile.println(dataValue);
			}
			resultFile.close();
			System.out.println(filePath+"�ļ����ɳɹ�");
	     }catch(Exception e){
		System.out.println("�޷��������ļ�");
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
		 System.out.println("��ѡ��\n======");
		   System.out.println("-->��( 1 )����");
		   System.out.println("-->��( 2 )��ԭ\n");
		   String way="";
	       BufferedReader input = new BufferedReader(new InputStreamReader(System.
	               in));
	           try {
				way = input.readLine();
				if (way.equals("1")){
					//����SQL
					CreateNewFile(outputfile,lineTxt);
					}
				else if(way.equals("2")){
					//��ԭSQL
					SqlToTxt(outputfile,lineTxt);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
		
	}
}
