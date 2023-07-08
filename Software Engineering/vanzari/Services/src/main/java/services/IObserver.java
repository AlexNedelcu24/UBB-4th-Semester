package services;


import domain.Offer;
import domain.Order;
import domain.Product;

public interface IObserver {
        void offerSent(Offer offer) throws Exceptions;

        void orderSent(Order order) throws  Exceptions;

        void updateProduct(Product product) throws Exceptions;
}

