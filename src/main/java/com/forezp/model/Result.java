package com.forezp.model;

/**
 * controller 普通JSON数据返回通用对象模型
 */
public class Result {
    protected static final String SUCCESS = "1";
    protected static final String ERROR = "-1";

    protected String code;
    protected String message;
    protected Object data;

    public Result(){
        this.code = "1";
        this.message = "操作成功";
    }

    public Result success(String message, Object data){
        this.code = SUCCESS;
        this.message = message;
        this.data = data;
        return this;
    }

    public Result error(String message, Object data){
        this.code = ERROR;
        this.message = message;
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data == null?"":data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
