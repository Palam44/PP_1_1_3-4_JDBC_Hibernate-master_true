package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // реализуйте алгоритм здесь
        UserDaoJDBCImpl userDao = new UserDaoJDBCImpl();

        userDao.createUsersTable();

        userDao.saveUser("Антон", "Картон", (byte) 33);
        userDao.saveUser("Богдан", "Фонтан", (byte) 29);
        userDao.saveUser("Кирилл", "Курилл", (byte) 17);
        userDao.saveUser("Наташа", "Три_Рубля_И_Наша", (byte) 22);

        userDao.removeUserById(2);

        userDao.getAllUsers();

        userDao.cleanUsersTable();

        userDao.dropUsersTable();
    }
}
