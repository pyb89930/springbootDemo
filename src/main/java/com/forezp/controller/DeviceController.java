package com.forezp.controller;

import com.forezp.entity.Test;
import com.forezp.model.Result;
import com.forezp.service.RabbitMQService;
import com.forezp.service.TestService;
import com.forezp.tcp.TcpNetty;
import com.forezp.utils.DateUtil;
import com.forezp.utils.EncodeUtil;
import com.forezp.utils.HexUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GPS设备操作
 *
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    TcpNetty tcpNetty;

    /**
     * 发送断电、恢复指令
     * @param deviceId 设备ID号
     * @param cmdId   指令ID  0：恢复断电   1：断电
     * @return
     */
    @RequestMapping(value = "/sendCmd", method = RequestMethod.GET)
    public Result sendCmd(String deviceId,int cmdId,int time) {
        Result result = new Result();
        String hxDeviceId = HexUtils.toHexString(deviceId.getBytes()).toString();
        String hxhhmmss = HexUtils.toHexString(DateUtil.getCurDateStr(DateUtil.DEFAULT_SHORT_DATE_FORMAT).getBytes()).toString();
        String hxTime = HexUtils.toHexString((time+"").getBytes()).toString();
        String cmdContent = null;
        if(cmdId == 0){//恢复
            cmdContent = "16 21 44 42 34 00 "+hxDeviceId+" 00 53 32 30 2C "+hxhhmmss+" 2C  31 2C 30 00 E6 B2 B9 E7 94 B5 E6 93 8D E4 BD 9C  18 26 ";
        }else if(cmdId == 1){//断电
            cmdContent = "16 21 44 42 34 00 "+hxDeviceId+" 00 53 32 30 2C "+hxhhmmss+" 2C  31 2C "+hxTime+" 00 E6 B2 B9 E7 94 B5  E6 93 8D E4 BD 9C 18 26 ";
        }else{
            return new Result().error("指令ID传入有误","");
        }
        cmdContent = cmdContent.replaceAll(" ","");
        result.setData(cmdContent);
        int rspCode = tcpNetty.sendCmd(cmdContent);
        result.setCode(rspCode+"");
        return result;
    }

    /**
     * 发送通用指令
     * @param deviceId 设备ID号
     * @param cmd   指令内容
     * @param cmdName   指令名称
     * @return
     */
    @RequestMapping(value = "/sendCommonCmd", method = RequestMethod.GET)
    public Result sendCommonCmd(String deviceId,String cmd,String cmdName) {
        Result result = new Result();
        String hxDeviceId = HexUtils.toHexString(deviceId.getBytes()).toString();
        String hexCmd = HexUtils.toHexString(cmd.getBytes()).toString();
        String hexCmdName = HexUtils.toHexString(cmdName.getBytes()).toString();
        String cmdContent = null;
        if(StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(cmd) && StringUtils.isNotBlank(cmdName)){//恢复
            cmdContent = "16 21 44 42 34 00 "+hxDeviceId+" 00 "+hexCmd+" 00 "+hexCmdName+" 18 26 ";
        }else{
            return new Result().error("指令信息传入有误","");
        }
        cmdContent = cmdContent.replaceAll(" ","");
        logger.info("发送通用指令："+cmdContent);
        result.setData(cmdContent);
        int rspCode = tcpNetty.sendCmd(cmdContent);
        result.setCode(rspCode+"");
        return result;
    }

    public static void main(String[] args) {
        String cmd = "16 21 44 42 34 00 31 32  36 35 37 39 00 53 32 30 2C 31 36 32 31 31 36 2C  31 2C 35 2C 35 2C 35 2C 35 00 E6 B2 B9 E7 94 B5  E6 93 8D E4 BD 9C 18 26";
        cmd = cmd.replaceAll(" ","");
        String hxhhmmss = DateUtil.getCurDateStr(DateUtil.DEFAULT_SHORT_DATE_FORMAT);
        System.out.println(new DeviceController().sendCommonCmd("2503584429","S17,"+hxhhmmss+",2,1","设置自动回传"));
    }



}