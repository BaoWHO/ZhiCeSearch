package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictReq {
    @NotBlank(message = "province不能为空")
    private String province;
    @NotBlank(message = "city不能为空")
    private String city;
    @NotBlank(message = "county不能为空")
    private String county;

    @Override
    public String toString() {
        return "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'';
    }
}
