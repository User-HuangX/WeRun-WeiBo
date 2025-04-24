package org.example.auth.pojo.bo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CheckEmailBO {
    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "请填入正确的邮箱的格式")
    private String Email;
    @NotNull(message = "验证码不能为空")
    @Pattern(regexp = "^[0-9]{6}$",message = "请填入6位数字验证码")
    private String Code;
}
