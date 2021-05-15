package util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传的工具类
 */
public class FileupoadUtils {
    /**
     * 修改头像的方法
     * @param request
     * @param response
     */
    public static String alterPhoto(HttpServletRequest request, HttpServletResponse response){
        String fileName = null;
        try {
                        request.setCharacterEncoding("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    response.setContentType("text/html;charset=utf-8");
                    FileItemFactory factory = new DiskFileItemFactory();        //获取文件上传的工厂
                    ServletFileUpload upload = new ServletFileUpload(factory);  //获取文件上传的组件
                    try {
                        List<FileItem> fileItems = upload.parseRequest(request);
                        for (FileItem item : fileItems) {
                            if(!item.isFormField()){                            //不是普通表单
                                String uuid = UUID.randomUUID().toString();    //生成UUID码
//                    System.out.println(uuid);
                                int i = item.getName().lastIndexOf(".");    //获取文件名字符串中后缀名的位置
                                fileName = uuid + item.getName().substring(i);  //生成一个唯一的文件名
                                //获取存放图片的资源路径 （这里写“死”了）
                                File file = new File("D:\\ideaCode\\QGtieba2\\src\\main\\webapp\\resource");
                    if(!file.exists()){
                        //如果文件不存在，就创建文件
                        file.mkdirs();
                    }
                    File file2 = new File(file, fileName);              //上传文件
//                    System.out.println(file2);
                    item.write(file2);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
