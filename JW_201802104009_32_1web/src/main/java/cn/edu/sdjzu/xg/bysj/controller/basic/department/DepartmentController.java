//201802104009
package cn.edu.sdjzu.xg.bysj.controller.basic.department;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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

//http://106.54.106.34:8080/department.ctl
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String paraType = request.getParameter("paraType");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        try {
            //如果id = null, 表示响应所有院系对象，否则响应id指定的院系对象
            if (id_str != null) {
                int id = Integer.parseInt(id_str);
                if (paraType == null) {
                    responseDepartment(id,response);
                } else if (paraType.equals("school")){
                    responseDepartmentBySchool(response, paraType, id);
                }
            }else{
                responseDepartments(response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    private void responseDepartmentBySchool(HttpServletResponse response, String paraType,int id)
            throws SQLException, IOException {
        Collection<Department>departments = DepartmentService.getInstance().findALLBySchool(id);
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应Departments_json到前端
        response.getWriter().println(departments_json);
    }

    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找学院
            Department department = DepartmentService.getInstance().find(id);
            String department_json = JSON.toJSONString(department);
            //响应
            //创建JSON对象message，以便往前端响应信息
            JSONObject message = new JSONObject();
            //加入数据信息
            response.getWriter().println(department_json);

        }catch(Exception e){
            e.getStackTrace();}
    }
    //响应所有学位对象
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException {
        //获得所有学院
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments);

        //响应
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        response.getWriter().println(departments_json);

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
            DepartmentService.getInstance().delete(id);
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
        String department_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //用大于4的随机数给departmentToAdd的id赋值
        departmentToAdd.setId(4 + (int)(1000*Math.random()));
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //在数据库表中增加Department对象
        try {
            DepartmentService.getInstance().add(departmentToAdd);
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
        String department_json = JSONUtil.getJSON(request);;
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        System.out.println("departmentToAdd="+departmentToAdd);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表修改Department对象对应的记录
        try {
            DepartmentService.getInstance().update(departmentToAdd);
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "修改失败");
        }
        //响应
        response.getWriter().println(message);

    }
}

