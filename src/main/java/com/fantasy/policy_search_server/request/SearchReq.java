package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReq {
    @NotBlank(message = "搜索词不能为空")
    private String keyword;

    private String grade;

    private String province;

    private String city;

    private String agency;
}
