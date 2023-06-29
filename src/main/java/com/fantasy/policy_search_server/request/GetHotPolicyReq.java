package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetHotPolicyReq {
    @NotBlank(message = "id不能为空")
    String id;
    @NotNull(message = "r不能为空")
    int r;
    //@NotBlank(message = "token不能为空")
    String token;

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", r=" + r +
                ", token='" + token + '\'';
    }
}
