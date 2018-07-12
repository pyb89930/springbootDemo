package com.forezp.controller;

import com.forezp.model.Result;
import com.forezp.service.RabbitMQService;
import com.forezp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试访问RabbitMQ 的 controller
 */
@RestController
@RequestMapping("/rabbitMQ")
public class RabbitMQTestController {

    @Autowired
    RabbitMQService rabbitMQService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        Result result = new Result();
        try {
            rabbitMQService.send("e_obd_wifi_mac", "r_obd_wifi_mac", "{123}");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("-1");
            result.setMessage("操作失败");
        }
        return result;
    }


}