package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
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
import java.util.Collection;

@WebServlet("/teacher.ctl")
public class TeacherControl extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseTeachers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeacher(id, response);
            }
        } catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找学院
            Teacher teacher = TeacherService.getInstance().find(id);
            String teacher_json = JSON.toJSONString(teacher);
            //响应
            //创建JSON对象message，以便往前端响应信息
            JSONObject message = new JSONObject();
            //加入数据信息
            response.getWriter().println(teacher_json);

            //响应message到前端
           // response.getWriter().println(message);
        }catch(Exception e){
            e.getStackTrace();}
    }
    //响应所有学位对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_json = JSON.toJSONString(teachers);

        //响应
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        response.getWriter().println(teachers_json);
        //响应message到前端
        //response.getWriter().println(message);
    }
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表中删除对应的学院
        try {
            TeacherService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //用大于4的随机数给teacherToAdd的id赋值
        teacherToAdd.setId(4 + (int)(1000*Math.random()));
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //在数据库表中增加Teacher对象
        try {
            TeacherService.getInstance().add(teacherToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String teacher_json = JSONUtil.getJSON(request);;
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        System.out.println("teacherToAdd="+teacherToAdd);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表修改Teacher对象对应的记录
        try {
            TeacherService.getInstance().update(teacherToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }
        //响应
        response.getWriter().println(message);

    }
}
