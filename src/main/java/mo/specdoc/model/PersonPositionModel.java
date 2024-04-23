/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.*;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class PersonPositionModel {
    public static PersonPosition getById(Long id) {
        Transaction transaction = null;
        PersonPosition result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(PersonPosition.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static PersonPosition getLastPositionByIdPerson(Long idPersona) {
        Transaction transaction = null;
        PersonPosition result = new PersonPosition();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session
                    .createQuery("SELECT pp FROM PersonPosition pp " +
                            "WHERE pp.dateAddPosition = (SELECT max(r.dateAddPosition) FROM PersonPosition r WHERE r.personaFromPosition.id = :id) " +
                            "AND pp.personaFromPosition.id =:id", PersonPosition.class);
            query.setParameter("id", idPersona);
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
    //добавить дату освобождения с должности
    public static List<PersonPosition> getAllActualPersons() {
        Transaction transaction = null;
        List<PersonPosition> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session
                    .createQuery("SELECT pp FROM PersonPosition pp " +
                            "WHERE pp.dateRemovePosition is null", PersonPosition.class);
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

    public static PersonPosition getActualPersonsFromPositionId(Long id) {
        Transaction transaction = null;
        PersonPosition result = new PersonPosition();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session
                    .createQuery("SELECT pp FROM PersonPosition pp " +
                            "WHERE pp.dateAddPosition = (SELECT max(r.dateAddPosition) FROM PersonPosition r WHERE r.state.id = :id) " +
                            "AND pp.state.id =:id", PersonPosition.class);
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

    public static List<PersonPosition> getFromIdPosition(long idState) {
        Transaction transaction = null;
        List<PersonPosition> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session.createQuery("SELECT a FROM PersonPosition a WHERE a.state.id =: id", PersonPosition.class);
            query.setParameter("id", idState);
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

    public static List<PersonPosition> getAllPosition() {
        Transaction transaction = null;
        List<PersonPosition> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query
                    = session.createQuery("SELECT a FROM PersonPosition a WHERE a.personaFromPosition.id=: id",
                    PersonPosition.class);
            query.setParameter("id", 4);
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

    public static List<PersonPosition> getAllFromIdPersona(long idPersona) {
        Transaction transaction = null;
        List<PersonPosition> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session.createQuery("SELECT a FROM PersonPosition a WHERE a.personaFromPosition.id=: id", PersonPosition.class);
            query.setParameter("id", idPersona);
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

    public static void saveOrUpdate(PersonPosition personPosition) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(personPosition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void delete(PersonPosition personPosition) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(personPosition);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
