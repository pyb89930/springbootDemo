package com.forezp.dao;

import com.forezp.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试 mapper + XML 进行数据库查询
 */
@Mapper
public interface TestMapper2 {
   int update(@Param("money") double money, @Param("id") int id);

   List<Test> findAccountList();
}
