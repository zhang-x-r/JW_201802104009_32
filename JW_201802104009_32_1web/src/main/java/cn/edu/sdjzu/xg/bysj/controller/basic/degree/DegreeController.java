//201802104009
package cn.edu.sdjzu.xg.bysj.controller.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
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

//http://106.54.106.34:8080/degree.ctl
@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
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
                responseDegrees(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseDegree(id, response);
            }
        } catch(Exception e){
            message.put("message", "获得失败");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找学院
            Degree degree = DegreeService.getInstance().find(id);
            String degree_json = JSON.toJSONString(degree);

            //响应message到前端
            response.getWriter().println(degree_json);
        }catch(Exception e){
            e.getStackTrace();}
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);

        //响应message到前端
        response.getWriter().println(degrees_json);
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
        //到数据库表中删除对应的学院
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        try {
            DegreeService.getInstance().delete(id);
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "删除失败");
        }
        //响应
        response.getWriter().println(message);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //用大于4的随机数给degreeToAdd的id赋值
        degreeToAdd.setId(4 + (int)(1000*Math.random()));
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //在数据库表中增加Degree对象
        try {
            DegreeService.getInstance().add(degreeToAdd);
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "增加失败");
        }
        //响应
        response.getWriter().println(message);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String degree_json = JSONUtil.getJSON(request);;
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        System.out.println("degreeToAdd="+degreeToAdd);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表修改Degree对象对应的记录
        try {
            DegreeService.getInstance().update(degreeToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "更新失败");
        }
        //响应
        response.getWriter().println(message);

    }
}

