package com.forezp.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

public class HttpUtil {
	
	public static void main(String[] args) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", "张三");
		params.put("age", 11);
		params.put("11", 11);
		System.out.println(HttpUtil.Get("http://www.baidu.com", params));
		
	}
	
	public static String Get(String url) {
		try {
			return Request.Get(url).execute().returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String Get(String url,Map<String, Object> params) {
		try {
			StringBuffer sb = new StringBuffer(url);
			boolean flag = true;
			for (Iterator<Entry<String, Object>> it = params.entrySet().iterator();it.hasNext();){
				Entry<String, Object> entry = it.next();
				if (flag){
					sb.append("?");
					flag = false;
				}else{
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(entry.getValue());
			}
			return Request.Get(sb.toString()).execute().returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String Post(String url, String... args){
		try {
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			NameValuePair nvp;
			for (int i = 0; i < args.length; i = i + 2) {
				nvp = new BasicNameValuePair(args[i], args[i + 1]);
				param.add(nvp);
			}
			return Request.Post(url)
					.bodyForm(param, Charset.forName("utf-8")).execute()
					.returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String Post(String url, Map<String, Object> params){
		try {
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			NameValuePair nvp;
			if (params != null) {
				for (Entry<String, Object> entry : params.entrySet()) {
					Object key = entry.getKey();
					Object value = entry.getValue();
					nvp = new BasicNameValuePair(
							key.toString(), value.toString());
					param.add(nvp);
				}
			}
			return Request.Post(url)
					.bodyForm(param, Charset.forName("utf-8")).execute()
					.returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String Post(String url, String key,String []strs){
		try {
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			NameValuePair nvp;
			for (String str :strs) {
				nvp = new BasicNameValuePair(key, str);
				param.add(nvp);
			}
			return Request.Post(url)
					.bodyForm(param, Charset.forName("utf-8")).execute()
					.returnContent().asString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
