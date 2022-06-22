package com.yealink.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yealink.pojo.Msg;
import com.yealink.pojo.Temp;
import com.yealink.pojo.VCS;
import com.yealink.service.TempService;
import com.yealink.service.VCSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @date 2018/3/16 16:09
 */
@Controller
@RequestMapping("/vcs")
public class VCSController {
    private VCSService vcsService;
    @Autowired
    public void setVcsService(VCSService vcsService) {
        this.vcsService = vcsService;
    }

    private TempService tempService;

    @Autowired
    public void setTempService(TempService tempService) {
        this.tempService = tempService;
    }

    @RequestMapping("pageList")
    @ResponseBody
    public Msg getPageList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,@ModelAttribute VCS vcs){
        PageHelper.startPage(pageNo,10);
        List<VCS> vcss=vcsService.getPageList(vcs);
        PageInfo<VCS> pageInfo=new PageInfo<VCS>(vcss,3);
        return Msg.success().add("pageInfo",pageInfo);
    }

    @RequestMapping(value="",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveVCS(@RequestBody VCS vcs){
        System.out.println(new Date()+":一场新的会议开始");
        Integer maxId=vcsService.selectMaxId() + 1;
        vcs.setVcsId(maxId);
        vcs.setVcsState(1);
        System.out.println(vcs);
        for(Temp temp:vcs.getInvited()){
            temp.setVcsId(maxId);
        }
        vcs.getInvited().add(new Temp(maxId,vcs.getCreater()));
        vcsService.saveVCS(vcs);
        tempService.saveTempList(vcs.getInvited());
        return Msg.success().add("vcs_Id",maxId);
    }

    @RequestMapping(value="",method=RequestMethod.PUT)
    @ResponseBody
    public String closeVCS(@RequestParam String username,Integer vcsId){
        //先检查是否是创建者
        String creater=vcsService.getCreater(vcsId);
        if(creater.trim().equals(username)){
            //关闭
            if(vcsService.closeVCS(vcsId)) {
                System.out.println(new Date()+":一场会议结束 会议ID："+vcsId);
                return "success";
            }
            else
                return "error";
        }
        else
            return "error";
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getTemps(@PathVariable Integer id){
        return Msg.success().add("temps",vcsService.getTemps(id));
    }

    @RequestMapping(value="admin/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteVcs(@PathVariable Integer id, HttpServletRequest request){
        if(vcsService.checkVcsState(id)){
            //可以删除
            vcsService.deleteVcs(id);
            String globalDir=request.getSession().getServletContext().getRealPath("/");
            String vcsDir=globalDir + "file/" + id;
            deleteDir(vcsDir);
            return "success";
        }else
            return "error";
    }

    @RequestMapping("admin/checkBatchVCSDelete")
    @ResponseBody
    public String checkBatchVCSDelete(@RequestParam(value = "ids[]") int ids[]){
        for(int id:ids){
            if(!vcsService.checkVcsState(id)){
                return "error";
            }
        }
        return "success";
    }


    @RequestMapping(value = "admin",method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBatch(@RequestParam(value = "ids[]") int ids[],HttpServletRequest request){
        Integer result=vcsService.deleteBatch(ids);
        if(result != 0) {
            String globalDir=request.getSession().getServletContext().getRealPath("/");
            for(int id:ids){
                String vcsDir=globalDir + "file/" + id;
                deleteDir(vcsDir);
            }
            return "success";
        }
        else{
            return "error";
        }
    }

    //删除文件夹
    public void deleteDir(String folderPath){
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delAllFile(String path){
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }

        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            //构建好要删除的文件路劲
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                deleteDir(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

}

