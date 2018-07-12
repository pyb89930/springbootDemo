package com.forezp.service;

import com.forezp.dao.TestMapper2;
import com.forezp.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试服务类2 （事务）
 */
@Service
public class TestService2 {

    @Autowired
    TestMapper2 testMapper2;

    @Transactional
    public void transfer() throws RuntimeException{
        testMapper2.update(90,1);//用户1减10块 用户2加10块
        int i=1/0;
        testMapper2.update(110,2);
    }

    @Transactional
    public List<Test> findAccountList() {
        return testMapper2.findAccountList();
    }
}
