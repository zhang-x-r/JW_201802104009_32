//201802104009
package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=
			new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	//返回结果集对象
	public Collection<Teacher> findAll(){
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		try{
			//获得数据库连接对象
			Connection connection = JdbcHelper.getConn();
			//在该连接上创建语句盒子对象
			Statement stmt = connection.createStatement();
			//执行SQL查询语句并获得结果集对象
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM Teacher");
			//若结果存在下一条，执行循环体
			while (resultSet.next()) {
				//打印结果集中记录的id字段
				System.out.print(resultSet.getInt("id"));
				System.out.print(",");
				//打印结果集中记录的name字段
				System.out.print(resultSet.getString("name"));
				System.out.print(",");
				//打印结果集中记录的profTitle字段
				System.out.print(resultSet.getString("title_id"));
				System.out.print(",");
				//打印结果集中记录的degree字段
				System.out.print(resultSet.getString("degree_id"));
				System.out.print(",");
				//打印结果集中记录的department字段
				System.out.print(resultSet.getString("department_id"));
				//根据数据库中的数据,创建Teacher类型的对象
				Teacher teacher = new Teacher(resultSet.getInt("id"),
						resultSet.getString("name"),
						ProfTitleDao.getInstance().find(resultSet.getInt("title_id")),
						DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
						DepartmentDao.getInstance().find(resultSet.getInt("department_id")),
						resultSet.getString("no"));
				//添加到集合teachers中
				teachers.add(teacher);
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return teachers;
	}
	public Teacher find(Integer id) throws SQLException{
		//声明一个Teacher类型的变量
		Teacher teacher = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("title_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")),
					resultSet.getString("no"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}
	public boolean add(Teacher teacher){
		Connection connection = null;
		int teacher_id = 0;
		PreparedStatement preparedStatement = null;
		int affectedRowNum = 0;
		try {
			connection = JdbcHelper.getConn();
			//关闭自动提交(事件开始）
			connection.setAutoCommit(false);
			String addTeacher_sql = "INSERT INTO Teacher (name,title_id,degree_id,department_id,no) VALUES" + " (?,?,?,?,?)";
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement(addTeacher_sql);
			//为预编译参数赋值
			preparedStatement.setString(1, teacher.getName());
			preparedStatement.setInt(2, teacher.getTitle().getId());
			preparedStatement.setInt(3, teacher.getDegree().getId());
			preparedStatement.setInt(4, teacher.getDepartment().getId());
			preparedStatement.setString(5, teacher.getNo());
			//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
			affectedRowNum = preparedStatement.executeUpdate();
			String selectTeacherByNo_sql = "SELECT * FROM teacher WHERE no=?";
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement(selectTeacherByNo_sql);
			//为预编译参数赋值
			preparedStatement.setString(1,teacher.getNo());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				teacher_id = resultSet.getInt("id");
			}
			String addUser_sql = "INSERT INTO User (username,password,teacher_id) VALUES" + " (?,?,?)";
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement(addUser_sql);
			//为预编译参数赋值
			preparedStatement.setString(1, teacher.getNo());
			preparedStatement.setString(2, teacher.getNo());
			preparedStatement.setInt(3, teacher_id);
			preparedStatement.executeUpdate();
			//提交当前连接所做的操作（事件以提交结束）
			connection.commit();
		}catch (SQLException e){
			e.printStackTrace();
			try{
				//回滚当前连接所作的操作
				if (connection != null){
					//事件以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}catch (Exception e){
			e.printStackTrace();
			try{
				//回滚当前连接所作的操作
				if (connection != null){
					//事件以回滚结束
					connection.rollback();
				}
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		} finally {
			try{
				//恢复自动提交
				if (connection!=null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement,connection);
		}
		return affectedRowNum>0;
	}
	//delete方法，根据teacher的id值，删除数据库中对应的degree对象
	public boolean delete(int id) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteUser_sql = "DELETE FROM User WHERE teacher_id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了user"+affectedRows+"行记录");
		//写SQL语句
		String deleteTeacher_sql = "DELETE FROM teacher WHERE id=?";
		preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	public boolean update(Teacher teacher)throws SQLException{
		Connection connection= JdbcHelper.getConn();
		String updateTeacher_sql="UPDATE teacher SET name=?,title_id=?,degree_id=?,department_id=?,no = ? where id=?";
		PreparedStatement pstmt=connection.prepareStatement(updateTeacher_sql);
		pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getTitle().getId());
		pstmt.setInt(3,teacher.getDegree().getId());
		pstmt.setInt(4,teacher.getDepartment().getId());
		pstmt.setString(5,teacher.getNo());
		pstmt.setInt(6,teacher.getId());
		int affectedRowNum=pstmt.executeUpdate();
		System.out.println("修改了"+ affectedRowNum + "条记录");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
//	//创建main方法，查询数据库中的对象，并输出
//	public static void main(String[] args) throws ClassNotFoundException,SQLException{
//		//删
//		//teacherDao.delete(2);
//		//TeacherDao.getInstance().findAll();
//		//查找id为1的teacher对象
//		Teacher teacher1 = TeacherDao.getInstance().find(1);
//		System.out.println(teacher1);
//		//修改teacher1对象的description字段值
//		teacher1.setName("李四");
//		//修改数据库中的对应记录
//		TeacherDao.getInstance().update(teacher1);
//		//查找id为1的teacher对象
//		Teacher teacher2 = TeacherDao.getInstance().find(1);
//		//打印修改后的description字段的值
//		System.out.println(teacher2.getName());
//		Teacher teacher = new Teacher("张三");
//		//System.out.println(TeacherService.getInstance().add(teacher));
//	}
}