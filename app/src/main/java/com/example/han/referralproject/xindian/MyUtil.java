package com.example.han.referralproject.xindian;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author fangrf 2017/07/15
 */
public class MyUtil {
	
	/**
	 * @param filePath
	 * @param fileName
	 * @param datas
	 */
	public static void writeFile(String filePath,String fileName,List<Integer> datas){
		if(datas==null || datas.size()==0){
			return;
		}
		
		File dir = new File(filePath);
        if (!dir.exists()) {
        	dir.mkdir(); //�����ļ���
        }
        
        filePath = filePath+"/"+fileName;
        
        File file = new File(filePath);
		if (file.exists()) {		 
			file.delete();
		}
		try {
			file.createNewFile();//���ļ����д����ļ�
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		byte[] buffer = new byte[datas.size()*4 +1]; 
		int index = 0;
		
		for(int j = 0;j < datas.size();j++){							
			int data = datas.remove(0);
			/* ����1��int���ݣ����ֽ���ǰ�����ֽ��ں� */
			buffer[index++] =  (byte) (data & 0xff);
			buffer[index++] =  (byte) ((0xff00 & data) >> 8);
			buffer[index++] =  (byte) ((0xff0000 & data) >> 16);
			buffer[index++] =  (byte) ((0xff000000 & data) >> 24);			
		}
		System.out.println("datas.size()*4="+datas.size()*4 +",index->"+index);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath,true);
			fos.write(buffer, 0, index);
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(fos!= null){
					fos.close();
				}
				fos = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * @param filePath
	 * @return
	 */
	public static List<Integer> readFile(String filePath) {
		try {
			FileInputStream fin = new FileInputStream(filePath);
			InputStream in = new BufferedInputStream(fin);

			byte[] buffer = new byte[1024];
			int index = 0;
			int data = 0;
			List<Integer> datas = new ArrayList<Integer>();

			while ((index = in.read(buffer)) != -1) {
				for (int j = 0; j < index;) {//��ԭ1��int����
					data = buffer[j++] & 0xff;
					data = data + ((buffer[j++] << 8) & 0xff00);
					data = data + ((buffer[j++] << 16) & 0xff0000);
					data = data + ((buffer[j++] << 24) & 0xff000000);
					datas.add(data);
				}
			}
			in.close();
			
			return datas;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
