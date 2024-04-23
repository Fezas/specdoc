/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DopuskModel {
    public static Dopusk getById(Long id) {
        Transaction transaction = null;
        Dopusk result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(Dopusk.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<Dopusk> getAllRecords() {
        Transaction transaction = null;
        List<Dopusk> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM Dopusk a", Dopusk.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<Dopusk> getByIdPersona(Long id) {
        Transaction transaction = null;
        List<Dopusk> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<Dopusk> query = session.createQuery("SELECT a FROM Dopusk a WHERE a.persona.id =: id", Dopusk.class);
            query.setParameter("id", id);
            result = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }


    public static List<Dopusk> getAllByStatePostId(Long id) {
        Transaction transaction = null;
        List<Dopusk> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<Dopusk> query = session.createQuery("SELECT a FROM Dopusk a WHERE a.state.id =: id", Dopusk.class);
            query.setParameter("id", id);
            result = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void saveOrUpdate(Dopusk entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void delete(Dopusk entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
