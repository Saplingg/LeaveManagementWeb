/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Date;
import model.RequestForLeave;
import model.RequestHistory;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class RequestDAO {

    /**
     * Lấy danh sách đơn của 1 nhân viên (chỉ đơn của họ)
     */
    public List<RequestForLeave> getRequestsByEmployeeId(int eid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM RequestForLeave r WHERE r.createdBy.eid = :eid ORDER BY r.createdTime DESC",
                            RequestForLeave.class)
                    .setParameter("eid", eid)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Lấy danh sách đơn của các cấp dưới (cho quản lý)
     */
    public List<RequestForLeave> getRequestsForManager(int supervisorEid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy tất cả đơn mà người tạo (createdBy) có supervisorid là supervisorEid
            return session.createQuery(
                            "FROM RequestForLeave r WHERE r.createdBy.supervisor.eid = :supervisorEid " +
                            "ORDER BY r.status ASC, r.createdTime DESC", // Ưu tiên đơn 'In Progress'
                            RequestForLeave.class)
                    .setParameter("supervisorEid", supervisorEid)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * Lấy 1 đơn theo ID
     * @param rid
     * @return 
     */
    public RequestForLeave getRequestById(int rid) {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RequestForLeave.class, rid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy 1 đơn và lịch sử của nó
     */
    public RequestForLeave getRequestWithHistory(int rid) {
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            RequestForLeave request = session.get(RequestForLeave.class, rid);
            if (request != null) {
                // Khởi tạo (fetch) collection history
                request.getHistory().size(); 
            }
            return request;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lưu (Tạo mới hoặc Cập nhật) đơn
     * Cần thêm cả history vào transaction
     */
    public boolean saveOrUpdate(RequestForLeave request, RequestHistory historyEntry) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Liên kết history với request
            if(historyEntry != null) {
                historyEntry.setRequest(request); 
            }
            
            // Lưu cả hai
            session.saveOrUpdate(request);
            if(historyEntry != null) {
                session.save(historyEntry);
            }
            
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    public List<RequestForLeave> getApprovedLeaveForEmployees(List<Integer> employeeIds, Date from, Date to) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyList();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM RequestForLeave r WHERE r.status = 1 " +
                            "AND r.createdBy.eid IN (:eids) " +
                            "AND r.fromDate <= :toDate AND r.toDate >= :fromDate",
                            RequestForLeave.class)
                    .setParameterList("eids", employeeIds)
                    .setParameter("fromDate", from)
                    .setParameter("toDate", to)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
