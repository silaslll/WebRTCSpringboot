package com.yealink.service;

import com.yealink.pojo.Post;

import java.util.List;

/**
 *
 * @date 2018/3/13 10:56
 */
public interface PostService {
    public List<Post> getPostPageList(Post post);
    public Integer deleteById(Integer id);
    public boolean checkPost(Post post);
    public Integer insert(Post post);
    public Integer updatePost(Post post);
    public Integer batchDeletePost(int[] ids);
}
