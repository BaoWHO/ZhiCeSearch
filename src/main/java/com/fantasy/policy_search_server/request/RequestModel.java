package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestModel {
    @NotBlank(message = "grade不能为空")
    String grade;
    @NotBlank(message = "province不能为空")
    String province;
    @NotBlank(message = "city不能为空")
    String city;
    @NotBlank(message = "county不能为空")
    String county;
    @NotBlank(message = "agency不能为空")
    String agency;
    @NotBlank(message = "type不能为空")
    String type;

    String keyword;

    //高级搜索
    @NotBlank(message = "sort_by不能为空")
    String sort_by;
    @NotBlank(message = "time_from不能为空")
    String time_from;
    @NotBlank(message = "time_to不能为空")
    String time_to;
    @NotNull(message = "page_no不能为空")
    Integer page_no;
    @NotNull(message = "page_size不能为空")
    Integer page_size;

    //@NotBlank(message = "token不能为空")
    String token;

    @Override
    public String toString() {
        return "grade='" + grade + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", agency='" + agency + '\'' +
                ", type='" + type + '\'' +
                ", keyword='" + keyword + '\'' +
                ", sort_by='" + sort_by + '\'' +
                ", time_from='" + time_from + '\'' +
                ", time_to='" + time_to + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                ", token='" + token + '\'';
    }
}
