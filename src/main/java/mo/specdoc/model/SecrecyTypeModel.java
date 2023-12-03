/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import mo.specdoc.entity.SecrecyType;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SecrecyTypeModel {
    public static SecrecyType getById(Long id) {
        Transaction transaction = null;
        SecrecyType result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(SecrecyType.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }            e.printStackTrace();
        }
        return result;
    }

    public static List<SecrecyType> getAllRecords() {
        Transaction transaction = null;
        List<SecrecyType> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM SecrecyType a", SecrecyType.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }
}
