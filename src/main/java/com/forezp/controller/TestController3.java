package com.forezp.controller;

import com.forezp.entity.Test;
import com.forezp.service.TestService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 测试controller3
 *  测试 springMVC 访问JSP
 */
@Controller
@RequestMapping("/jsptest")
public class TestController3 {

    @RequestMapping(value = "/test")
    public ModelAndView gotoPage(){
        ModelAndView view = new ModelAndView("test");
        view.addObject("visitName","张三");
        System.out.println("page");
        return view;
    }

}
