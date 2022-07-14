package com.example.demo.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.bean.Duqu;
import com.example.demo.bean.Student;
import com.example.demo.service.Userservice;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("/Index/")
public class IndexController {
	
	@Value("${web.upload-path}")
	private String uploadpath;            //自定义路径
	
	@Autowired
	private Userservice userservice;
	
	@RequestMapping("index")
	public String index() {
		return "Index/login";
	}
	@RequestMapping("show")
	public String show() {
		return "Index/show";
	}
	@RequestMapping("live")
	public String live() {
		return "Index/live";
	}
	
	@RequestMapping("get_main")
	public String get_main() {
		return "Index/main";
	}
	
	@RequestMapping("zhuce")
	public String zhuce() {
		return "Index/zhuce";
	}
	
	@RequestMapping("user")
	public String user() {
		return "Index/user";
	}
	
	@RequestMapping("upshipin")
	public String upshipin() {
		return "Index/upshipin";
	}
	
	@RequestMapping("viewshipin")
	public String viewshipin() {
		return "Index/viewshipin";
	}
	
	@RequestMapping("kanbo")
	public String kanbo() {
		return "Index/kanbo";
	}
	
	@RequestMapping("new_user")
	public String new_user() {
		return "Index/new_user";
	}
	
	
	@RequestMapping("fangjian")
	public String fangjian() {
		return "Index/fangjian";
	}
	
	
	@RequestMapping("new_fangjian")
	public String new_fangjian() {
		return "Index/new_fangjian";
	}
	
	
	@RequestMapping("zhibo")
	public String zhibo(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		String oid = request.getParameter("oid");
		String uid = session.getId();
		
		request.setAttribute("initiator", "false");
		
		if(!AcgistVideo.canCreate()) {
			try {
				response.getWriter().write("不能创建通话房间，超过最大创建数量！");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
//		if(!AcgistVideo.canJoin(oid)) {
//			try {
//				response.getWriter().write("对不起对方正在通话中，你不能加入！");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.println("33");
//			return null;
//		}
		
		if(AcgistVideo.addUser(uid, oid)) {
			request.setAttribute("uid", uid);
		} else {
			request.setAttribute("initiator", "true");
			
			request.setAttribute("uid", uid);
			request.setAttribute("oid", oid);
		}
		
		return "Index/zhibo";
	}
	
	@RequestMapping("get_user")
	@ResponseBody
	public Map<String,Object> get_user(HttpServletRequest req) {
		Map<String,Object> map = new HashMap<String,Object>();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		Student stu  = userservice.get_user(username,password);
		if(stu!=null) {
			map.put("status", "1");
			map.put("info", "登陆成功");
			req.getSession().setAttribute("uid",stu.getId());
			req.getSession().setAttribute("username",stu.getUsername());
			req.getSession().setAttribute("leixing",stu.getLeixing());
		}else {
			map.put("status", "0");
			map.put("info", "用户名或者密码错误");
		}
		
		return map;
	}
	
	
	@RequestMapping("zhuce_user")
	@ResponseBody
	public Map<String,Object> zhuce_user(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String leixing  = request.getParameter("leixing");
		
		
		
		Student stu  = userservice.get_user_username(username);
		if(stu!=null) {
			map.put("status", 2);
			map.put("info", "用户名已被注册");
		}else {
			int res =  userservice.zhuce(username,password,leixing);
			if(res!=0) {
				map.put("status", 1);
				map.put("info", "注册成功");
			}else {
				map.put("status", 2);
				map.put("info", "注册失败");
			}
		}
		
		return map;
	}
	
	@RequestMapping("up_shipin")
	@ResponseBody
	public Map<String,Object> up_shipin(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
		String name = request.getParameter("name");
		String username = request.getSession().getAttribute("username").toString();
		String fileName = UUID.randomUUID()+ file.getOriginalFilename();
		String path = uploadpath;
		File file1 = new File(path+fileName);
		if(!file1.getParentFile().exists()) {
			file1.getParentFile().mkdirs();
		}
		
		if(!file.isEmpty()){
			byte[] bytes;
			try {
				bytes = file.getBytes();
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(path+fileName)));
				bufferedOutputStream.write(bytes);
				bufferedOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		int res = userservice.up_shipin(fileName,username,name);
		if(res!=0) {
			map.put("status", 1);
			map.put("info", "上传成功");
		}else {
			map.put("status", 2);
			map.put("info", "上传失败");
		}
		return map;
	}
	
	@RequestMapping("get_user_list")
	@ResponseBody
	public List<Map<String,Object>> get_user_list(HttpServletRequest req) {
		
		String username = req.getParameter("username");
		List<Map<String,Object>> list  = userservice.get_user_list(username);
		return list;
	}
	
	@RequestMapping("get_shipin")
	@ResponseBody
	public List<Map<String,Object>> get_shipin(HttpServletRequest req) {
		
		List<Map<String,Object>> list  = userservice.get_shipin();
		return list;
	}
	
	
	@RequestMapping("get_fangjian_list")
	@ResponseBody
	public List<Map<String,Object>> get_fangjian_list(HttpServletRequest req) {
		
		String fangjianname = req.getParameter("fangjianname");
		String username = req.getSession().getAttribute("username").toString();
		List<Map<String,Object>> list  = userservice.get_fangjian_list(fangjianname,username);
		return list;
	}
	
	
	@RequestMapping("get_fangjian_list1")
	@ResponseBody
	public List<Map<String,Object>> get_fangjian_list1(HttpServletRequest req) {
		
		String fangjianname = req.getParameter("fangjianname");
		String username = "admin";
		List<Map<String,Object>> list  = userservice.get_fangjian_list(fangjianname,username);
		return list;
	}
	
	@RequestMapping("get_role")
	@ResponseBody
	public Map<String,Object> get_role(HttpServletRequest req) {
		
		String user_no = req.getParameter("user_no");
		Map<String,Object> map  = userservice.get_role(user_no);
		return map;
	}
	
	@RequestMapping("del_user")
	@ResponseBody
	public Map<String,Object> del_user(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		int id = Integer.parseInt(request.getParameter("id"));
		int res = userservice.del_user(id);
		if(res!=0) {
			map.put("status",1);
			map.put("info","删除成功");
		}else {
			map.put("status",0);
			map.put("info","删除失败");
		}
		
		return map;
	}
	
	
	
	@RequestMapping("del_fangjian")
	@ResponseBody
	public Map<String,Object> del_fangjian(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		int id = Integer.parseInt(request.getParameter("id"));
		int res = userservice.del_fangjian(id);
		if(res!=0) {
			map.put("status",1);
			map.put("info","删除成功");
		}else {
			map.put("status",0);
			map.put("info","删除失败");
		}
		
		return map;
	}
	
	@RequestMapping("save_user")
	@ResponseBody
	public Map<String,Object> save_user(HttpServletRequest req) {
		
		int id = Integer.parseInt( req.getParameter("id"));
		String username = req.getParameter("username1");
		String password = req.getParameter("password");
		String leixing = req.getParameter("leixing");
		
		
		Map<String,Object> map  = new HashMap<String,Object>();
			int res = 	userservice.save_user(id,username,password,leixing);
			if(res!=0) {
				map.put("status", "1");
				map.put("info", "操作成功");
			}else {
				map.put("status", "0");
				map.put("info", "操作失败");
			}
		return map;
	}
	
	
	@RequestMapping("save_fangjian")
	@ResponseBody
	public Map<String,Object> save_fangjian(HttpServletRequest req) {
		
		int id = Integer.parseInt( req.getParameter("id"));
		String fangjianname = req.getParameter("fangjianname1");
		String title = req.getParameter("title");
		String username = req.getSession().getAttribute("username").toString();
		int  fangjian_flag = Integer.parseInt( req.getParameter("fangjian_flag"));
		Map<String,Object> map  = new HashMap<String,Object>();
		
		Map<String,Object> fj  =  userservice.get_fangjian_name(fangjianname);
		if(fj==null) {
			int res = 999;
			if(fangjian_flag==0) {
				res = 	userservice.save_fangjian(fangjianname,title,username);
			}else {
				res = 	userservice.edit_fangjian(id,fangjianname,title);
			}
			if(res!=0) {
				map.put("status", "1");
				map.put("info", "操作成功");
			}else {
				map.put("status", "0");
				map.put("info", "操作失败");
			}
		}else {
			map.put("status", "2");
			map.put("info", "房间名已存在，换一个房间名");
		}
		return map;
	}
}
