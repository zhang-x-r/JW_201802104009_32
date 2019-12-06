package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.User;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static UserDao userDao = new UserDao();
    public static UserDao getInstance(){return userDao;
    }
    public User find(Integer id) throws SQLException {
        //声明一个User类型的变量
        User user = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写SQL语句
        String findUser_sql = "SELECT * FROM user WHERE id = ?";
        //创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        //创建结果集对象
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("id"))
                    );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public User findByUsername(String username) throws SQLException {
        //声明一个User类型的变量
        User user = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写SQL语句
        String findByUsernameUser_sql = "SELECT * FROM user WHERE username = ?";
        //创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findByUsernameUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        //创建结果集对象
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("userName"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("id"))
            );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public User login(String username, String password)throws SQLException{
        //声明一个User类型的变量
        User user = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写SQL语句
        String loginUser_sql = "select * from user where username=? and password=?";
        //创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(loginUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,password);
        //创建结果集对象
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
                    TeacherDao.getInstance().find(resultSet.getInt("id"))
            );
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public boolean changePassword(User user)throws SQLException,ClassNotFoundException{
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String changePassword_sql = " update user set password =? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(changePassword_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,user.getPassword());
        preparedStatement.setInt(2,user.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        System.out.println("修改了"+affectedRows+"行记录");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
}
