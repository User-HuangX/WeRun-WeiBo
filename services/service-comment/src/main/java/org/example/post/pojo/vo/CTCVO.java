package org.example.post.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor

public class CTCVO {
    private String commentId;
    private String postedCommentId;
    private String userId;
    private String content;
    private int isLiked;
    private Date createTime;
    private String replyId;
}
