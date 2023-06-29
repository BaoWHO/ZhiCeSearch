package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgencyFilterReq {
    @NotBlank(message = "grade不能为空")
    private String grade;
    private String province;
    private String city;
    private String county;

    @Override
    public String toString() {
        return "grade='" + grade + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'';
    }
}
