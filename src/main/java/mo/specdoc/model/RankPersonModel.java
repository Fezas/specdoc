/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.RankPerson;
import mo.specdoc.entity.State;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RankPersonModel {
    public static RankPerson getById(Long id) {
        Transaction transaction = null;
        RankPerson result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(RankPerson.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static RankPerson getLastRankByIdPerson(Long idPersona) {
        Transaction transaction = null;
        RankPerson result = new RankPerson();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<RankPerson> query = session
                    .createQuery("SELECT rp FROM RankPerson rp " +
                    "WHERE rp.dateAddRank = (SELECT max(rp.dateAddRank) AS dr FROM RankPerson r) " +
                            "AND rp.persona.id =:id", RankPerson.class);
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

    public static List<RankPerson> getAllRanksByIdPerson(Long id) {
        Transaction transaction = null;
        List<RankPerson> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<RankPerson> query = session.createQuery("SELECT a FROM RankPerson a JOIN Rank b ON a.rank.id = b.id  WHERE a.persona.id =: id", RankPerson.class);
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

    public static void saveOrUpdate(RankPerson rankPerson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(rankPerson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void delete(RankPerson rankPerson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(rankPerson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
