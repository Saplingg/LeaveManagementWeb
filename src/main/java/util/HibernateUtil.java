/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Tạo StandardServiceRegistry
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml") // Tải cấu hình từ file
                    .build();

            // Tạo MetadataSources
            MetadataSources metadataSources = new MetadataSources(standardRegistry);

            // Tạo Metadata
            Metadata metadata = metadataSources.getMetadataBuilder().build();

            // Tạo SessionFactory
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            // Log lỗi
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Đóng caches và connection pools
        getSessionFactory().close();
    }
}
