package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDBModel {
    private Long id;
    private String title;
    private String grade;
    private String pubNumber;
    private Date pubTime;
    private String type;
    private String province;
    private String city;
    private String source;
    private String county;
    private Date createAt;
    private Date updatedAt;
    private Integer view;
    private Integer like;
    private Integer collect;
}