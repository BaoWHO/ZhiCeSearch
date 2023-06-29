package com.fantasy.policy_search_server.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReq {
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "性别不能为空")
    private String gender;

    @NotBlank(message = "职业不能为空")
    private String career;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotNull(message ="经度不能为空")
    private BigDecimal longitude;

    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Override
    public String toString() {
        return "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", career='" + career + '\'' +
                ", email='" + email + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude;
    }
}
