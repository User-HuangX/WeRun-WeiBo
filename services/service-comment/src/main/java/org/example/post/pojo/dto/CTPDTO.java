package org.example.post.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CTPDTO{
    @NotNull(message = "正确指定所发的帖子")
    private String postId;
    @NotNull(message = "所发内容不可以为空")
    private String content;
}
