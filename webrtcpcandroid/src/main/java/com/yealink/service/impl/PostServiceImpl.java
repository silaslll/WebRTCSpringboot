package com.yealink.service.impl;

import com.yealink.dao.PostDao;
import com.yealink.pojo.Post;
import com.yealink.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2018/3/13 10:57
 */
@Service
public class PostServiceImpl implements PostService {
    PostDao postDao;
    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getPostPageList(Post post){
        return postDao.selectAll(post);
    }

    public Integer deleteById(Integer id){
        return postDao.deleteById(id);
    }
    public boolean checkPost(Post post){
        return postDao.checkPost(post) == null;
    }
    public Integer insert(Post post){
        return postDao.insert(post);
    }
    public Integer updatePost(Post post){
        return postDao.updatePost(post);
    }
    public Integer batchDeletePost(int [] ids){
        return postDao.batchDeletePost(ids);
    }
}
