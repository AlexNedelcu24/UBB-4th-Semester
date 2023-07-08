package persistence;

import domain.Product;

public interface IProduct extends ICrudRepository<Integer, Product> {

    Product update(Product product);
}
