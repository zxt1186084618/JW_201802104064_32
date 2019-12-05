package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class DepartmentDao {
    //创建私有的静态的DepartmentDao对象
    private static DepartmentDao departmentDao = new DepartmentDao();
    //创建私有的静态的集合departments
    private static Collection<Department> departments = new HashSet<Department>();
    //定义私有的构造器
    private DepartmentDao(){}
    //定义getInstance()方法,返回departmentDao指向对象
    public static DepartmentDao getInstance(){
        return departmentDao;
    }

    public static Collection<Department> findAll() throws SQLException {
        Collection<Department> departments = new HashSet<>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from department");
        //从数据库中取出数据
        School school = null;
        while (resultSet.next()) {
            school = SchoolService.getInstance().find(resultSet.getInt("school_id"));
            Department department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"), school);
            departments.add(department);
        }
        return departments;
    }
    public static Collection findAllBySchool(Integer schoolId) throws SQLException {
        Collection findAllBySchool = new TreeSet<Department>();
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from department where id = ?");
        preparedStatement.setInt(1,schoolId);
        ResultSet resultSet = preparedStatement.executeQuery();
        //从数据库中取出数据
        while (resultSet.next()) {
            ((TreeSet) findAllBySchool).add(new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    SchoolService.getInstance().find(resultSet.getInt("school_id"))
                    ));
        }
        return findAllBySchool;
    }
    public Department find(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String findDepartment_sql = "select * from department where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(findDepartment_sql);
        pstmt.setInt(1, id);
        //返回结果集
        ResultSet resultSet = pstmt.executeQuery();
        //获取第二行
        Department department = null;
        if (resultSet.next()) {
            department = new Department(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"),
                    SchoolService.getInstance().find(resultSet.getInt("school_id")));
        }
        JdbcHelper.close(pstmt, connection);
        return department;
    }

    public boolean update(Department department) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        String updateDepartment_sql = "update department set description=?,no=?,remarks=? where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateDepartment_sql);
        preparedStatement.setString(1, department.getDescription());
        preparedStatement.setString(2, department.getNo());
        preparedStatement.setString(3, department.getRemarks());
        preparedStatement.setInt(4, department.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("更新了" + affectedRows + "条记录");
        //关闭资源
        JdbcHelper.close(preparedStatement, connection);
        return affectedRows > 0;
    }

    //通过向集合中添加department，实现添加功能
    public boolean add(Department department) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String addDepartment_sql="Insert into Department (no,description,remarks,school_id) values (?,?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt =connection.prepareStatement(addDepartment_sql);
        //为预编译参数赋值
        pstmt.setString(1, department.getNo());
        pstmt.setString(2, department.getDescription());
        pstmt.setString(3, department.getRemarks());
        pstmt.setInt(4,department.getSchool().getId());
        departments.add(department);
        //执行预编译对象的excuteUpdate方法，获取添加的记录行数
        int affectedRowNum=pstmt.executeUpdate();
        //显示添加的记录的行数
        System.out.println("添加了"+affectedRowNum+"条记录");
        JdbcHelper.close(pstmt,connection);
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //删除数据
        String deleteDepartment_sql = "delete from department where id=?";
        PreparedStatement pstmt = connection.prepareStatement(deleteDepartment_sql);
        //为预编译参数赋值
        pstmt.setInt(1, id);
        //执行预编译对象的executeUpdate方法,获取删除记录的行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了" + affectedRowNum + "条记录");
        return affectedRowNum > 0;
    }
}


//		Department department1 = DepartmentService.getInstance().find(2);
//		System.out.println("Description = "+department1.getDescription());}

