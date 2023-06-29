package com.fantasy.policy_search_server.model;

public class DistrictModel {
    private Integer id;

    private String name;

    private String grade;

    private Integer pid;

    public DistrictModel(Integer id, String name, String grade, Integer pid) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.pid = pid;
    }

    public DistrictModel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}