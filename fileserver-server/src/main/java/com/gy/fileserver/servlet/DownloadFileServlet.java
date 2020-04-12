package com.gy.fileserver.servlet;

import com.alibaba.fastjson.JSONObject;
import com.gy.fileserver.bean.ReturnFile;
import com.gy.fileserver.service.FileService;
import com.gy.fileserver.service.impl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DownloadFileServlet
 * 通过指定uuid下载文件
 */
public class DownloadFileServlet extends HttpServlet {
    FileService fileService = new FileServiceImpl();
    private static Logger log = LoggerFactory.getLogger(DownloadFileServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("utf-8");
        String uuid = req.getParameter("uuid");
        if (!"".equals(uuid)) {
            ReturnFile returnFile = fileService.downloadFile(uuid);
            log.debug("fileInfo===>{}",returnFile);
            if (returnFile!=null) {
                resp.getWriter().write(JSONObject.toJSONString(returnFile));
            } else {
                resp.getWriter().write("<h1>410 Gone</h1>");
            }
        } else {
            resp.getWriter().write("请输入uuid");
        }
    }
}
