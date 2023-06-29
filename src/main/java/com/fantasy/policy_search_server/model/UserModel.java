package com.fantasy.policy_search_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String phone;
    private String password;
    private String nickName;
    private String gender;
    private String email;
    private String career;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Date createAt;
    private Date updatedAt;
}