package cn.edu.sdjzu.xg.bysj.service;

import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.SQLException;

public class UserService {
    private static UserService userService = new UserService();
    private static UserDao userDao = new UserDao();
    public static UserService getInstance(){
        return userService;
    }
    public boolean changePassword(User user)throws SQLException,ClassNotFoundException {
        return userDao.changePassword(user);
    }
    public User find(Integer id)throws SQLException{
        return userDao.find(id);
    }
    public User findByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }
    public User login(String username,String password)throws SQLException{
        return userDao.login(username,password);
    }

}
