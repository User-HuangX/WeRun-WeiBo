package org.example.post.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class PostBO {
    private String uuid;
    private String authorId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer commentCount;
    private Integer schedule;
    private Date scheduleTime;
}
