package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();
            String sqlUpdate = """
                    CREATE TABLE IF NOT EXISTS `new_schema`.`user` (
                      `Id` BIGINT NOT NULL AUTO_INCREMENT,
                      `Name` VARCHAR(45) NOT NULL,
                      `LastName` VARCHAR(45) NOT NULL,
                      `Age` TINYINT NOT NULL,
                      PRIMARY KEY (`Id`));""";
            session.createSQLQuery(sqlUpdate).executeUpdate();

            transaction.commit();
            System.out.println("Table has been created");

        } catch (Exception qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        } finally {
            assert session != null;
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();
            String sqlUpdate = "DROP TABLE IF EXISTS user";
            session.createSQLQuery(sqlUpdate).executeUpdate();
            transaction.commit();
            System.out.println("Table has been deleted");

        } catch (HibernateException qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        } finally {
            assert session != null;
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User: " + name + " " + lastName + " has been added");

        } catch (HibernateException qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();

        } finally {
            assert session != null;
            session.close();
        }

    }

    @Override
    public void removeUserById(long id) {

        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete User where id = " + id).executeUpdate();
            transaction.commit();
            System.out.println("User id" + id + " has been deleted");

        } catch (HibernateException qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        } finally {
            assert session != null;
            session.close();
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();
            String HQLQuery = "FROM User";;
            users = session.createQuery(HQLQuery).list();
            transaction.commit();

        } catch (Exception qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        } finally {
            assert session != null;
            session.close();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = Util.getHibernateSessionFactory().openSession();
            transaction = session.beginTransaction();
            String SQLUpdate = "Truncate table user";
            session.createSQLQuery(SQLUpdate).executeUpdate();
            transaction.commit();
            System.out.println("Table has been cleaned");

        } catch (Exception qe) {
            qe.printStackTrace();
            assert transaction != null;
            transaction.rollback();
        } finally {
            assert session != null;
            session.close();
        }
    }
}