package com.ujiuye.mis.eum;

public enum EmployeeConditionQuery {
    EMPLOYEE_ENAME_QUERY(1,"根据员工的姓名模糊查询"),
    EMPLOYEE_DNAME_QUERY(2,"根据部门名称模糊查询");

    private Integer condition;
    private String keywords;

    public Integer getCondition() {
        return condition;
    }

    public String getKeywords() {
        return keywords;
    }

    EmployeeConditionQuery(Integer condition, String keywords) {
        this.condition = condition;
        this.keywords = keywords;
    }
}
