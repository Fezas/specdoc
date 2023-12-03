/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.RankPerson;
import mo.specdoc.entity.SecrecyPerson;
import mo.specdoc.entity.WorkDay;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SecrecyPersonModel {
    public static SecrecyPerson getById(Long id) {
        Transaction transaction = null;
        SecrecyPerson result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(SecrecyPerson.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static SecrecyPerson getLastSecrecyByIdPerson(Long id) {
        Transaction transaction = null;
        SecrecyPerson result = new SecrecyPerson();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<SecrecyPerson> query = session.createQuery("SELECT a FROM SecrecyPerson a " +
                    "WHERE a.dateAddSecrecy = (SELECT max(r.dateAddSecrecy) FROM SecrecyPerson r WHERE r.persona.id = :id)" +
                    "AND a.persona.id =: id", SecrecyPerson.class);
            query.setParameter("id", id);
            result = query.getResultList().stream().findFirst().orElse(null);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<SecrecyPerson> getAllSecreciesByIdPerson(Long id) {
        Transaction transaction = null;
        List<SecrecyPerson> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<SecrecyPerson> query = session.createQuery("SELECT a FROM SecrecyPerson a JOIN SecrecyType b ON a.secrecyType.id = b.id  WHERE a.persona.id =:id", SecrecyPerson.class);
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

    public static void saveOrUpdate(SecrecyPerson secrecyPerson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(secrecyPerson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void delete(SecrecyPerson secrecyPerson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(secrecyPerson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
