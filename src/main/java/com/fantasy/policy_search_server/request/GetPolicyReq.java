package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPolicyReq {
    @NotBlank(message = "id不能为空")
    String id;
    //@NotBlank(message = "token不能为空")
    String token;

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", token='" + token + '\'';
    }
}
