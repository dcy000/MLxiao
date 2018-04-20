/*
 * 日期 : 2016年12月7日<br>
 * 作者 : lintax<br>
 * 功能 : 分析拼音相似性，进行字符替换<br>
 */

package com.zane.androidupnpdemo.pinyin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * An object to convert Chinese character to its corresponding pinyin string.
 * Change word to our target word.
 */
public class PinyinSimilarity {
    
    String[] englishPinYin26={
    		"EI1", "BI4", "SEI4", "DI4", "YI4", "EFU1", "JI4", 
    		"EIQI1", "AI4", "JEI4", "KEI4", "EOU1", "EMEN1", "EN1", 
    		"OU1", "PI1", "KIU1", "A4", "ESI1", "TI4", 
    		"YOU4", "WEI4", "DABULIU3", "EKESI1", "WAI4", "ZEI4"
    };

    String englishString26="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String numberStringArabic="0123456789";
    String numberString="零一二三四五六七八九十百点";
    String specialHanziString="年级班分";
    String myCharAll = numberString + specialHanziString;

    List<String> numberPinYin=new ArrayList<String>(20);//数字的拼音(10)
    List<String> specialHanziPinYin=new ArrayList<String>(10);//特定汉字集的拼音（除了中文的数字之外的）
    List<String> myCharAllPinYin=new ArrayList<String>(40);//所有拼音的集合
 
    boolean fuzzyMatching=true;//是否开启模糊匹配功能
    
    public PinyinSimilarity(boolean fuzzyMatching){
    	this.fuzzyMatching = fuzzyMatching;
    	init();
    }    
    
 	//拼音中有音标
    //初始化目标汉字集的拼音列表
 	public void init()  
    {  
		try{			
			String str ;
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
			
			str = numberString;//数字
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);				
				numberPinYin.add(vals[0]);
			}

			str = specialHanziString;//汉字
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);				
				specialHanziPinYin.add(vals[0]);
			}
	       
	        myCharAllPinYin.addAll(numberPinYin);
	        myCharAllPinYin.addAll(specialHanziPinYin);
	        
	        checkSimilarity(myCharAllPinYin);        
	        
		} catch (Exception e){
			e.printStackTrace();
		}
    }  
 	 	
	public String changeOurWordsWithPinyin(String input){
		String output=input;
		try{
			
			//处理符号：不关注符合，遇到，就去掉（要保留小数点）
			output = changeWordProcessSignal(output);
			
			//处理英文字母：转大写
			output = changeWordProcessEnglish(output);
			int index;
			String str;
			String strChanged;
			StringBuilder strBuilder = new StringBuilder();			
			for(index=0;index<input.length();index++){
				str = input.substring(index,index+1);
				strChanged = changeOneWord(str);
				strBuilder.append(strChanged);
			}
			
			output=strBuilder.toString();

		} catch (Exception e){
			e.printStackTrace();
		}
		
		return output;
	}
 	
	public String changeWordProcessSignal(String strInput){
		String strOutput = strInput;
		
		//去掉 ，。空格- 
		strOutput = strOutput.replace("，", "");
		strOutput = strOutput.replace("。", "");
		strOutput = strOutput.replace("-", "");
		strOutput = strOutput.replace(" ", "");

		return strOutput; 
	}	

	public String changeWordProcessEnglish(String strInput){
		String strOutput = strInput;
		
		//转大写
		strOutput = strOutput.toUpperCase();
		
		return strOutput; 
	}
	
 	public String changeEnglishWord(String strInput){
 		if(englishString26.contains(strInput)){
 	    	return strInput;
 	    }
 		
 		List<String> listEnglishPinYin = new ArrayList<String>();
 		listEnglishPinYin.addAll(Arrays.asList(englishPinYin26));
 		
 		return changeWord(strInput, listEnglishPinYin, englishString26);
 	}
 	 	
 	//尾字如果是汉字，进行拼音相同字的替换（零不能替换，可以先转换为0）
 	public String changeOneWord(String strInput){
 		//若已经在目标集合中了，就不需要转换了
 	    if(numberString.contains(strInput)||numberStringArabic.contains(strInput)){
 	    	return strInput;
 	    } else if(specialHanziString.contains(strInput)){
 	    	return strInput;
 	    }
 	    
 	    String strChanged;
 		List<String> listEnglishPinYin = new ArrayList<String>();
 		 		
 		strChanged = changeWord(strInput, numberPinYin, numberString);
 		if(numberString.contains(strChanged)){
  	    	return strChanged;
  	    }
 		
 		return changeWord(strInput, specialHanziPinYin,  specialHanziString);
 	}
 	
/**
 *  	只替换一个字，先分析字符类型，获取拼音，然后与目标进行对比
 *		参数说明：
 *		strInput：输入字符串，目前只支持单个字符
 *		listPinYin:拼音列表
 *		strSource：字符列表，要求与拼音是对应的。
 */ 	
    private String changeWord(String strInput, List<String> listPinYin, String strSource) {
		
    	//先判断输入，是什么类型的字符：数字、字母、汉字
    	String strOutput="";
    	
		String str=strInput.substring(0,1);		
		String strPinyin = "";
		boolean flagGetPinyin=false;
           
		try{
			
			if(str.matches("^[A-Z]{1}$")){
				
	            strPinyin = englishPinYin26[englishString26.indexOf(str)];
	            flagGetPinyin = true;
			}
			else if(str.matches("^[0-9]{1}$")){
				
	            strPinyin = numberPinYin.get(numberString.indexOf(str));
	            flagGetPinyin = true;
			}
			else if(str.matches("^[\u4e00-\u9fa5]{1}$")){
				
				HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
				format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
				format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

				char c = str.charAt(0);
				String[] vals = PinyinHelper.toHanyuPinyinStringArray(c, format);				
	
	            strPinyin=vals[0];//token.target;
	            flagGetPinyin = true;
			}             
	            
			if(flagGetPinyin){
	            int num=0;

	            //在目标拼音集合中查找匹配项
	            num=listPinYin.indexOf(strPinyin);
	            if(num>=0){ //拼音精确匹配成功
	            	return strSource.substring(num, num+1);
	            } else {

	            	if(fuzzyMatching){//若开启了模糊匹配
		        
	            		String strPinyinFuzzy;
	            		
	            		//声母替换
	            		strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串
	            		
	            		strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
	            		boolean flagReplacedHeadString = (strPinyinFuzzy==null)?false:true;
//	            		LogUtil.logWithMethod(new Exception(),"flagReplacedHeadString="+flagReplacedHeadString);
	            		if(flagReplacedHeadString){
	            			num=listPinYin.indexOf(strPinyinFuzzy);
//	            			LogUtil.logWithMethod(new Exception(), "del Phonogram, replace Initials,strPinyinFuzzy="+strPinyinFuzzy+": indexOf num="+num);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}
	            		
	            		//韵母替换
	            		strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串，不使用声母替换后的字符串
	            		strPinyinFuzzy = replaceTailString(strPinyinFuzzy);
	            		boolean flagReplacedTailString = (strPinyinFuzzy==null)?false:true;
	            		if(flagReplacedTailString){
	            			num=listPinYin.indexOf(strPinyinFuzzy);
//	            			LogUtil.logWithMethod(new Exception(), "del Phonogram, replace Vowel,strPinyinFuzzy="+strPinyinFuzzy+": indexOf num="+num);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}
	            		
	            		//声母韵母都替换
	            		if(flagReplacedHeadString && flagReplacedTailString){
		            		strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);		            		
	            			num=listPinYin.indexOf(strPinyinFuzzy);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}
	            		
	            		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	            		//去掉音标后，再次进行模糊匹配
	            		strPinyin=strPinyin.substring(0, strPinyin.length()-1);
	            		
	            		strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串
	            		num=findPinyin(strPinyinFuzzy,listPinYin);
	            		if(num>=0){ //拼音模糊匹配成功
         	            	return strSource.substring(num, num+1);
         	            }
	            		
	            			            		
	            		//声母替换
	            		strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
	            		flagReplacedHeadString = (strPinyinFuzzy==null)?false:true;
	            		if(flagReplacedHeadString){

	            			num=findPinyin(strPinyinFuzzy,listPinYin);
	            			
//	            			LogUtil.logWithMethod(new Exception(), "del Phonogram, replace Initials,strPinyinFuzzy="+strPinyinFuzzy+": indexOf num="+num);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}
	            		
	            		//韵母替换
	            		strPinyinFuzzy = new String(strPinyin) ;//避免修改原字符串，不使用声母替换后的字符串
	            		strPinyinFuzzy = replaceTailString(strPinyinFuzzy);
	            		flagReplacedTailString = (strPinyinFuzzy==null)?false:true;
	            		if(flagReplacedTailString){
	            			num=findPinyin(strPinyinFuzzy,listPinYin);
//	            			LogUtil.logWithMethod(new Exception(), "del Phonogram, replace Vowel,strPinyinFuzzy="+strPinyinFuzzy+": indexOf num="+num);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}
	            		
	            		//声母韵母都替换
	            		if(flagReplacedHeadString && flagReplacedTailString){
	            			strPinyinFuzzy = replaceHeadString(strPinyinFuzzy);
	            			num=findPinyin(strPinyinFuzzy,listPinYin);
	            			if(num>=0){ //拼音模糊匹配成功
	         	            	return strSource.substring(num, num+1);
	         	            }
	            		}	            		
	            		return str;
	            		
		            } else {
		            	return str;
		            }
	            } 
	            
			} else {//若该字符没有找到相应拼音，使用原字符
				strOutput = strInput;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
    	return strOutput;
    	
	}
    
    
    private String replaceHeadString(String strPinyin){
    	
		//声母替换
    	String strReplaced = null;
		if(strPinyin.contains("ZH")){
			strReplaced = strPinyin.replace("ZH", "Z");	            			
		} else if(strPinyin.contains("CH")){
			strReplaced = strPinyin.replace("CH", "C");	            			
		} else if(strPinyin.contains("SH")){
			strReplaced = strPinyin.replace("SH", "S");
		} 
		else if(strPinyin.contains("Z")){
			strReplaced = strPinyin.replace("Z", "ZH");	            			
		} else if(strPinyin.contains("C")){
			strReplaced = strPinyin.replace("C", "CH");	            			
		} else if(strPinyin.contains("S")){
			strReplaced = strPinyin.replace("S", "SH");	            			
		}
		else if(strPinyin.contains("L")){
			strReplaced = strPinyin.replace("L", "N");	            			
		} else if(strPinyin.indexOf('N')==0){ //n有在后面的，n只在做声母时易混
			strReplaced = strPinyin.replace("N", "L");	            			
		} else {
			return null;
		}
		
		return strReplaced;//flagReplaced;
    	
    }
    
	private String replaceTailString(String strPinyin) {

		// 韵母替换
		String strReplaced = null;
		if (strPinyin.contains("ANG")) {
			strReplaced = strPinyin.replace("ANG", "AN");
		} else if (strPinyin.contains("ENG")) {
			strReplaced = strPinyin.replace("ENG", "EN");
		} else if (strPinyin.contains("ING")) {
			strReplaced = strPinyin.replace("ING", "IN");
		} else if (strPinyin.contains("AN")) {
			strReplaced = strPinyin.replace("AN", "ANG");
		} else if (strPinyin.contains("EN")) {
			strReplaced = strPinyin.replace("EN", "ENG");
		} else if (strPinyin.contains("IN")) {
			strReplaced = strPinyin.replace("IN", "ING");
		} else {
			return null;
		}
		return strReplaced;
	}

    //在指定拼音集合中寻找某个不带音标的拼音
 	//缺少一个音标，不能使用List的indexOf()了
    private int findPinyin(String strPinyin, List<String> listPinYin){ 
        int num=0;        
        //在目标拼音集合中查找匹配项
        for(String strTmp:listPinYin){
            if(strTmp.contains(strPinyin) && strPinyin.length()==(strTmp.length()-1) ){
            	return num;
            }
            num++;
        }        
        return -1;
    }
    
    void checkSimilarity(List<String> list){
    	int i=0;
    	int len = list.size();
    	for(String str : list){
    		for(int j=i+1;j<len;j++){
		    	if(str.contentEquals(list.get(j))){}
    		}
    		i++;
    	}
    }

    
}
