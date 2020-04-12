package com.gy.fileserver.servlet;

import com.alibaba.fastjson.JSONObject;
import com.gy.fileserver.bean.FileInfo;
import com.gy.fileserver.service.FileService;
import com.gy.fileserver.service.impl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UploadFileServlet
 * 上传文件并加密
 */
public class UploadFileServlet extends HttpServlet {
    private static Logger log = LoggerFactory.getLogger(UploadFileServlet.class);
    FileService fileService = new FileServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileInfo fileInfo = fileService.uploadFile(req);
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(JSONObject.toJSONString(fileInfo));
    }
}
