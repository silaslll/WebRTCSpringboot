package com.live.servet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaServlet extends HttpServlet {
	private static final long seriaLVersionUTD = 1;
	public final static int WIDTH = 80;
	public final static int HTIGHT = 20;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) {

		// 在内存环境中创建一个图片对象,类比画图
		BufferedImage im = new BufferedImage(WIDTH, HTIGHT,
				BufferedImage.TYPE_INT_RGB);

		// 画图: 获得画笔,设置颜色,绘图
		// Graphics 图形
		Graphics g = im.getGraphics();

		// 画笔沾颜色
		g.setColor(new Color(255, 255, 255));

		// 绘图: 底色 绘制验证码字符,干扰线
		g.fillRect(0, 0, WIDTH, HTIGHT);

		// 生成四位随机码
		Random r = new Random();

		// 注释生成的随机码
		/* String code = r.nextInt(9999)+""; */

		// 接收方法,绘制随机码,位数可以控制
		String code = getCodeStr(4);

		// 把验证码存入到session中,因为要验证
		request.getSession().setAttribute("code", code);

		// 设置字体
		g.setFont(new Font(null, Font.BOLD | Font.ITALIC, 18));

		// 重新绘制颜色
		g.setColor(new Color(0, 0, 0));

		// 绘上去
		g.drawString(code, 10, 20);

		// 加干扰线
		for (int i = 0; i < 6; i++) {
			// 干扰线加颜色(红绿蓝随机)
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

			// 绘制线条
			g.drawLine(r.nextInt(WIDTH), r.nextInt(HTIGHT), r.nextInt(WIDTH),
					r.nextInt(HTIGHT));
		}

		// 设置响应消息
		response.setContentType("image/jpeg");

		try {
			OutputStream output = response.getOutputStream();
			ImageIO.write(im, "jpeg", output);
		} catch (IOException e) {
			System.out.println("图片格式出错!");
		}

	}

	private String getCodeStr(int length) {
		String str = "QWWERTYUIPASDFGHJKZXCVBNM23456789";
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			// 随机长度,是为了防止数组下标越界
			sb.append(str.charAt(r.nextInt(str.length())));
		}
		return sb.toString();
	}

}
