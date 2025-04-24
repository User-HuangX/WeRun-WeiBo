package org.example.post.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CTPPO {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
}
