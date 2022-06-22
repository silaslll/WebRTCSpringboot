package com.yealink.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yealink.pojo.Msg;
import com.yealink.pojo.Post;
import com.yealink.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @date 2018/3/13 10:58
 */
@Controller
@RequestMapping("/post")
public class PostController {
    PostService postService;
    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/{pageNo}/{dId}/{name}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getPostPageList(@PathVariable(value = "pageNo") Integer pageNo,@ModelAttribute Post post){
        if(post.getName().equals("0")){
            post.setName(null);
        }
        PageHelper.startPage(pageNo,10);
        List<Post> posts=postService.getPostPageList(post);
        PageInfo<Post> pageInfo=new PageInfo<Post>(posts,3);
        return Msg.success().add("pageInfo",pageInfo);
    }

    @RequestMapping(value="/{dId}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getPostByDeptId(@PathVariable(value = "dId") Integer dId){
        Post post=new Post();
        post.setdId(dId);
        List<Post> posts=postService.getPostPageList(post);
        return Msg.success().add("posts",posts);
    }

    //检查某部门id下是否有被引用
    @RequestMapping("admin/checkDeptDelete")
    @ResponseBody
    public String checkDeptDelete(@RequestParam Integer id){
        Post post=new Post();
        post.setdId(id);
        List<Post> posts=postService.getPostPageList(post);
        if(posts.isEmpty())
            return "success";
        else return "error";
    }

    @RequestMapping("admin/checkBatchDeptDelete")
    @ResponseBody
    public String checkBatchDeptDelete(@RequestParam(value = "ids[]") Integer ids[]){
        Post post=new Post();
        List<Post> posts;
        for(Integer id :ids){
            post.setdId(id);
            posts=postService.getPostPageList(post);
            if(!posts.isEmpty())
                return "error";
        }
        return "success";
    }

    @RequestMapping(value="admin/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteById(@PathVariable Integer id){
        Integer result=postService.deleteById(id);
        if(result != 0) return "success";
        else return "error";
    }

    @RequestMapping("admin/checkPost")
    @ResponseBody
    public String checkPost(@ModelAttribute Post post){
        //首先检查各属性是否为空
        if(post.getName()==null || post.getName().trim().equals("")){
            return "nameNull";
        }
        if(post.getdId() == null || post.getdId() == 0){
            return "dIdNull";
        }
        //然后检查职位名称是否合格
        String regx = "^[\\u2E80-\\u9FFF]{2,10}$";
        if (!post.getName().matches(regx)) {
            return "nameFormatError";
        }
        //最后检查职位名称是否重复
        if(postService.checkPost(post))
            return "success";
        else return "repeat";
    }

    @RequestMapping(value="admin",method = RequestMethod.POST)
    @ResponseBody
    public String savePost(@ModelAttribute Post post){
        Integer result= postService.insert(post);
        if(result != 0){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping(value = "admin",method = RequestMethod.PUT)
    @ResponseBody
    public String updatePost(@ModelAttribute Post post){
        Integer result= postService.updatePost(post);
        if(result != 0){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping(value="admin",method = RequestMethod.DELETE)
    @ResponseBody
    public String batchDelete(@RequestParam(value="ids[]") int  ids[]){
        Integer result=postService.batchDeletePost(ids);
        if(result != 0){
            return "success";
        }else {
            return "error";
        }
    }
}
