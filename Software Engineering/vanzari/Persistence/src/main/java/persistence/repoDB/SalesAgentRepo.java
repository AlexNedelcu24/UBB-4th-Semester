package persistence.repoDB;

import domain.SalesAgent;
import persistence.ISalesAgent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SalesAgentRepo implements ISalesAgent {

    private SessionFactory sessionFactory;

    public SalesAgentRepo(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public SalesAgent save(SalesAgent user) {

        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }

        }

        return user;

    }

    @Override
    public SalesAgent delete(SalesAgent entity) {

        return null;
    }

    @Override
    public Iterable<SalesAgent> findAll() {

        return null;
    }

    @Override
    public SalesAgent findOne(Integer integer) {

        return null;
    }

    @Override
    public SalesAgent findSalesAgent(String username, String password) {

        SalesAgent user = null;
        try (Session session = sessionFactory.openSession()) {

            String query = "from SalesAgent where username = :username and password = :password";
            user = session.createQuery(query, SalesAgent.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return user;
    }
}

