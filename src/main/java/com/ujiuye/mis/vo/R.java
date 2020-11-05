package com.ujiuye.mis.vo;

import java.util.HashMap;
import java.util.Map;

//统一结果集
public class R {

    //状态
    private Boolean status;
    //提示信息
    private String msg;
    private Map<String,Object> map=new HashMap<>();

    //提供一些静态的方法
    public static R ok(){
        //返回状态
        return new R(){{
            setStatus(true);
        }};
    }
    //失败
    public static R error(){
        return new R(){{
            setStatus(false);
        }};
    }
    //编辑提示信息的方法
    public R msg(String m){
        this.msg = m;
        return  this;
    }

    //为该实力添加数据
    public R data(Map<String,Object> map){
        this.map = map;
        return this;
    }

    //重载
    public R data(String key,Object o){
        this.map.put(key,o);
        return this;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
