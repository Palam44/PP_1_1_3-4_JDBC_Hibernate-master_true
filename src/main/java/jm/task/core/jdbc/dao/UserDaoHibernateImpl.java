package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory sessionFactory = Util.getHibernateSessionFactory();
    private Transaction transaction = null;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS user (Id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
                            + "name VARCHAR(45), lastname VARCHAR(45), age TINYINT)")
                    .executeUpdate();
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != transaction) {
                transaction.rollback();
            }
        }
    }


    @Override
    public void dropUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS user").executeUpdate();
            transaction.commit();
            System.out.println("Table has been deleted");
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
            System.out.println("User: " + name + " " + lastName + " has been added");
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (null != user) {
                session.delete(user);
            }
            transaction.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != transaction) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List list = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            list = session.createQuery("FROM " + User.class.getSimpleName()).list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("Truncate Table User").executeUpdate();
            transaction.commit();
            System.out.println("Table has been cleaned");
        } catch (HibernateException e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}