package com.yealink.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @date 2018/3/22 9:16
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value="upload", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    @ResponseBody
    public Map<String,String> upload(@RequestParam MultipartFile userUploadFile, Integer vcsId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Map<String,String> result=new HashMap<String,String>();
        if(userUploadFile!=null) {
            if (!userUploadFile.isEmpty()) {
                String fileName = userUploadFile.getOriginalFilename();
                String globalDir = request.getSession().getServletContext().getRealPath("/");
                File write = new File(globalDir + "file/" + vcsId, fileName);
                if (write.getParentFile().exists() == false) {
                        write.getParentFile().mkdirs();
                }
                write.createNewFile();
                try {
                userUploadFile.transferTo(write);
                } catch (Exception e) {
                    result.put("state", "2");
                    e.printStackTrace();
                }

                /*
                InputStream ins = userUploadFile.getInputStream();
                OutputStream ous = new FileOutputStream(write);
                try {
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = ins.read(buffer)) > -1)
                        ous.write(buffer, 0, len);
                } catch (Exception e) {
                    result.put("state", "2");
                    e.printStackTrace();
                } finally {
                    ous.close();
                    ins.close();
                }
                */
                //记得把文件的存放路径回传，以便其它用户取用
                result.put("filename",fileName);
                result.put("fileurl",write.getAbsolutePath());
                result.put("state", "1");
            } else {
                result.put("state", "0");
            }
        }else {
            result.put("state", "0");
        }
        return  result;
    }

    @RequestMapping(value="download")
    public void downFile(@RequestParam String fileUrl, HttpServletRequest request, HttpServletResponse response){
        File file=new File(fileUrl);
        String fileName=file.getName();
       // response.setContentType("text/html;charset=utf-8");
        if (!file.exists()) {

        }else{
            response.setContentType("multipart/form-data");
            String fileNameNet= null;
            try {
                fileNameNet = new String(fileName.getBytes("UTF-8"),"iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.addHeader("Content-disposition", "attachment; filename=" + fileNameNet);
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = null;
            OutputStream out=null;
            try {
                in = new FileInputStream(file);
                // 创建输出流
                out = response.getOutputStream();
                // 创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                // 循环将输入流中的内容读取到缓冲区中
                while ((len = in.read(buffer)) > 0) {
                    // 输出缓冲区内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(in!=null)
                        in.close();
                    if(out!=null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
