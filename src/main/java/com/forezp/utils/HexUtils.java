package com.forezp.utils;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class HexUtils {

	// 转化十六进制编码为字符串
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			s = new String(baKeyword, "GBK");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	//将十六进制的字符转成十进制的数据
	public static long convertHexToInt(String hex){
		return Long.parseLong(hex, 16);
	}

	//将十六进制的字符转成ascii字符串
	public static String toASCII(String hexStr){
		int len = hexStr.length();
		StringBuilder str = new StringBuilder();
		for(int i=0; i<len; i=i+2){
			String hex = hexStr.substring(i,i+2);
			str.append((char)Integer.parseInt(hex,16));
		}
		return str.toString();
	}

	//获取整个文件的十六进制字符串
	public static String readFileToHex(File file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		StringWriter sw = new StringWriter();

		int len = 1;
		byte[] temp = new byte[len];

		/* 16进制转化模块 */
		for (; (fin.read(temp, 0, len)) != -1;) {
			if (temp[0] > 0xf && temp[0] <= 0xff) {
				sw.write(Integer.toHexString(temp[0]));
			} else if (temp[0] >= 0x0 && temp[0] <= 0xf) {// 对于只有1位的16进制数前边补“0”
				sw.write("0" + Integer.toHexString(temp[0]));
			} else { // 对于int<0的位转化为16进制的特殊处理，因为Java没有Unsigned int，所以这个int可能为负数
				sw.write(Integer.toHexString(temp[0]).substring(6));
			}
		}

		return sw.toString();
	}


	public static StringBuilder toHexString(byte[] file){
//		StringWriter sw = new StringWriter();
		StringBuilder sw = new StringBuilder();
		int len = file.length;
		for(int i = 0; i<len; i++){
			if (file[i] > 0xf && file[i] <= 0xff) {
				sw.append(Integer.toHexString(file[i]));
			} else if (file[i] >= 0x0 && file[i] <= 0xf) {// 对于只有1位的16进制数前边补“0”
				sw.append("0" + Integer.toHexString(file[i]));
			} else { // 对于int<0的位转化为16进制的特殊处理，因为Java没有Unsigned int，所以这个int可能为负数
				sw.append(Integer.toHexString(file[i]).substring(6));
			}
		}
		return sw;
	}

	public static void main(String[] args) {
		System.out.println(toHexString("123".getBytes()));
	}

}
