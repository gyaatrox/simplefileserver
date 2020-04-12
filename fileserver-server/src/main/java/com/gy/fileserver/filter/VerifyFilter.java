package com.gy.fileserver.filter;

import com.gy.fileserver.utils.SignUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * VerifyFilter
 * 过滤请求的权限
 */
public class VerifyFilter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("开始验证。。。。");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取公钥
        Properties properties = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("publicKey.properties");
        properties.load(in);
        String publicKey = properties.getProperty("publicKey");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String sid = req.getHeader("X-SID");
        String signature = req.getHeader("X-signature");
        if (!"".equals(sid) && !"".equals(signature)) {
            //验签
            boolean verifyResult = SignUtil.verify(publicKey.getBytes(), sid, signature);
            if (verifyResult) {
                chain.doFilter(request, response);
            }else{
                resp.getWriter().write("<h1>403 Forbidden</h1>");
            }
        }

    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁。。。。");
    }
}
