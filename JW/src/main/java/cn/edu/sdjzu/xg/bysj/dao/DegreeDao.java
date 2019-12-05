package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Degree;

import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public final class DegreeDao {
    //创建私有的静态的DegreeDao对象
    private static DegreeDao degreeDao = new DegreeDao();
    //创建私有的静态的集合degrees
    private static Collection<Degree> degrees = new HashSet<Degree>();
    //定义私有的构造器
    private DegreeDao(){}
    //定义getInstance()方法,返回degreeDao指向对象
    public static DegreeDao getInstance(){
        return degreeDao;
    }
    //定义findAll()方法,返回degrees集合
    public static Collection<Degree> findAll() throws SQLException{
        degrees.clear();
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //执行SQL查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from Degree");
        while (resultSet.next()){
            Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
            degrees.add(degree);
        }
        return degrees;
    }
    //定义find()方法
    public Degree find(Integer id) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        String updateDegree_sql = "SELECT * FROM Degree where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
        pstmt.setInt(1,id);
        ResultSet resultSet = pstmt.executeQuery();
        Degree degree = null;
        if (resultSet.next()) {
            degree = new Degree(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getString("no"), resultSet.getString("remarks"));
        }
        JdbcHelper.close(pstmt,connection);
        return degree;
    }
    //通过向集合中添加degree，实现添加功能
    public boolean add(Degree degree) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String addDegree_sql="Insert into Degree (no,description,remarks) values (?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(addDegree_sql);
        //为预编译参数赋值
        pstmt.setString(1, degree.getNo());
        pstmt.setString(2, degree.getDescription());
        pstmt.setString(3, degree.getRemarks());
        degrees.add(degree);
        //执行预编译对象的excuteUpdate方法，获取添加的记录行数
        int affectedRowNum=pstmt.executeUpdate();
        //显示添加的记录的行数
        System.out.println("添加了"+affectedRowNum+"条记录");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }
    //定义delete()方法
    public boolean delete(Degree degree) throws SQLException{
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String deleteDegree_sql="Delete from Degree where id=? ";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(deleteDegree_sql);
        //为预编译参数赋值
        pstmt.setInt(1,degree.getId());
        //执行预编译对象的excuteUpdate方法
        int affectedRowNum=pstmt.executeUpdate();
        //显示删除的记录的行数
        System.out.println("删除了"+affectedRowNum+"条记录");
        JdbcHelper.close(pstmt,connection);
        return true ;
    }
    public boolean update(Degree degree) throws ClassNotFoundException,SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateDegree_sql = " update degree set description=?,no=?,remarks=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,degree.getDescription());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getRemarks());
        preparedStatement.setInt(4,degree.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("更新了" + affectedRows + "条记录");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
}

