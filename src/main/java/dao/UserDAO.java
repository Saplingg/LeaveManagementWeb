/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Employee;
import model.User;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAO {

    /**
     * Lấy User để đăng nhập
     * @param username
     * @param password
     * @return 
     */
    public User getUserByUsernameAndPassword(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password); // Trong thực tế, mật khẩu phải được hash
            return query.uniqueResult(); // Trả về 1 user hoặc null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy thông tin Employee từ User ID
     */
    public Employee getEmployeeByUserId(int uid) {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             // Dùng Enrollment để liên kết
            Query<Employee> query = session.createQuery(
                    "SELECT e FROM Employee e JOIN e.user u WHERE u.uid = :uid", Employee.class);
            query.setParameter("uid", uid);
            return query.uniqueResult(); 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
