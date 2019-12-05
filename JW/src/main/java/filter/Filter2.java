package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter2",urlPatterns = {"/*"})
public class Filter2 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 2 - begins");
        HttpServletRequest httpServletRequest = (HttpServletRequest)req;
        //获得请求路径
        String path= httpServletRequest.getRequestURI();
        if (path.contains("/login")){
            //执行其他过滤器，若其他过滤器执行完毕则执行原请求
            chain.doFilter(req,resp);
            //System.out.println("执行下一个过滤器或原请求");
        // 若路径符合条件，则首先设置响应对象字符编码格式为utf8
        }else {
            HttpSession session = httpServletRequest.getSession(false);
            JSONObject message = new JSONObject();
            if (session == null || session.getAttribute("currentUser") == null) {
                message.put("message", "您没有登录，请登录");
                resp.getWriter().println(message);
            } else {
                //执行其他过滤器，若其他过滤器执行完毕则执行原请求
                chain.doFilter(req, resp);
            }
        }
        System.out.println("Filter 3 - ends");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
