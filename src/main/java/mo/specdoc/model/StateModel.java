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
    private static List<State> sortedListState(List<State> data) {
        List<State> result = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            for(int j = 0; j < data.size() - i - 1; j++) {
                if(data.get(j + 1).getSortValue() < data.get(j).getSortValue()) {
                    State swap = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, swap);
                }
            }
        }
        return data;
    }
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
        return sortedListState(result);
    }

    public static List<State> getFromTypeState(int id) {
        Transaction transaction = null;
        List<State> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<State> query = session
                    .createQuery("SELECT a FROM State a WHERE a.typeState = :id", State.class);
            query.setParameter("id", id);
            result = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return sortedListState(result);
    }

    public static List<State> getChildrenPosition(long id) {
        Transaction transaction = null;
        List<State> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<State> query = session.createQuery("SELECT a FROM State a WHERE a.parentIdState =: id", State.class);
            query.setParameter("id", id);
            result = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return sortedListState(result);
    }

    public static List<State> getAllChildrenStructure(State state) {
        List<State> result = new ArrayList<>();
        State root = getById(state.getIdState());
        List<State> childrens = getChildrenPosition(root.getIdState());
        if (!childrens.isEmpty()) {
            for (State s : childrens) {
                result.add(s);
                getAllChildrenStructure(s);
            }
        }
        return sortedListState(result);
    }

    public static State getRootState() {
        Transaction transaction = null;
        State result = new State();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<State> query = session.createQuery("SELECT a FROM State a WHERE a.parentIdState = null", State.class);
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
