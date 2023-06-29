package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSearchReq {
    @NotBlank(message = "keyword不能为空")
    String keyword;
    @NotNull(message = "page_no不能为空")
    Integer page_no;
    @NotNull(message = "page_size不能为空")
    Integer page_size;
    //@NotBlank(message = "token不能为空")
    private String token;

    @Override
    public String toString() {
        return "keyword='" + keyword + '\'' +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                ", token='" + token + '\'';
    }
}
