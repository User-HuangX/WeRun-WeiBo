package org.example.auth.pojo.po;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPO {
    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "请填入正确的邮箱的格式")
    private String Email;
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "请填入正确的手机号的格式")
    private String Phone;
    @NotNull(message = "新密码不能为空")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[^\\da-zA-Z\\s]).{1,20}$",message = "至少包含字母、数字、特殊字符，1-20位")
    private String Password;

}
