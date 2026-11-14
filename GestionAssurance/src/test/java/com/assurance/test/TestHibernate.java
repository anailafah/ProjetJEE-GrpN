package com.assurance.test;

import com.assurance.util.HibernateUtil;
import org.hibernate.Session;

public class TestHibernate {

    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            session.doWork(connection -> {
                System.out.println("Connexion MySQL réussie !");
                System.out.println("Base de données : " + connection.getCatalog());
            });

        } catch (Exception e) {

            System.out.println("Erreur de connexion Hibernate !");
            e.printStackTrace();

        } finally {
            HibernateUtil.shutdown();
        }
    }
}