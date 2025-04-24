package org.example.common.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserBO {

    private String uuid;
    private String Username;
    private String Phone;
    private String Email;
    private String Password;
    private int Status;
    private String Signature;
    private String AvatarURL;
    private String Address;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String Gender;
    private Date Birthday;

}
