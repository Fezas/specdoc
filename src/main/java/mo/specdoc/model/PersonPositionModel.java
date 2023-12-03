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

    public static PersonPosition getActualPersonsFromPositionId(Long id) {
        Transaction transaction = null;
        PersonPosition result = new PersonPosition();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session
                    .createQuery("SELECT pp FROM PersonPosition pp " +
                            "WHERE pp.dateAddPosition = (SELECT max(r.dateAddPosition) FROM PersonPosition r WHERE r.position.id = :id) " +
                            "AND pp.position.id =:id", PersonPosition.class);
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

    public static List<PersonPosition> getFromIdPosition(long idPosition) {
        Transaction transaction = null;
        List<PersonPosition> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<PersonPosition> query = session.createQuery("SELECT a FROM PersonPosition a WHERE a.position.id =: id", PersonPosition.class);
            query.setParameter("id", idPosition);
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
