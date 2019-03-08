package com.forezp.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncodeUtil {
	
	protected final static Log log = LogFactory.getLog(EncodeUtil.class);
	
	/**
	 * 调换单双号的字符
	 *
	 * @param SimCode
	 * @return
	 */
	public static String switchString(String str){
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < str.length()/2+1; i++) {
			if(i*2-1<str.length()){
				sb.append(str.charAt(i*2-1));
			}
			if(i*2-2<str.length()){
				sb.append(str.charAt(i*2-2));
			}
		}
		return sb.toString();
	}
	
	/**
	 * translate the 12 simCode to 11 simCode
	 *
	 * @param SimCode
	 * @return
	 */
	public static String tranSimCode12To11(String SimCode){
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < SimCode.length()/2+1; i++) {
			sb.append(SimCode.charAt(i*2-1));
			sb.append(SimCode.charAt(i*2-2));
		}
		return sb.substring(0, sb.length()-1);
	}
	
	/**
	 * 将字节转换成原码的long型数字
	 * get the byte[]'s original value to long
	 * @param bytes
	 * @return
	 */
	public static long originalValue(byte[] bytes){
		long retValue  = 0;
		String binaryStr = byte2binaryStr(bytes);
		if(binaryStr.startsWith("0")){
			retValue = absBinaryStrToLong(binaryStr.substring(1));
		}else{
			retValue = absBinaryStrToLong(binaryStr.substring(1))*(-1);
		}
		return retValue;
	}
	
	
	
	/**
	  * get the byte 's unsign value ,and then to formate length string,
	  * for example: 
	  * 1) byteToIntStr((byte)0x01,3) --->return : 001
	  * 2)  byteToIntStr((byte)0x10,1) --->return : 2
	 * @param b
	 * @param formatLen
	 * @return
	 */
	public static String byteToIntStr(byte b,int formatLen) {
		int i = getUnsignValue(b);
		String temp = new String(i+"");
		StringBuffer sb = new StringBuffer();
		if (temp.length()< formatLen) {
			sb.append(temp);
			for (int j = 0; j < formatLen - temp.length(); j++) {
				sb.insert(0, "0");
			}
		}else{
			sb.append(temp.substring(temp.length()-formatLen));
		}
		return sb.toString();
	}
	
	/**
	  * let two ASCII char to a byte
	  * for example  "EF"--> 0xEF
	  * @param src0 byte
	  * @param src1 byte
	  * @return byte
	  */
	public static byte uniteBytes(byte src0, byte src1) {
	   byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
	   _b0 = (byte) (_b0 << 4);
	   byte _b1 = Byte.decode("0x" + new String(new byte[] {src1})).byteValue();
	   byte ret = (byte) (_b0 ^ _b1);
	   return ret;
	}
	
	/**
	  * let the string src to hex byte array ,every tow char to a byte
	  * for example "2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	  * @param src String
	  * @return byte[]
	  */
	public static byte[] HexString2Bytes(String hexStr,int byteLen) {
	   int strLen = hexStr.length();
	   StringBuffer sb = new StringBuffer(hexStr);
	   if(byteLen>strLen/2){
		   int setLen = byteLen*2;
		   for (int i = 0; i < setLen-strLen; i++) {
			sb.insert(0, "0");
		   }
	   }
	   hexStr = sb.toString();
	   byte [] ret  = new byte[byteLen];
	   byte[] tmp = hexStr.getBytes();
	   for (int i = 0; i < byteLen; i++) {
		   ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
	   }	   
	   return ret;
	}
	
	/**
	  * let the string src to hex byte array ,every tow char to a byte
	  * for example "2B" --> 0x2B
	  * @param src String
	  * @return byte[]
	  */
	public static byte HexString2Byte(String hexStr) {
	   StringBuffer sb = new StringBuffer(hexStr);
	   hexStr = sb.toString();
	   byte ret = (byte)0x00;
	   byte[] tmp = hexStr.getBytes();
	   for (int i = 0; i < 1; i++) {
		   ret= uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
	   }	   
	   return ret;
	}
//num  BCD  num	BCD	num	BCD	num	BCD
//	0	0x0	4	0x4	8	0x8	keep 0xC
//	1	0x1	5	0x5	9	0x9	keep 0xD
//	2	0x2	6	0x6	*	0xA	keep 0xE
//	3	0x3	7	0x7	#	0xB	濉┖	0xF
	public static String toBCDEncode(String str){
		str = str.replaceAll("\\*", "A");
		str = str.replaceAll("\\#", "B");
		str = str.replaceAll(" " , "F");
		return str;
	}
	
	/**
	 * 转换为规则长度的BCD码
	 * 13800571505--->3108501705F5FFFFFFFF
	 * @param str
	 * @param length
	 * @return
	 */
	public static String toBCDEncodeWithF(String str,int length){
		StringBuilder sb = new StringBuilder();
		if(str.length()<length){
			String fStr = "";
			for (int index = 0; index < length-str.length(); index++) {
				fStr +="F";
			}
			str +=fStr;
		}
		for (int i = 1; i < str.length()/2+1; i++) {
			sb.append(str.charAt(i*2-1));
			sb.append(str.charAt(i*2-2));
		}
		return sb.toString();
	}
	

	/**
	 * get the byte[] 's validateCode
	 * @param bytes
	 * @param start
	 * @param end
	 * @return
	 */
	public static byte getValidate(byte[] bytes,int start,int end){
		int check = 0;
		for (int i = start; i < end; i++) {
			check +=getUnsignValue(bytes[i]);
		}
		byte kk = (byte)(check%256);
		return kk;
	}
	
	public static byte swap(byte b) {
		b = (byte) (((b & 0xf0) >> 4) + ((b & 0xf) << 4));
		return b;
	}
	
	public static String getHexStr(byte temp){
		return Integer.toHexString((temp & 0xFF)+0x100).substring(1).toUpperCase();
	}

	
	public static String getHexStr(byte[] temp){
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < temp.length; index++) {
			sb.append(getHexStr(temp[index]));
		}
		return sb.toString();
	}
	
	public static void printHexStr(byte[] temp){
		log.info("  buffer's Hex str:"+getHexStr(temp));
	}
	
	public static String int2HexStr(int i,int formatLen){
		String hexStr = Integer.toHexString(i);
		if(hexStr.length()<formatLen){
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j =hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
		}else{
			StringBuffer sb = new StringBuffer("");
			sb.append(hexStr.substring(hexStr.length()-formatLen));
			return sb.toString().toUpperCase();
		}
	}
	
	public static String int2HexStr(long i,int formatLen){
		String hexStr = Long.toHexString(i);
//		System.out.println(  "hexStr=="+hexStr);
		if(hexStr.length()<formatLen){
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j =hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
//		}
		}else{
			StringBuffer sb = new StringBuffer("");
			sb.append(hexStr.substring(hexStr.length()-formatLen));
			return sb.toString().toUpperCase();
		}
	}
	
	/**
	 * conver long type to format length's hex string
	 * @param i
	 * @param formatLen
	 * @return
	 */
	public static String long2HexStr(long i,int formatLen){
		String hexStr = Long.toHexString(i);
//		System.out.println(  "hexStr=="+hexStr);
		if(hexStr.length()<formatLen){
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j =hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
//		}
		}else{
			StringBuffer sb = new StringBuffer("");
			sb.append(hexStr.substring(hexStr.length()-formatLen));
			return sb.toString().toUpperCase();
		}
	}
	
	/**
	 * conver long type to format length's binary string
	 * @param i 
	 * @param formatLen
	 * @return
	 */
	public static String long2BinaryStr(long i,int formatLen){
		String binaryStr = Long.toBinaryString(i);
		if(binaryStr.length()<formatLen){
			StringBuffer sb = new StringBuffer(binaryStr);
			for (int index = binaryStr.length(); index < formatLen; index++) {
				sb.insert(0, "0");
			}
			return sb.toString();
		}else{
			StringBuffer sb = new StringBuffer("");
			sb.append(binaryStr.substring(binaryStr.length()-formatLen));
			return sb.toString();
		}
	}
	
	public static char[] bytesToChars(byte[] bytes){
		return new String(bytes).toCharArray();  
	}
	
	public static byte[] charsToBytes(char[] chars){
		return new String(chars).getBytes(); 
	}
	
	public static char byteToChar(byte bytes){
		return (char)bytes;
	}
	
	public static byte charToByte(char chars){
		return (byte)chars;  
	}
	
	public static int getUnsignValue(byte byteStr) {
		return byteStr & 0XFF;
	}
	
	public static int getUnsignValue(byte[] bytes) {
		return (int) getUnsignValueToLong(bytes);
	}
	
	public static long getUnsignValueToLong(byte[] bytes) {
		long v = 0;
		for (int i = 0; i < bytes.length ; i++) {
			v = (v << 8) + (bytes[i] & 0xff);
		}
		return v;
	}
	
	/**
	 * translate byte to 8 bit binary string
	 * @param bytes
	 * @return
	 */
	public static String byte2binaryStr(byte bytes){
		StringBuilder sb = new StringBuilder("00000000");
		for (int bit = 0; bit < 8; bit++) {
			if (((bytes >> bit) & 1) > 0) {
				sb.setCharAt(7 - bit, '1');
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * translate byte[] to  binary string
	 * and every byte to 8 bit binary string
	 * @param bytes
	 * @return
	 */
	public static String byte2binaryStr(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2binaryStr(bytes[i]));
		}
		return sb.toString();
	}
	
	/**
	 * get the absolute int value from binary string
	 * @param binaryStr
	 * @return
	 */
	public static int absBinaryStr(String binaryStr){
		double count =0;
		for (int index = 0; index < binaryStr.length(); index++) {
			char str = binaryStr.charAt(index);
			count += Integer.parseInt(""+str)*Math.pow(2.0, binaryStr.length()-index-1);
		}
		return (int)count;
	}
	
	/**
	 * get the absolute long value from binary string
	 * @param binaryStr
	 * @return
	 */
	public static long absBinaryStrToLong(String binaryStr){
		long count =0;
		for (int index = 0; index < binaryStr.length(); index++) {
			char str = binaryStr.charAt(index);
			count += Integer.parseInt(""+str)*Math.pow(2.0, binaryStr.length()-index-1);
		}
		return count;
	}
	
	
	public static int binaryToInt(String binaryStr){
		return Integer.parseInt(binaryStr, 2);
	}
	
	public static byte binaryStrToByte(String binaryStr){
		return (byte)Integer.parseInt(binaryStr, 2);
	}

	
	public static byte[] binaryStrToBytes(String binaryStr,int byteLen){
		byte[] bytes = new byte[byteLen];
		int binaryStrLen = byteLen*8;
		if(binaryStr.length()<binaryStrLen){
			StringBuffer sb = new StringBuffer(binaryStr);
			for (int i = binaryStr.length(); i < binaryStrLen; i++) {
				sb.insert(0,"0");
			}
			binaryStr = sb.toString();
		}else{
			binaryStr = binaryStr.substring(binaryStr.length()-binaryStrLen);
		}
		for (int index = 0; index < byteLen; index++) {
			String byteBin = binaryStr.substring(index*8,index*8+8);
			bytes[index] = binaryStrToByte(byteBin);
		}
		return bytes;
	}
	
    public static short byteArrayToShort(byte[] b) {
        return byteArrayToShort(b, 0);
    }

    public static short byteArrayToShort(byte[] b, int offset) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (b[i + offset] & 0x00FF) << shift;
        }
        return value;
    }
    
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
    
    public static char ascii2Char(int ASCII) {  
         return (char) ASCII;  
    }  
    
    public static int char2ASCII(char c) {  
        return (int) c;  
    }  

        
    public static int[] string2ASCII(String s) {// 字符串转换为ASCII码  
        if (s == null || "".equals(s)) {  
                return null;  
        }  
        char[] chars = s.toCharArray();  
        int[] asciiArray = new int[chars.length];  
        
        for (int i = 0; i < chars.length; i++) {  
            asciiArray[i] = char2ASCII(chars[i]);  
        }  
         return asciiArray;  
   }  
    
    
   public static String addZero(String base,int length,int addType){
       
       if(base.length() > length){
           return base;
       }
       StringBuffer sb = new StringBuffer();
       sb.append(base);
       while(sb.length() < length){
           if(addType == 0){
               sb.insert(0, "0");
           }else{
               sb.append(0);
           }
       }
       
       return sb.toString();
   }

	public static String toStringHex2(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
   
   public static void main(String[] args) {
   		//76,75,90,2,10,0,0,0,1,250,255,255,255,1,1,0,1,1,69,78,68

	   System.out.println(getHexStr("123".getBytes()));
   }

}
