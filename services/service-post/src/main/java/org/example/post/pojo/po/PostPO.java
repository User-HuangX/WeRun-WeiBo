package org.example.post.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class PostPO {
    private String uuid;
    private String authorId;
    private String title;
    private String content;
    private Integer schedule;
    private Date scheduleTime;
    private List<String> tags;
}
