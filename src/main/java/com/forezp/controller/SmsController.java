package com.forezp.controller;

import com.forezp.model.Result;
import com.forezp.model.SmsRecord;
import com.forezp.tcp.TcpNetty;
import com.forezp.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    @CrossOrigin
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public Result sendCmd(String sendtype,String mobile,String iccid,String content) {
        Result result = new Result();

        try {
            content = URLDecoder.decode(content,"UTF-8");
            content =  URLEncoder.encode(content,"GBK");
        } catch (Exception e) {
            logger.error("",e);
        }
        if(StringUtils.isBlank(mobile) && StringUtils.isNotBlank(iccid)){
            String url = "http://www.2016gps.cn/servlet/sim_info1?phone_number="+iccid;
            String rsContent = HttpUtil.Get(url);
            JSONObject jsonObject=JSONObject.fromObject(rsContent);
            if(jsonObject.containsKey("status") && jsonObject.getInt("status") == 0){
                JSONObject dataJson = jsonObject.getJSONObject("data");
                mobile = dataJson.getString("phone_number");
            }
        }
        String url = "http://121.41.77.158:8888/MobileSendSave.asp?sendtype="+sendtype+"&mobile="+mobile+"&content="+content;
        String msg = HttpUtil.Get(url);
        result.setCode("1");
        result.setMessage(mobile);
        return result;
    }

    /**
     * 通过ICCID获取手机信息
     * @param iccid
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getPhone", method = RequestMethod.GET)
    public Result getPhone(String iccid) {
        Result result = new Result();


        String url = "http://www.2016gps.cn/servlet/sim_info1?phone_number="+iccid;
        String rsContent = HttpUtil.Get(url);
        result.setCode("1");
        result.setData(rsContent);
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