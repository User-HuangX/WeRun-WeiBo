package org.example.auth.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterPO {
    private String Email;
    private String Username;
    private String Phone;
    private String Password;
    private String Gender;
    private String AvatarURL;
    private String uuid;
    public String getEmail(){
        return Email;
    }
}
