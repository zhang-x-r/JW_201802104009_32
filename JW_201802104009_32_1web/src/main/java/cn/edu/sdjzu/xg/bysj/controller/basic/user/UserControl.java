package cn.edu.sdjzu.xg.bysj.controller.basic.user;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user.ctl")
public class UserControl extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        String username_str = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        try {
            if (id_str != null) {
                int id = Integer.parseInt(id_str);
                responseUser(id, response);
            }
            else{
                responseUserByUsername(username_str,response);
            }
        } catch(Exception e){
            message.put("message", "获得失败");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    public void doPut(HttpServletRequest request,HttpServletResponse response)
        throws ServletException,IOException{
        String user_json = JSONUtil.getJSON(request);;
        //将JSON字串解析为User对象
        User userToChange = JSON.parseObject(user_json, User.class);
        System.out.println("userToChange="+userToChange);
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表修改User对象对应的记录
        try {
            UserService.getInstance().changePassword(userToChange);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "更新失败");
        }
        //响应
        response.getWriter().println(message);

    }
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找用户
            User user= UserService.getInstance().find(id);
            String user_json = JSON.toJSONString(user);

            //响应到前端
            response.getWriter().println(user_json);
        }catch(Exception e){
            e.getStackTrace();}
    }
    private void responseUserByUsername(String username,HttpServletResponse response)
        throws ServletException,IOException{
        try{
            //根据用户名查找用户
            User user = UserService.getInstance().findByUsername(username);
            String user_json = JSON.toJSONString(user);
            //相应message到前端
            response.getWriter().println(user_json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
