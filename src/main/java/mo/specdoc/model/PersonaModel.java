/*
 * Copyright (c) 2023
 */

package mo.specdoc.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaModel {
    public static Persona getById(Long id) {
        Transaction transaction = null;
        Persona result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(Persona.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<Persona> getAll() {
        Transaction transaction = null;
        List<Persona> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM Persona a", Persona.class).getResultList();
            transaction.commit();
            for (Persona persona : result) {
                persona.getFamilyStringProperty().setValue(persona.getFamily());
                persona.getNameStringProperty().setValue(persona.getNamePerson());
                persona.getLastnameStringProperty().setValue(persona.getLastname());
                persona.getBirthdayObjectProperty().setValue(persona.getDateBirth());
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void saveOrUpdate(Persona persona) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(persona);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void delete(Persona persona) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(persona);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    //fail?
    public static List<Persona> getVacantPersons() {
        Transaction transaction = null;
        List<Persona> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Query<Persona> query = session.createQuery("SELECT p FROM Persona p WHERE p.id NOT IN (SELECT pp.id FROM PersonPosition pp)");
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
}
