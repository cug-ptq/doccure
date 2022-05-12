package com.example.doccure.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public static String getNewFileName(String originName){
        return System.currentTimeMillis()
                + originName.substring(originName.lastIndexOf("."));
    }

    /**
     * 得到文件访问地址
     * @param request 得到IP
     * @param filename 文件名
     * @param accessPath 访问目录-虚拟地址映射
     * @param port 端口号
     * @return 文件地址
     */
    public static String getImgURL(HttpServletRequest request,
                                   String filename,String accessPath,String port){
        return request.getScheme() + "://" + request.getServerName()+":"
                + port +
                accessPath + filename;
    }

    /**
     * 上传文件
     * @param file 文件流
     * @param port 端口号
     * @param filePath 实际存储路径
     * @param accessPath 访问路径-虚拟地址
     * @param old_filename 用户的旧文件名-删除（图片）
     * @param request 得到IP
     * @return 文件地址
     */
    public static String uploadFile(MultipartFile file, String port,String filePath,String accessPath,
                                   String old_filename, HttpServletRequest request){
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String newFilename = FileUtil.getNewFileName(fileName);
        File destFile = new File(filePath+newFilename);
        File destCatalog = new File(filePath);
        if (!destCatalog.exists()){
            Boolean result = destCatalog.mkdirs();
        }
        try {
            if (!destFile.exists()){
                File oldImg = new File(filePath+old_filename);
                if (oldImg.exists()){ Boolean aBoolean = oldImg.delete();} //删除旧图片
                Files.copy(file.getInputStream(), destFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FileUtil.getImgURL(request,newFilename,accessPath,port);
    }

//    /**
//     * 上传文件
//     * @param file 文件流
//     * @param port 端口号
//     * @param filePath 实际存储路径
//     * @param accessPath 访问路径-虚拟地址
//     * @param request 得到IP
//     * @return 文件地址
//     */
//    public static String uploadAssessImg(MultipartFile file, String port,String filePath,String accessPath,
//                                         HttpServletRequest request){
//        String fileName = file.getOriginalFilename();
//        assert fileName != null;
//        String newFilename = FileUtil.getNewFileName(fileName);
//        File dest = new File(filePath+newFilename);
//        try {
//            if (!dest.exists()){
//                Files.copy(file.getInputStream(), dest.toPath());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return FileUtil.getImgURL(request,newFilename,accessPath,port);
//    }
}
