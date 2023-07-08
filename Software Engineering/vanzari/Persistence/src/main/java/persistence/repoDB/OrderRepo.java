package persistence.repoDB;

import domain.Offer;
import domain.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import persistence.IOrder;

import java.util.List;

public class OrderRepo implements IOrder {
    private SessionFactory sessionFactory;

    public OrderRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order save(Order order) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(order);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null) {
                tx.rollback();
            }
            throw ex;
        }
        return order;
    }

    @Override
    public Order delete(Order order) {
        // Not implemented yet
        return null;
    }

    @Override
    public Iterable<Order> findAll() {
        List<Order> orders = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Order";
            Query<Order> q = session.createQuery(query, Order.class);
            orders = q.getResultList();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return orders;
    }


    @Override
    public Order findOne(Integer id) {
        // Not implemented yet
        return null;
    }
}
