package com.ujiuye.mis.qo;

public class EmployeeQuery {

    private Integer condition; //查询的条件判断
    private String keywords; //关键词

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
