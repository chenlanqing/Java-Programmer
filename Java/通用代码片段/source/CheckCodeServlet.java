import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckCodeServlet extends HttpServlet {
	private int width = 60;
	private int height = 23;
	
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * 阶段一：绘图
		 */
		//创建一个内存映像对象（画布）
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		//获得画笔
		Graphics g = image.getGraphics();
		//给画笔设置颜色
		Random r = new Random();
		g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
		//给画布设置背景颜色
		g.fillRect(0, 0, width, height);
		//将一个随机数画在画布上
		//String number = r.nextInt(10000) + "";
		String[] arr = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V",
				"W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};
		String number = "";
		do{
			number += arr[r.nextInt(arr.length)];
		}while(number.length()<5);
		//将随机数绑定到session对象上
		HttpSession session = request.getSession();
		session.setAttribute("number",number);
		g.setColor(new Color(255,255,255));
		//设置字体		
		g.setFont(new Font(null,Font.ITALIC|Font.BOLD,15));
		g.drawString(number,3,18);
		//加上一些干扰线
		for(int i=0;i<8;i++){
			g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
			g.drawLine(r.nextInt(width),r.nextInt(height),r.nextInt(width),r.nextInt(height));
		}
		/**
		 * 阶段二：将图片压缩输出到浏览器
		 */
		response.setContentType("image/jpeg");
		//二进制字节输出流
		OutputStream ops = response.getOutputStream();
		//write方法将原始图片压缩,然后发送给response
		javax.imageio.ImageIO.write(image,"jpeg",ops);		
	}
}














