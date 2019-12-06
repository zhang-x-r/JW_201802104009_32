package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 20",urlPatterns = {"/*"})
public class Filter20 implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 20 -encodingg begins");
        HttpServletRequest request = (HttpServletRequest)req;//ServletRequest中无getRequestURI()方法
        HttpServletResponse response = (HttpServletResponse)res;
        //获得path
        String path = request.getRequestURI();
        //创建JSON对象
        JSONObject message = new JSONObject();
        if(!path.contains("/login")) {
            //设置响应字符编码为UTF-8
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("设置响应字符编码为UTF-8");
            String method = request.getMethod();
            if("POST-PUT".contains(method)){
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
                System.out.println("设置请求字符编码格式为UTF-8");
            }
            //访问权限验证
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentUser")==null){
                message.put("message","请登录或重新登陆");
                //响应message到前端
                response.getWriter().println(message);
            }
        }
        chain.doFilter(req,res);//执行其他过滤器，如果过滤器已经执行完毕，则执行原请求
        System.out.println("Filter 20 -encoding ends");
    }
}
