

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class Test {

	
	public static void main(String[] args)  {
	    try {
	      ArrayList<String> lineTxt= new ArrayList<String>();
	      lineTxt=deploy.ReadTxt("F:\\SAVEFILE\\test.txt", "GBK");
	      String xmlString = lineTxt.get(0);
	      DOMParser parser = new DOMParser();
	      byte[] tempByte = xmlString.getBytes();
	      ByteArrayInputStream bais = new ByteArrayInputStream(tempByte);
	      BufferedReader reader = new BufferedReader(new InputStreamReader(bais,
	          "gbk"));
	      String valueString = null;
	      while ((valueString = reader.readLine())!=null){
	    	  System.out.println(new String(xmlString.getBytes(),"gbk"));
	      }
	      parser.parse(reader);
	    }
	    catch (Exception e) {
	      
	    }
	}
	
}
