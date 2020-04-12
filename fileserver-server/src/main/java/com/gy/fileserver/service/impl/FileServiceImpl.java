package com.gy.fileserver.service.impl;

import com.gy.fileserver.bean.FileInfo;
import com.gy.fileserver.bean.ReturnFile;
import com.gy.fileserver.dao.FileDao;
import com.gy.fileserver.dao.impl.FileDaoImpl;
import com.gy.fileserver.service.FileService;
import com.gy.fileserver.utils.AesUtil;
import com.gy.fileserver.utils.DigestUtil;
import com.gy.fileserver.utils.RsaUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class FileServiceImpl implements FileService {

    FileDao fileDao = new FileDaoImpl();

    @Override
    public FileInfo uploadFile(HttpServletRequest req) {
        FileInfo fileInfo = new FileInfo();
        //上传文件
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
        try {
            List<FileItem> fileItems = fileUpload.parseRequest(req);
            for (FileItem fileItem : fileItems) {
                if (!fileItem.isFormField()) {
                    //原始文件名
                    String name = fileItem.getName();
                    fileInfo.setOriginName(name);
                    //文件类型
                   // String type = name.substring(name.lastIndexOf(".") + 1);
                    String contentType = fileItem.getContentType();
                    fileInfo.setType(contentType);
                    //文件新名
                    String newName = UUID.randomUUID().toString().replaceAll("-", "");
                    fileInfo.setUuid(newName);
                    //创建时间
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss:SSS");
                    String createTime = dateTimeFormatter.format(now);
                    fileInfo.setCreateTime(createTime);
                    //文件保存地址
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String format = formatter.format(now);
                    String strPath = "D:/fileserver/" + format ;
                    File file = new File(strPath);
                    if(!file.exists()){
                      file.mkdirs();
                    }
                    fileInfo.setSaveAddress("D:/fileserver/" + format);
                    //把流加密写入文件
                    InputStream inputStream = fileItem.getInputStream();//输入流
                    fileInfo.setSize(inputStream.available());//文件大小 单位byte
                    String key = AesUtil.getKey();//获取aes秘钥
                    String encrypt = AesUtil.encrypt(IOUtils.toString(inputStream,"utf-8"), key);
                    FileOutputStream fileOutputStream = new FileOutputStream(strPath+ "/" + newName);//输出流
                    fileOutputStream.write(encrypt.getBytes());
                    fileOutputStream.close();
                    //数字信封
                    Properties properties = new Properties();
                    InputStream in = getClass().getClassLoader().getResourceAsStream("publicKey.properties");
                    properties.load(in);
                    String publicKey = properties.getProperty("publicKey");
                    byte[] bytes = RsaUtil.encryptByPublicKey(key.getBytes(), publicKey);
                    fileInfo.setDigitEnvelop(DigestUtil.byte2hex(bytes));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据存入db
        fileDao.insert(fileInfo);
        return fileInfo;
    }

    @Override
    public ReturnFile downloadFile(String uuid) {
        FileInfo fileInfo = fileDao.findFileByUuid(uuid);
        ReturnFile returnFile = new ReturnFile();
        returnFile.setOriginName(fileInfo.getOriginName());
        returnFile.setDigitEnvelop(fileInfo.getDigitEnvelop());
        returnFile.setType(fileInfo.getType());
        //读出对应的文件
        String path = fileInfo.getSaveAddress()+"/"+uuid;
        FileInputStream inputStream =null;
        try {
            inputStream = new FileInputStream(path);
            String data = IOUtils.toString(inputStream, "utf-8");
            System.out.println("下载读出的流===="+data);
            returnFile.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnFile;
    }

    @Override
    public FileInfo getFileInfo(String uuid) {
        return fileDao.findFileByUuid(uuid);
    }

    @Override
    public List<FileInfo> listFileInfo(Integer count) {
        return fileDao.listCount(count);
    }
}
