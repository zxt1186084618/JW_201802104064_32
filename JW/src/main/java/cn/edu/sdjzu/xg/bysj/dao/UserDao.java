package cn.edu.sdjzu.xg.bysj.dao;



import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class UserDao {
    //创建私有的静态的UserDao对象
    private static UserDao userDao = new UserDao();
    //创建私有的静态的集合users
    private static Collection<User> users = new HashSet<User>();
    //定义getInstance()方法,返回userDao指向对象
    public static UserDao getInstance(){
        return userDao;
    }
    //定义findByUsername()方法，返回users集合
    public User findByUsername(String username) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateUser_sql = "SELECT * FROM User where username = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        pstmt.setString(1,username);
        ResultSet resultSet = pstmt.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = new User(resultSet.getString("username"), resultSet.getString("password"), TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
        }
        JdbcHelper.close(pstmt,connection);
        return user;
    }
    //定义find()方法
    public User find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String updateUser_sql = "SELECT * FROM User where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateUser_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = new User(resultSet.getString("username"), resultSet.getString("password"),TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
        }
        JdbcHelper.close(pstmt,connection);
        return user;
    }
    public boolean changePassword(Integer id,String password) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateUser_sql = " update user set password = ? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1, password);
        preparedStatement.setInt(2,id);
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("修改了" + affectedRows + "条记录");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
    //通过向集合中添加user，实现添加功能
    public boolean add(User user,Connection connection) throws SQLException {
        Connection connectionByTeacher = connection;
        //创建sql语句
        String addUser_sql="Insert into User (username,password,teacher_id) values (?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(addUser_sql);
        //为预编译参数赋值
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setInt(3, user.getTeacher().getId());
        users.add(user);
        //执行预编译对象的excuteUpdate方法，获取添加的记录行数
        int affectedRowNum=pstmt.executeUpdate();
        //显示添加的记录的行数
        System.out.println("添加了"+affectedRowNum+"条记录");
        pstmt.close();
        return affectedRowNum > 0;
    }
    public boolean delete(Integer teacher_id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String deleteUser_sql = "DELETE FROM User WHERE teacher_id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteUser_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1, teacher_id);
        //执行预编译语句，获取删除记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        preparedStatement.close();
        return affectedRows > 0;
    }
    public User login(String username,String password) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateUser_sql = "select * from user where username=? and password=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateUser_sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User(resultSet.getString("username"), resultSet.getString("password"),TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
        return user;
    }
}
