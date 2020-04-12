package com.gy.fileserver;

import com.gy.fileserver.servlet.GetFileInfoServlet;
import com.gy.fileserver.filter.VerifyFilter;
import com.gy.fileserver.servlet.DownloadFileServlet;
import com.gy.fileserver.servlet.ListFileServlet;
import com.gy.fileserver.servlet.UploadFileServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * file server start
 */
public class ServerApp {
    public static void main(String[] args) {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        //拦截所有请求，验证权限
        FilterHolder filterHolder = handler.addFilterWithMapping(VerifyFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        handler.addFilter(filterHolder);
        //接口的请求路径http://localhost:8080/xxx
        handler.addServletWithMapping(UploadFileServlet.class, "/upload");
        handler.addServletWithMapping(DownloadFileServlet.class, "/download");
        handler.addServletWithMapping(GetFileInfoServlet.class, "/get");
        handler.addServletWithMapping(ListFileServlet.class, "/list");
        try {
            server.start();
            //server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
