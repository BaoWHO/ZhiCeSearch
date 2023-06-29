package com.fantasy.policy_search_server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyModel {
    private String id;
    private String title;
    private String no_html_title;
    private String grade;
    private String agency_id;
    private String agency;
    private String agency_full_name;
    private String pub_number;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
    private Date pub_time;
    private String type;
    private String body;
    private String province;
    private String city;
    private String county;
    private String source;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
    private Date update_date;
    private int view;
    private int like;
    private int collect;
}
