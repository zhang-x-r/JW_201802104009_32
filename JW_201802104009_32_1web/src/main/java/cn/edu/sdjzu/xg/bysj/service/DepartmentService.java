
package cn.edu.sdjzu.xg.bysj.service;

import cn.edu.sdjzu.xg.bysj.dao.DepartmentDao;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public final class DepartmentService {
    private static DepartmentDao departmentDao= DepartmentDao.getInstance();
    private static DepartmentService departmentService=new DepartmentService();
//    private DepartmentService(){}
    public static DepartmentService getInstance(){
        return departmentService;
    }
    public Collection<Department> findAll(){
        return departmentDao.findAll();
    }
//    public Collection<Department> getAll(){
//        return departmentDao.findAll();
//    }
    public Collection<Department> getAll(School school){
        Collection<Department> departments = new HashSet<Department>();
        for(Department department: departmentDao.findAll()){
            if(department.getSchool()==school){
                departments.add(department);
            }
        }
        return departments;
    }
    public Department find(Integer id)throws SQLException{
        return departmentDao.find(id);
    }
    public boolean update(Department department)throws ClassNotFoundException, SQLException {
        return departmentDao.update(department);
    }
    public boolean add(Department department)throws SQLException,ClassNotFoundException{
        return departmentDao.add(department);
    }
    public boolean delete(Integer id)throws SQLException,ClassNotFoundException{
        return departmentDao.delete(id);
    }
    public Collection<Department> findALLBySchool(Integer schoolId) throws SQLException {
        return departmentDao.findAllBySchool(schoolId);
    }
}

