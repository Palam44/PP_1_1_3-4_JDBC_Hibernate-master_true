package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {

        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();
            String sqlUpdate = """
                    CREATE TABLE IF NOT EXISTS `new_schema`.`user` (
                      `Id` BIGINT NOT NULL AUTO_INCREMENT,
                      `Name` VARCHAR(45) NOT NULL,
                      `LastName` VARCHAR(45) NOT NULL,
                      `Age` TINYINT NOT NULL,
                      PRIMARY KEY (`Id`));""";
            PreparedStatement stmt = Util.getJBDCConnection().prepareStatement(sqlUpdate);
            stmt.executeUpdate();

            con.commit();
        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {

        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();

            String sqlUpdate = "DROP TABLE user";
            PreparedStatement stmt = con.prepareStatement(sqlUpdate);
            stmt.executeUpdate();

            con.commit();
        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveUser(String name, String lastName, byte age) {

        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();
            String sqlUpdate = "INSERT INTO user (Name, LastName, Age) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sqlUpdate);

            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setByte(3, age);

            stmt.executeUpdate();
            con.commit();
        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {

        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();

            String sqlUpdate = "DELETE FROM user WHERE Id = ?" ;
            PreparedStatement stmt = con.prepareStatement(sqlUpdate);
            stmt.setLong(1,id);
            stmt.executeUpdate();

            con.commit();
        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();

        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();

            Statement stmt = con.createStatement();
            savePoint = con.setSavepoint();
            String sqlQuery = "SELECT * FROM user";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            while (rs.next()) {
                Long id = rs.getLong("Id");
                String name = rs.getString("Name");
                String lastName = rs.getString("LastName");
                Byte age = rs.getByte("Age");

                User user = new User(name,lastName,age);
                user.setId(id);
                userList.add(user);
            }

            rs.close();
            con.commit();

        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return userList;
    }

    public void cleanUsersTable() {
        Connection con = null;
        Savepoint savePoint = null;
        try {
            con = Util.getJBDCConnection();
            savePoint = con.setSavepoint();
            String sqlUpdate = "Truncate table user";
            PreparedStatement stmt = con.prepareStatement(sqlUpdate);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException se) {
            se.printStackTrace();
            try {
                con.rollback(savePoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}