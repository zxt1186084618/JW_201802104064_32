package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@WebFilter(filterName = "Filter1",urlPatterns = {"/*"})//所有的目录下的资源进行过滤
public class Filter1 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter1 - date begins");
        //将参数servletRequest强制类型转换为HttpServletRequest，声明HttpServletRequest变量request指向它
        HttpServletRequest request=(HttpServletRequest)req;
        //获得请求路径
        String path=request.getRequestURI();
        //获得时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
        String time = sdf.format(date);
        System.out.println(path+" @ "+time);
        chain.doFilter(req, resp);
        System.out.println("Filter1 - date ends");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
