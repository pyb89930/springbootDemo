package com.forezp.service;

import com.forezp.dao.TestMapper;
import com.forezp.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试服务类
 */
@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public int add(String name, double money) {
        return testMapper.add(name, money);
    }
    public int update(String name, double money, int id) {
        return testMapper.update(name, money, id);
    }
    public int delete(int id) {
        return testMapper.delete(id);
    }
    public Test findAccount(int id) {
        return testMapper.findAccount(id);
    }
    public List<Test> findAccountList() {
        return testMapper.findAccountList();
    }

}