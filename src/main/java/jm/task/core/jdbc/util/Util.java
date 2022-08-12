package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class Util {
    // реализуйте настройку соеденения с БД
    private static final String BD_DRIVERS = "com.mysql.cj.jdbc.Driver";
    private static final String BD_URL = "jdbc:mysql://localhost:3306/new_schema";
    private static final String BD_USER = "root";
    private static final String BD_PASSWORD = "DANEKpalam55";

    private final static String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static Connection conn;
    private static SessionFactory sessionFactory;


    public static Connection getJBDCConnection() {

        try {
            if (conn == null || conn.isClosed()) {
                Class.forName(BD_DRIVERS);
                conn = DriverManager.getConnection(BD_URL, BD_USER, BD_PASSWORD);
                conn.setAutoCommit(false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }


    public static SessionFactory getHibernateSessionFactory() {

        if (sessionFactory == null) {

            try {
            // Свойства
            Properties properties = new Properties();
            properties.setProperty("hibernate.connection.driver_class",BD_DRIVERS );
            properties.setProperty("hibernate.connection.url",BD_URL);
            properties.setProperty("hibernate.connection.username",BD_USER);
            properties.setProperty("hibernate.connection.password",BD_PASSWORD);
            properties.setProperty("hibernate.dialect",DIALECT);
            // Конфигурация и класс
            Configuration configuration = new Configuration();
            configuration.setProperties(properties);
            configuration.addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
        return sessionFactory;
    }

//    public static Session getHibernateSessionFactory() {
//        return sessionFactory.openSession();
//    }


    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (HibernateException se) {
            se.printStackTrace();
        }

    }

}
