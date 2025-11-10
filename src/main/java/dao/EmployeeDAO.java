/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Employee;
import util.HibernateUtil;
import org.hibernate.Session;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Acer
 */
public class EmployeeDAO {

    /**
     * Lấy tất cả nhân viên thuộc một phòng ban
     * (Không bao gồm chính người quản lý)
     */
    public List<Employee> getEmployeesByDivision(int divisionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Employee e WHERE e.division.did = :did",
                            Employee.class)
                    .setParameter("did", divisionId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
