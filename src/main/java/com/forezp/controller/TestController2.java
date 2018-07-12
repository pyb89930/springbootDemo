package com.forezp.controller;

import com.forezp.entity.Test;
import com.forezp.model.Result;
import com.forezp.service.TestService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试controller2
 *  测试事务 和 XML方式的SQL操作
 */
@RestController
@RequestMapping("/dbtest2")
public class TestController2 {
    @Autowired
    TestService2 accountService;

    @RequestMapping(value = "transfer", method = RequestMethod.GET)
    public Result transfer(){
        Result result = new Result();
        try {
            accountService.transfer();
        } catch (Exception e) {
            result.setCode("-1");
            result.setMessage("操作失败");
            e.printStackTrace();
        } finally {
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAccounts() {
        Result result = new Result();
        List<Test> list =  accountService.findAccountList();
        result.setData(list);
        return result;
    }
}
