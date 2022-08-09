package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соединения с БД
    private static final String url = "jdbc:mysql://localhost:3306/mysql";
    private static final String username = "root";
    private static final String password = "DANEKpalam55";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            System.out.println("Successfully connected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().addAnnotatedClass(User.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }
}