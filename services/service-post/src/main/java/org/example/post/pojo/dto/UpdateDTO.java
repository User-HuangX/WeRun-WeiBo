package org.example.post.pojo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateDTO {
    private String uuid;
    private String title;
    private String content;
    private Set<String> deleteTags;
    private Set<String> Tags;
    private Date updateTime;
}
