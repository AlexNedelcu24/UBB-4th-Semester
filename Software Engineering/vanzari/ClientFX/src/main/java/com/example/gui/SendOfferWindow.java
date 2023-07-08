package com.example.gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SendOfferWindow implements Initializable, IObserver {

    private final ObservableList<Product> productModel = FXCollections.observableArrayList();
    private final ObservableList<Client> clientModel = FXCollections.observableArrayList();

    Client selectedClient;
    List<Product> selectedProducts;

    @FXML
    private Button logoutButton;
    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, String> clientsUsernameColumn;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, String> productsNameColumn;

    @FXML
    private TableColumn<Product, Float> productsPriceColumn;

    @FXML
    private TableColumn<Product, Integer> productsQuantityColumn;

    @FXML
    private Button sendOffersButton;

    @FXML
    private ListView<Product> selectedProductsList;

    private ObservableList<Product> selectedProductsModel = FXCollections.observableArrayList();


    private IServices server;




    private SalesAgent crtSalesAgent;

    public SendOfferWindow(){};

    public SendOfferWindow(IServices server){
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
        clientsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        productsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        clientsTable.setItems(clientModel);
        productsTable.setItems(productModel);

        selectedProductsList.setItems(selectedProductsModel);

    }

    public void loadClients() throws Exceptions {
        Iterable<Client> clients = server.findAllClients();

        List<Client> us = StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toList());
        clientModel.clear();
        clientModel.setAll(us);

    }

    public void loadProducts() throws Exceptions {
        Iterable<Product> products = server.findAllProducts();

        List<Product> us = StreamSupport.stream(products.spliterator(), false).collect(Collectors.toList());
        productModel.clear();
        productModel.setAll(us);

    }

    public void onSelectClient(javafx.scene.input.MouseEvent e){
        selectedClient = clientsTable.getSelectionModel().getSelectedItem();
    }

    public void onSelectProduct(javafx.scene.input.MouseEvent e){
        selectedProducts = productsTable.getSelectionModel().getSelectedItems();

        // Add the selected products to our new observable list
        selectedProductsModel.addAll(selectedProducts);
    }

    public void deselectProduct(javafx.scene.input.MouseEvent e){
        Product productToDeselect = selectedProductsList.getSelectionModel().getSelectedItem();

        // Remove the product from our selected products observable list
        selectedProductsModel.remove(productToDeselect);
    }



    @FXML
    public void onUserLogout(ActionEvent event) throws IOException {
        //close();
        Platform.runLater(() -> {
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });
    }


    public void onSendOffers(ActionEvent actionEvent) throws Exceptions {

        if (selectedClient == null) {

            return;
        }


        if (selectedProductsModel.isEmpty()) {

            return;
        }

        String productIds = selectedProductsModel.stream()
                .map(product -> product.getId().toString())
                .collect(Collectors.joining(","));

        server.saveOffer(productIds, crtSalesAgent.getUsername(), selectedClient.getUsername());

        Util.showWarning("","OFFER SENT.");
    }

    public void offerSent(Offer offer) throws Exceptions{
        Platform.runLater(()->{
            clientsTable.getItems().clear();
            try {
                this.loadClients();
            } catch (Exceptions e) {
                throw new RuntimeException(e);
            }
        });

            productsTable.getItems().clear();
            this.loadProducts();
    }

    @Override
    public void orderSent(Order order) throws Exceptions {

    }

    @Override
    public void updateProduct(Product product) throws Exceptions {
        Platform.runLater(()->{
            productsTable.getItems().clear();
            try {
                this.loadProducts();
            } catch (Exceptions e) {
                throw new RuntimeException(e);
            }
        });

    }

}

