package cn.edu.sdjzu.xg.bysj.controller.basic.user;


import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String value_str = request.getParameter("value");
        String type = request.getParameter("type");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有专业对象，否则响应id指定的专业对象
            if (type.equals("id")) {
                int id = Integer.parseInt(value_str);
                responseUsersById(id,response);
            } else if (type.equals("username")){
                responseUsersByUsername(value_str,response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    private void responseUsersById(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        User user = UserService.getInstance().findById(id);
        String user_json = JSON.toJSONString(user);

        //响应user_json到前端
        response.getWriter().println(user_json);
    }
    private void responseUsersByUsername(String value, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据学院的id查找专业
        User user = UserService.getInstance().findByUsername(value);
        String user_json = JSON.toJSONString(user);
        //响应user_json到前端
        response.getWriter().println(user_json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = null;
        JSONObject message = new JSONObject();
        try {
            user = UserService.getInstance().login(username,password);
        } catch (SQLException e){
            e.printStackTrace();
            //message.put("message","数据库操作异常");
        }catch(Exception e){
            e.printStackTrace();
            //message.put("message","网络异常");
        }
        String user_json = JSON.toJSONString(user);
        response.getWriter().println(user_json);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        String password = request.getParameter("password");
        JSONObject message = new JSONObject();
        try {
            UserService.getInstance().changePassword(id,password);
            message.put("message","修改成功");
        } catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }//响应message到前端
        response.getWriter().println(message);
    }
}
