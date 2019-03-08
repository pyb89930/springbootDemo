package com.forezp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonToMapUtil{
	
	public static Map getMapFromJsonObjStr(String jsonObjStr) {
		JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);
		Map map = new HashMap();
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}
	
	public static List<Map> getMapFromStrToMap(String jsonObjStr){
		 JSONArray jsonArray = JSONArray.fromObject(jsonObjStr);  
		 List<Map> mapList = new ArrayList<Map>();
         List<Map<String,Object>> mapListJson = (List)jsonArray;  
         for (int i = 0; i < mapListJson.size(); i++) {  
             Map<String,Object> obj=mapListJson.get(i);  
             Map map = new HashMap();
             for(Entry<String,Object> entry : obj.entrySet()){  
                 String strkey = entry.getKey();  
                 Object strval = entry.getValue(); 
                 map.put(strkey, strval);
             }  
             mapList.add(map);
         }
		 return mapList;  
	}
}