package org.example.post.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CTCPO {
    private String commentId;
    private String postedCommentId;
    private String postId;
    private String userId;
    private String content;
}
