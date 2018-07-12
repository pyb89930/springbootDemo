package com.forezp.dao;

import com.forezp.entity.Test;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 测试注解方式查询数据库
 */
@Mapper
public interface TestMapper {

    @Insert("insert into test(name, money) values(#{name}, #{money})")
    int add(@Param("name") String name, @Param("money") double money);

    @Update("update test set name = #{name}, money = #{money} where id = #{id}")
    int update(@Param("name") String name, @Param("money") double money, @Param("id") int id);

    @Delete("delete from test where id = #{id}")
    int delete(int id);

    @Select("select id, name as name, money as money from test where id = #{id}")
    Test findAccount(@Param("id") int id);

    @Select("select * from test")
    List<Test> findAccountList();
}