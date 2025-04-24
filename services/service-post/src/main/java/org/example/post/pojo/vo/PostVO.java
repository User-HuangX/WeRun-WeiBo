package org.example.post.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
@Data
@AllArgsConstructor

public class PostVO {
    String uuid;
    String authorId;
    String title;
    String content;
    Date createTime;
    Date updateTime;
    Integer commentCount;
}
