package filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(filterName = "Filter 10",urlPatterns = {"/*"})
public class Filter10 implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 -encodingg begins");
        HttpServletRequest request = (HttpServletRequest)req;
        String path = request.getRequestURI();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月DD日HH:mm");
        Date currentTime = new Date();
        String time = sdf.format(currentTime);
        System.out.println("请求资源的名称是"+path+"  请求资源的时间是"+time);
        chain.doFilter(req,res);
        System.out.println("Filter 10 -encoding ends");
    }
}
