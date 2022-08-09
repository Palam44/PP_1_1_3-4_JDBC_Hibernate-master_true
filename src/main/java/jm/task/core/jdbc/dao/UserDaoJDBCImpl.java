package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Обработка всех исключений, связанных с работой с базой данных должна находиться в dao
public class UserDaoJDBCImpl implements UserDao {
    private final Connection connector;
    public UserDaoJDBCImpl() throws SQLException, ClassNotFoundException {
        this.connector = Util.getConnection();
    }

    // Создание таблицы для User(ов) - не должна приводить к исключению, если такая таблица уже существует
    public void createUsersTable() {
        String s = "CREATE TABLE IF NOT EXISTS users (id mediumint NOT NULL auto_increment, name varchar(50) NOT NULL, lastName varchar(60) NOT NULL, age tinyint, primary key(id))";
        try (Connection connection = Util.getConnection();) {
            try (Statement statement = connection.createStatement();) {
                connection.setAutoCommit(false);
                statement.executeUpdate(s);
                System.out.println("Table was created");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    // Удаление таблицы User(ов) - не должна приводить к исключению, если таблицы не существует
    public void dropUsersTable() {
        String s = "DROP TABLE if exists users";

        try (Connection connection = Util.getConnection();) {
            try (Statement statement = connection.createStatement();) {
                statement.executeUpdate(s);
                System.out.println("Table was deleted");
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    // Добавление User в таблицу
    public void saveUser(String name, String lastName, byte age) {
        String s = "insert into users (name, lastName, age) values (?, ?, ?)";
        try (Connection connection = Util.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(s);) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                System.out.println("Пользователь: " + name + " добавлен в базу данных");
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    // Удаление User из таблицы (по id)
    public void removeUserById(long id) {
        String s = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(s);) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.println("Пользователь: " + id + " удален из БД");
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    // Получение всех User(ов) из таблицы
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String SQL = "select * from users";
        try (Statement statement = connector.createStatement()) {
            ResultSet set = statement.executeQuery(SQL);
            while (set.next()) {
                User user = new User();
                user.setId(set.getLong(1));
                user.setName(set.getString(2));
                user.setLastName(set.getString(3));
                user.setAge(set.getByte(4));
                users.add(user);
            }
            connector.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    // Очистка содержания таблицы
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();) {
            try (Statement statement = connection.createStatement();) {
                statement.executeUpdate("Truncate Table Users");
                System.out.println("Table deleted");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
