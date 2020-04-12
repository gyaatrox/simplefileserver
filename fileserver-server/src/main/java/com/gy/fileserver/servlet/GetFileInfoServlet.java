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
 * GetFileInfoServlet
 * 通过指定uuid获取指定的文件信息
 */
public class GetFileInfoServlet extends HttpServlet {
    FileService fileService = new FileServiceImpl();
    private static Logger log = LoggerFactory.getLogger(GetFileInfoServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("charset","utf-8");
        String uuid = req.getParameter("uuid");
        if (!"".equals(uuid)) {
            FileInfo fileInfo = fileService.getFileInfo(uuid);
            log.debug("fileInfo===>{}",fileInfo);
            if (fileInfo.getSize()!=null) {
                resp.getWriter().write(JSONObject.toJSONString(fileInfo));
            } else {
                resp.getWriter().write("请检查文件名");
            }
        } else {
            resp.getWriter().write("请输入uuid");
        }
    }
}
