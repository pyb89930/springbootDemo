package com.forezp.controller;

import com.forezp.model.Result;
import com.forezp.model.SmsRecord;
import com.forezp.tcp.TcpNetty;
import com.forezp.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 短信发送
 *
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    TcpNetty tcpNetty;

    /**
     * 发送短信
     * @param sendtype
     * @param mobile
     * @param content
     * @return
     */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public Result sendCmd(String sendtype,String mobile,String content) {
        Result result = new Result();

        try {
            content = URLDecoder.decode(content,"UTF-8");
            content =  URLEncoder.encode(content,"GBK");
        } catch (Exception e) {
            logger.error("",e);
        }
        String url = "http://121.41.77.158:8888/MobileSendSave.asp?sendtype="+sendtype+"&mobile="+mobile+"&content="+content;
        HttpUtil.Get(url);
        result.setCode("1");
        return result;
    }

    /**
     * 查询短信记录
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result search(String mobile) {
        Result result = new Result();
        try {
            String url = "http://121.41.77.158:8888/MobileSendRecord.asp?mobile="+mobile;
            String dataJson = HttpUtil.Get(url);
            Map<String,Object> jsonMap = JsonToMapUtil.getMapFromJsonObjStr(dataJson);
            List<SmsRecord> list = new ArrayList<SmsRecord>();
            if(jsonMap.get("data1") != null ){
                JSONArray data1Arr = (JSONArray) jsonMap.get("data1");
                if(!data1Arr.toString().equals("")){
                    for(int tempIndex = 0;tempIndex < data1Arr.size();tempIndex ++){
                        if(!data1Arr.get(tempIndex).toString().equals("")){
                            JSONObject curObj = (JSONObject) data1Arr.get(tempIndex);
                            SmsRecord smsModel = new SmsRecord();
                            smsModel.setContent(curObj.get("content") != null ?curObj.get("content").toString():"");
                            smsModel.setContent(URLDecoder.decode(smsModel.getContent(),"GBK"));
                            smsModel.setMtDate(curObj.get("mt_date") != null ?curObj.get("mt_date").toString():"");
                            smsModel.setTerminalId(curObj.get("terminal_id") != null ?curObj.get("terminal_id").toString():"");
                            smsModel.setSender("self");
                            list.add(smsModel);
                        }
                    }
                }
            }
            if(jsonMap.get("data2") != null ){
                JSONArray data1Arr = (JSONArray) jsonMap.get("data2");
                if(!data1Arr.toString().equals("")){
                    for(int tempIndex = 0;tempIndex < data1Arr.size();tempIndex ++){
                        if(!data1Arr.get(tempIndex).toString().equals("")){
                            JSONObject curObj = (JSONObject) data1Arr.get(tempIndex);
                            SmsRecord smsModel = new SmsRecord();
                            smsModel.setContent(curObj.get("content") != null ?curObj.get("content").toString():"");
                            smsModel.setContent(URLDecoder.decode(smsModel.getContent(),"GBK"));
                            smsModel.setMtDate(curObj.get("mt_date") != null ?curObj.get("mt_date").toString():"");
                            smsModel.setTerminalId(curObj.get("terminal_id") != null ?curObj.get("terminal_id").toString():"");
                            smsModel.setSender("other");
                            list.add(smsModel);
                        }
                    }
                }
            }
            Collections.sort(list, new Comparator<SmsRecord>() {
                public int compare(SmsRecord a, SmsRecord b) {

                    return a.getMtDate().compareTo(b.getMtDate());
                }
            });

            result.setCode("1");
            result.setData(list);
        } catch(Exception e){
            logger.error("", e);
        }


        return result;
    }

    public static void main(String[] args) {

    }



}