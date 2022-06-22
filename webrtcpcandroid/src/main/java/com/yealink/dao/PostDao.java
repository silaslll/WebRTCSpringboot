package com.yealink.dao;

import com.yealink.pojo.Post;

import java.util.List;

/**
 *
 * @date 2018/3/9 17:24
 */
public interface PostDao {
    public Post selectById(Integer id);
    public List<Post> selectAll(Post post);
    public Integer deleteById(Integer id);
    public Integer insert(Post post);
    public Post checkPost(Post post);
    public Integer updatePost(Post post);
    public Integer batchDeletePost(int[] ids);
}
