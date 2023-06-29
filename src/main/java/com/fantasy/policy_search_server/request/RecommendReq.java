package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendReq {
    @NotNull(message = "size不能为空")
    int size;
    //@NotBlank(message = "token不能为空")
    String token;

    @Override
    public String toString() {
        return "size=" + size +
                ", token='" + token + '\'';
    }
}
