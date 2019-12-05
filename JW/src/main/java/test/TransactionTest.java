package test;

import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTest {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement("insert into School (description,no) values (?,?)");
            preparedStatement.setString(1,"管理工程学院");
            preparedStatement.setString(2,"22");
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("insert into School (description,no) values (?,?)");
            preparedStatement.setString(1,"土木学院");
            //模拟一个违反no字段的唯一性约束
            preparedStatement.setString(2,"02");
            preparedStatement.executeUpdate();

            connection.commit();


        }catch (SQLException e){
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
            try {
                //回滚当前连接的所作的操作，从事务日志中删除了记录，对数据库数据无反应
                if (connection != null){
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }finally {
                try {
                    //恢复自动提交
                    if (connection!=null){
                        connection.setAutoCommit(true);
                    }
                }catch (SQLException e3){
                    e3.printStackTrace();
                }

                JdbcHelper.close(preparedStatement,connection);
            }

        }
    }
}
