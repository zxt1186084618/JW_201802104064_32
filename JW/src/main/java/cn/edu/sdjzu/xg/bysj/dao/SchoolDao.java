package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class SchoolDao {
    //创建私有的静态的SchoolDao对象
    private static SchoolDao schoolDao = new SchoolDao();
    //创建私有的静态的集合schools
    private static Collection<School> schools = new HashSet<School>();
    //定义私有的构造器
    private SchoolDao(){}
    //定义getInstance()方法,返回schoolDao指向对象
    public static SchoolDao getInstance(){
        return schoolDao;
    }
    //定义findAll()方法,返回schools集合
    public Collection<School> findAll() throws SQLException{
        schools.clear();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //执行SQL查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from School");
        while (resultSet.next()){
            School school = new School(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
            schools.add(school);
        }
        return schools;
    }
    //定义find()方法
    public School find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String updateSchool_sql = "SELECT * FROM School where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateSchool_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        School school = null;
        if (resultSet.next()) {
            school = new School(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getString("no"), resultSet.getString("remarks"));
        }
        JdbcHelper.close(pstmt,connection);
        return school;
    }
    //通过向集合中添加school，实现添加功能
    public boolean add(School school) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String addSchool_sql="Insert into School (no,description,remarks) values (?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(addSchool_sql);
        //为预编译参数赋值
        pstmt.setString(1, school.getNo());
        pstmt.setString(2, school.getDescription());
        pstmt.setString(3, school.getRemarks());
        schools.add(school);
        //执行预编译对象的excuteUpdate方法，获取添加的记录行数
        int affectedRowNum=pstmt.executeUpdate();
        //显示添加的记录的行数
        System.out.println("添加了"+affectedRowNum+"条记录");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    //定义delete()方法
    public boolean delete(School school) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String deleteSchool_sql="Delete from School where id=? ";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(deleteSchool_sql);
        //为预编译参数赋值
        pstmt.setInt(1,school.getId());
        //执行预编译对象的excuteUpdate方法
        int affectedRowNum=pstmt.executeUpdate();
        //显示删除的记录的行数
        System.out.println("删除了"+affectedRowNum+"条记录");
        JdbcHelper.close(pstmt,connection);
        return true ;
    }
    public boolean update(School school) throws ClassNotFoundException,SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateSchool_sql = " update school set description=?,no=?,remarks=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,school.getDescription());
        preparedStatement.setString(2,school.getNo());
        preparedStatement.setString(3,school.getRemarks());
        preparedStatement.setInt(4,school.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("更新了" + affectedRows + "条记录");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
}
