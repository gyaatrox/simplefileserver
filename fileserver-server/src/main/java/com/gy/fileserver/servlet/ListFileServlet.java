package com.gy.fileserver.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gy.fileserver.bean.FileInfo;
import com.gy.fileserver.service.FileService;
import com.gy.fileserver.service.impl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ListFileServlet
 * 通过指定count来获取多少条文件信息，返回json数组
 */
public class ListFileServlet extends HttpServlet {
    FileService fileService = new FileServiceImpl();
    private static Logger log = LoggerFactory.getLogger(GetFileInfoServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("utf-8");
        String str = req.getParameter("count");
        if (!"".equals(str)) {
            Integer count = Integer.parseInt(str);
            if (count >= 0) {
                List<FileInfo> fileInfos = fileService.listFileInfo(count);
                log.debug("fileInfos===>{}", fileInfos);
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(fileInfos));
                resp.getWriter().write(jsonArray.toJSONString());
            } else {
                resp.getWriter().write("Parameter value must be >= 0.");
            }
        } else {
            resp.getWriter().write("查询条数不能为空");
        }
    }
}
