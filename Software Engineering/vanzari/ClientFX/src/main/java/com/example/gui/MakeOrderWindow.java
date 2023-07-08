package com.example.gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MakeOrderWindow implements Initializable, IObserver {

    private final ObservableList<Product> productModel = FXCollections.observableArrayList();
    private final ObservableList<Order> orderModel = FXCollections.observableArrayList();

    @FXML
    private Button logoutButton;

    @FXML
    private TextField quantities;

    @FXML
    private ListView<Product> productList;

    @FXML
    private ListView<Order> orderList;

    private IServices server;

    private SalesAgent crtSalesAgent;

    public MakeOrderWindow(){};

    public MakeOrderWindow(IServices server){
        this.server = server;
    };

    public void setServer(IServices s) {
        server = s;
    }

    public void setUser(SalesAgent user) {
        this.crtSalesAgent = user;
    }

    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {

        productList.setItems(productModel);
        orderList.setItems(orderModel);

    }

    public void loadProducts() throws Exceptions {
        Iterable<Product> products = server.findAllProducts();

        List<Product> us = StreamSupport.stream(products.spliterator(), false).collect(Collectors.toList());
        productModel.clear();
        productModel.setAll(us);

    }

    public void loadOrders() throws Exceptions {
        Iterable<Order> orders = server.findAllOrders();

        List<Order> us = StreamSupport.stream(orders.spliterator(), false).collect(Collectors.toList());
        orderModel.clear();
        orderModel.setAll(us);

    }

    @FXML
    public void onUserLogout(ActionEvent event) throws IOException {
        //close();
        Platform.runLater(() -> {
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });
    }

    @FXML
    public void onPlaceOrder(ActionEvent actionEvent) throws Exceptions {
        // Get selected order
        Order selectedOrder = orderList.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // Parse productIds and quantities from Order and TextField
            String[] productIdsStr = selectedOrder.getProductIds().split(",");
            String[] quantitiesStr = quantities.getText().split(",");
            if (quantitiesStr.length != productIdsStr.length) {
                Util.showWarning("","Numarul de cantitati introduse nu corespunde cu numarul de produse din comanda.");
                throw new Exceptions("Numarul de cantitati introduse nu corespunde cu numarul de produse din comanda.");
            }

            int[] productIds = new int[productIdsStr.length];
            int[] quantities = new int[quantitiesStr.length];
            for (int i = 0; i < quantitiesStr.length; i++) {
                productIds[i] = Integer.parseInt(productIdsStr[i]);
                quantities[i] = Integer.parseInt(quantitiesStr[i]);
            }

            // Check if there are enough products and prepare for update
            List<Product> productsToUpdate = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                Product product = server.findProduct(productIds[i]);
                if (product.getQuantity() < quantities[i]) {
                    Util.showWarning("","Produsul cu id " + product.getId() + " nu are suficiente unitati in stoc.");
                    throw new Exceptions("Produsul cu id " + product.getId() + " nu are suficiente unitati in stoc.");
                } else {
                    product.setQuantity(product.getQuantity() - quantities[i]); // prepare for update
                    productsToUpdate.add(product);
                }
            }

            // If there are enough products, update the quantity for each product in the database
            for (Product product : productsToUpdate) {
                server.updateProduct(product, product.getQuantity());
            }

            loadOrders();
            loadProducts();
            Util.showWarning("","Order Placed");
        } else {
            Util.showWarning("","Nu a fost selectata nicio comanda.");
            throw new Exceptions("Nu a fost selectata nicio comanda.");
        }
    }



    @Override
    public void orderSent(Order order) throws Exceptions {}

    @Override
    public void updateProduct(Product product) throws Exceptions {
        Platform.runLater(() -> {
            productList.getItems().clear();
            try {
                this.loadProducts();
            } catch (Exceptions e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Override
    public void offerSent(Offer offer) throws Exceptions{}


}
