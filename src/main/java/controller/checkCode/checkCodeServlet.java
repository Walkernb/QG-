package controller.checkCode;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "checkCodeServlet", value = "/checkCodeServlet")
public class checkCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int width=250,  height=50;
        //1.创建对象（一张图片）
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        //2.美化图片
        Graphics graphics = bufferedImage.getGraphics();    //获取“画笔”
        graphics.setColor(Color.white);                   //设置画笔颜色
        graphics.fillRect(0,0,width,height);            //填充背景颜色

        graphics.setColor(Color.BLACK);
        graphics.drawRect(0,0,width-1,height-1);    //画边框

        graphics.setFont(new Font("宋体", Font.BOLD&Font.ITALIC, 30));   //字体样式
        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrtuvwxyz1234567890";
        String checkCode="";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            graphics.setColor(new Color(random.nextInt(255),random.nextInt(255), random.nextInt(255)));
            int index = random.nextInt(str.length());   //随机生产str.length()以内的整数
            char c = str.charAt(index);                 //获取数组中下标为index的字符
            checkCode+=c;
            graphics.drawString(c+"",(i+1)*50,height/2 );    //画字符
        }
        for(int j=0,n=random.nextInt(200);j<n;j++){
            graphics.setColor(Color.RED);
            graphics.fillRect(random.nextInt(width),random.nextInt(height),1,1);//随机点
        }
        request.getSession().setAttribute("checkCode",checkCode);
        //3.在web上显示
        ImageIO.write(bufferedImage,"jpg",response.getOutputStream());
    }
}
