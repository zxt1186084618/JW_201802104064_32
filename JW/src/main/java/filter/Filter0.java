package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 0",urlPatterns = {"/*"})//所有资源进行过滤
public class Filter0 implements Filter {
    public void destroy(){
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter 0 - encoding begins");
        //将参数servletRequest强制类型转换为HttpServletRequest，声明HttpServletRequest变量request指向它
        HttpServletRequest request = (HttpServletRequest)req;
        //将参数servletResponse强制类型转换为HttpServletResponse，声明HttpServletResponse变量response指向它
        HttpServletResponse response =(HttpServletResponse)resp;
        //获得请求路径
        String path= request.getRequestURI();
        //获得请求的方法名
        String method=request.getMethod();
        //判断请求的路径是否包含login，若包含则提示不设置字符编码
        if (path.contains("/cn/edu/sdjzu/xg/bysj/controller/basic/login")){
            System.out.println("未设置字符编码格式");
        }else {//若路径符合条件，则首先设置响应对象字符编码格式为utf8
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("响应对象字符编码格式为UTF-8");
            //对调用的方法进行判断，若为post或put则设置请求对象字符编码格式为utf8
            if (method.equals("POST")||method.equals("PUT")){
                request.setCharacterEncoding("UTF-8");
                System.out.println("请求对象字符编码格式为UTF-8");
            }
        }
        //执行其他过滤器，如果过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter 0 - encoding ends");
    }
    public void init(FilterConfig config) throws ServletException{

    }

}
