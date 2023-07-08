package persistence.repoDB;

import domain.Client;
import domain.SalesAgent;
import persistence.IClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ClientRepo implements IClient {

    private SessionFactory sessionFactory;

    public ClientRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Client save(Client client) {
        // Not implemented yet
        return null;
    }

    @Override
    public Client delete(Client client) {
        // Not implemented yet
        return null;
    }

    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Client";
            Query<Client> q = session.createQuery(query, Client.class);
            clients = q.getResultList();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return clients;
    }

    @Override
    public Client findOne(Integer id) {
        // Not implemented yet
        return null;
    }

    @Override
    public Client findClient(String username, String password) {

        Client user = null;
        try (Session session = sessionFactory.openSession()) {

            String query = "from Client where username = :username and password = :password";
            user = session.createQuery(query, Client.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return user;
    }
}

