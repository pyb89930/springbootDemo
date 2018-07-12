package com.forezp.controller;

import com.forezp.entity.Test;
import com.forezp.model.Result;
import com.forezp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试controller
 * 测试注解SQL的方式操作数据库
 */
@RestController
@RequestMapping("/dbtest")
public class TestController {

    @Autowired
    TestService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAccounts() {
        Result result = new Result();
        List<Test> list =  accountService.findAccountList();
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result getAccountById(@PathVariable("id") int id) {
        Result result = new Result();
        try {
            accountService.findAccount(id);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData("-1");
            result.setMessage(e.getMessage());
        } finally {

        }
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateAccount(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                                @RequestParam(value = "money", required = true) double money) {
        int t= accountService.update(name,money,id);
        if(t==1) {
            return "success";
        }else {
            return "fail";
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id")int id) {
        int t= accountService.delete(id);
        if(t==1) {
            return "success";
        }else {
            return "fail";
        }

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postAccount(@RequestParam(value = "name") String name,
                              @RequestParam(value = "money") double money) {

       int t= accountService.add(name,money);
       if(t==1) {
           return "success";
       }else {
           return "fail";
       }

    }




}