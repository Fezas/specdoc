/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.State;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class StateModel {
    public static State getById(Long id) {
        Transaction transaction = null;
        State result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(State.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<State> getAllRecords() {
        Transaction transaction = null;
        List<State> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM State a", State.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<State> getAllPersonsWithLastDatePositionInSubdiv(long idSubdivision) {
        Transaction transaction = null;
        List<State> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<State> query = session
                    .createQuery("SELECT a FROM State a " +
                            "WHERE a.dateAddPosition = (SELECT max(a.dateAddPosition) FROM State) " +
                            "AND a.idSubdivision = :id", State.class);
            query.setParameter("id", idSubdivision);
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

    public static State getLastPositionByPersonId(Long idPersona) {
        Transaction transaction = null;
        State result = new State();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<State> query = session
                    .createQuery("SELECT a FROM State a " +
                            "WHERE a.dateAddPosition = (SELECT max(a.dateAddPosition) FROM State) " +
                            "AND a.idPersona = :id", State.class);
            query.setParameter("id", idPersona);
            result = query.getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<State> getAllPositionByPersonId(Long idPersona) {
        Transaction transaction = null;
        List<State> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT s FROM State s " +
                    "WHERE s.idPersona = '" + idPersona + "'", State.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void saveOrUpdate(State entity) {
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

    public static void delete(State entity) {
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
