package org.example.post.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private int total; // 总记录数
    private List<T> list; // 当前页的数据
    private int page; // 当前页码
    private int pageSize; // 每页大小

}