package com.forezp.entity;

/**
 * 测试实体类
 * springboot 核心配置文件加入  mybatis.configuration.map-underscore-to-camel-case=true
 * 开启驼峰命名，例如数据库字段project_code可以直接映射实体属性projectCode

 */
public class Test {
    private int id ;
    private String name ;
    private double money;
    private String projectCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
