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

		// ���ڴ滷���д���һ��ͼƬ����,��Ȼ�ͼ
		BufferedImage im = new BufferedImage(WIDTH, HTIGHT,
				BufferedImage.TYPE_INT_RGB);

		// ��ͼ: ��û���,������ɫ,��ͼ
		// Graphics ͼ��
		Graphics g = im.getGraphics();

		// ����մ��ɫ
		g.setColor(new Color(255, 255, 255));

		// ��ͼ: ��ɫ ������֤���ַ�,������
		g.fillRect(0, 0, WIDTH, HTIGHT);

		// ������λ�����
		Random r = new Random();

		// ע�����ɵ������
		/* String code = r.nextInt(9999)+""; */

		// ���շ���,���������,λ�����Կ���
		String code = getCodeStr(4);

		// ����֤����뵽session��,��ΪҪ��֤
		request.getSession().setAttribute("code", code);

		// ��������
		g.setFont(new Font(null, Font.BOLD | Font.ITALIC, 18));

		// ���»�����ɫ
		g.setColor(new Color(0, 0, 0));

		// ����ȥ
		g.drawString(code, 10, 20);

		// �Ӹ�����
		for (int i = 0; i < 6; i++) {
			// �����߼���ɫ(���������)
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

			// ��������
			g.drawLine(r.nextInt(WIDTH), r.nextInt(HTIGHT), r.nextInt(WIDTH),
					r.nextInt(HTIGHT));
		}

		// ������Ӧ��Ϣ
		response.setContentType("image/jpeg");

		try {
			OutputStream output = response.getOutputStream();
			ImageIO.write(im, "jpeg", output);
		} catch (IOException e) {
			System.out.println("ͼƬ��ʽ����!");
		}

	}

	private String getCodeStr(int length) {
		String str = "QWWERTYUIPASDFGHJKZXCVBNM23456789";
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			// �������,��Ϊ�˷�ֹ�����±�Խ��
			sb.append(str.charAt(r.nextInt(str.length())));
		}
		return sb.toString();
	}

}
