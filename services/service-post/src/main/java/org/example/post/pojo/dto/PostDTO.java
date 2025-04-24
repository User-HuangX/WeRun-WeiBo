package org.example.post.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String content;
    private Integer schedule;
    private String scheduleTime;
    private List<String> tags;
}
