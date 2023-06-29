package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutReq {
    @NotBlank(message = "token不能为空")
    private String token;

    @Override
    public String toString() {
        return "token='" + token + '\'';
    }
}
