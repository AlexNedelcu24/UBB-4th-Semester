package persistence.repoDB;

import domain.Product;
import org.hibernate.Transaction;
import persistence.IProduct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ProductRepo implements IProduct {

    private SessionFactory sessionFactory;

    public ProductRepo(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Product save(Product product) {
        // Not implemented yet
        return null;
    }

    @Override
    public Product delete(Product product) {
        // Not implemented yet
        return null;
    }

    @Override
    public Iterable<Product> findAll() {
        List<Product> products = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Product";
            Query<Product> q = session.createQuery(query, Product.class);
            products = q.getResultList();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return products;
    }

    @Override
    public Product findOne(Integer id) {
        Product product = null;
        try (Session session = sessionFactory.openSession()) {
            String query = "from Product where id = :id";
            product = session.createQuery(query, Product.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (RuntimeException ex) {
            throw ex;
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(product);
                transaction.commit();
                return product;
            } catch (RuntimeException ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw ex;
            }
        }
    }


}

