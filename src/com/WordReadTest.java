package com;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordReadTest {

    public static void main(String[] args) throws InvalidFormatException {
        //mapAll��keyΪ��Ҫ�滻���ַ���valueΪ�滻��ʲô
        Map<String, Object> mapAll = new HashMap<String, Object>();
        mapAll.put("replace1", "0000000000");
        mapAll.put("replace2", "Do the test!!");
        mapAll.put("replace3", "Test");
        mapAll.put("replace4", "2222222222222");
        
        //���¶���һ��table��ģ���ļ�����Ҫ��2�У���һ��Ϊheader���ڶ���Ϊ�����滻�������Ƕ�����¼������table��name������ڵ�һ����λ����table�ĵ�һ�е�һ�У�
        //table�ĵڶ�����Test�е����ӣ������ǵ�һ��Ϊ�кţ����عܣ����ڶ���Ϊ<replace5>��������Ϊ<replace6>
        /**********table********************start****************************************/
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //��һ������
        Map<String, String> row1 = new HashMap<String, String>();
        row1.put("replace5", "-");
        row1.put("replace6", "WWWWWWWWWW");
        list.add(row1);
        
        //�ڶ�������
        Map<String, String> row2 = new HashMap<String, String>();
        row2.put("replace5", "");
        row2.put("replace6", "SSSSSSSSSS");
        list.add(row2);
        
        //����������
        Map<String, String> row3 = new HashMap<String, String>();
        row3.put("replace5", "11111");
        row3.put("replace6", "ZZZZZZZZZZZ");
        list.add(row3);

        mapAll.put("Table No.1", list);//table name Ϊ Table No.1
        /*******table***********************end****************************************/
        //ͼƬ�滻
        mapAll.put(WordUtil.IMAGE_ + "image",
                   "D:\\qianming.jpg");
        try {
            WordUtil a = new WordUtil();
            a.generateWordFromTemplate("D:\\Template.docx",
                                     "D:\\test.docx",
                                     mapAll);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        

}
