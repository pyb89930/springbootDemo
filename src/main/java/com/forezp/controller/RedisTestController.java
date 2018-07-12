package com.forezp.controller;

import com.forezp.model.Result;
import com.forezp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试访问redis 的 controller
 */
@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    RedisService redisService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Result test() {
        Result result = new Result();
        try {
            Object obj = redisService.getHashValue("wk_thirdpart_shop_list","shop1585559363824934");
            result.setData(obj);
        } catch (Exception e) {
            result.setCode("-1");
            result.setMessage("操作失败");
            e.printStackTrace();
        } finally {
        }
        return result;
    }


}