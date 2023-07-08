package persistence.repoDB;

import domain.Offer;
import org.hibernate.query.Query;
import persistence.IOffer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OfferRepo implements IOffer {

    private SessionFactory sessionFactory;

    public OfferRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Offer save(Offer offer) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(offer);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
        return offer;
    }

    @Override
    public Offer delete(Offer offer) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(offer);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
        return offer;
    }


    @Override
    public Iterable<Offer> findAll() {
        List<Offer> offers = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Offer";
            Query<Offer> q = session.createQuery(query, Offer.class);
            offers = q.getResultList();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return offers;
    }


    @Override
    public Offer findOne(Integer id) {
        // Not implemented yet
        return null;
    }

    public Offer findOfferByCriteria(String product_ids, String sales_agent_username, String client_username) {
        Offer offer = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Offer where product_ids = :product_ids and sales_agent_username = :sales_agent_username and client_username = :client_username";
            Query<Offer> q = session.createQuery(query, Offer.class);
            q.setParameter("product_ids", product_ids);
            q.setParameter("sales_agent_username", sales_agent_username);
            q.setParameter("client_username", client_username);
            offer = q.uniqueResult();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return offer;
    }

}

