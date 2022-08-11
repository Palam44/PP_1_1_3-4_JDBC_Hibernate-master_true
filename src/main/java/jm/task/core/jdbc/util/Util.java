package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
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

        if (sessionFactory == null || !sessionFactory.isOpen()) {
            try {
                Configuration config = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, BD_DRIVERS);
                settings.put(Environment.URL, BD_URL);
                settings.put(Environment.USER, BD_USER);
                settings.put(Environment.PASS, BD_PASSWORD);

                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");
//                settings.put(Environment.HBM2DDL_AUTO, "create-drop");

                config.setProperties(settings);
                config.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

                sessionFactory = config.buildSessionFactory(serviceRegistry);
            } catch (Exception se) {
                se.printStackTrace();
            }
        }
        return sessionFactory;
    }

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
