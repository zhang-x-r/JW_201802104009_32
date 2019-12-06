//201802104009
package cn.edu.sdjzu.xg.bysj.controller.basic.profTitle;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
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
                responseProfTitles(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseProfTitle(id, response);
            }
        } catch(Exception e){
            message.put("message", "获得失败");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            //根据id查找学院
            ProfTitle profTitle = ProfTitleService.getInstance().find(id);
            String profTitle_json = JSON.toJSONString(profTitle);
            //响应
            //创建JSON对象message，以便往前端响应信息
            JSONObject message = new JSONObject();
            //加入数据信息
            response.getWriter().println(profTitle_json);
            //响应message到前端
            //response.getWriter().println(message);
        }catch(Exception e){
            e.getStackTrace();}
    }
    //响应所有学位对象
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles);

        //响应
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //加入数据信息
        response.getWriter().println(profTitles_json);

        //响应message到前端
       // response.getWriter().println(message);
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
            ProfTitleService.getInstance().delete(id);
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
        String profTitle_json = JSONUtil.getJSON(request);

        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //用大于4的随机数给profTitleToAdd的id赋值
        profTitleToAdd.setId(4 + (int)(1000*Math.random()));
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //在数据库表中增加ProfTitle对象
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
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
        String profTitle_json = JSONUtil.getJSON(request);;
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        System.out.println("profTitleToAdd="+profTitleToAdd);
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //创建JSON对象
        JSONObject message = new JSONObject();
        //访问权限验证
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser")==null){
            return;
        }
        //到数据库表修改ProfTitle对象对应的记录
        try {
            ProfTitleService.getInstance().update(profTitleToAdd);
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

