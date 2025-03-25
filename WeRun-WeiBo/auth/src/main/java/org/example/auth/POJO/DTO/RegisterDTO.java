package org.example.auth.POJO.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDTO {
    //这是手机号，必须填写且符合格式
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "请填入正确的手机号的格式")
    private String userPhone;
    //这是密码，在注册端已经符合格式，不需要检验格式，必填
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\da-zA-Z\\s]).{1,9}$",message = "至少包含字母、数字、特殊字符，1-9位")
    private String userPassword;
}
