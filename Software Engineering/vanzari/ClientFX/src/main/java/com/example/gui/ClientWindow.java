package com.example.gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientWindow implements Initializable, IObserver {

    public ClientWindow(){}

    private IServices server;

    private final ObservableList<String> offerModel = FXCollections.observableArrayList();

    Offer selectedOffer;

    @FXML
    private Button logoutButton;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<String> offersList;

    @FXML
    private Button sendOrderButton;

    private Client crtClient;

    public ClientWindow(IServices server){
        this.server = server;
    }

    public void setServer(IServices s) {
        server = s;
    }

    public void setUser(Client user) {
        this.crtClient = user;
    }

    @FXML
    public void initialize(URL arg0, ResourceBundle arg1) {

        offersList.setItems(offerModel);

    }

    public void loadOffers() throws Exceptions {
        Iterable<Offer> offers = server.findAllOffers();

        List<String> offerNames = new ArrayList<>();
        for (Offer offer : offers) {
            String productNames = Arrays.stream(offer.getProductIds().split(","))
                    .map(idStr -> {
                        try {
                            int id = Integer.parseInt(idStr);
                            Product product = server.findProduct(id);
                            return product != null ? product.getName() : "Unknown Product";
                        } catch (NumberFormatException | Exceptions ex) {
                            return "Invalid ID";
                        }
                    })
                    .collect(Collectors.joining(", "));
            offerNames.add(productNames);
        }

        offerModel.clear();
        offerModel.setAll(offerNames);
    }

    @FXML
    public void onUserLogout(ActionEvent event) throws IOException {
        logout();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    void logout() {
        try {
            server.logoutCL(crtClient, this);
        } catch (Exceptions e) {
            System.out.println("Logout error " + e);
        }

    }

    public void onSendOrder(ActionEvent actionEvent) throws IOException, Exceptions {
        String selectedOffer = offersList.getSelectionModel().getSelectedItem();
        String message = textArea.getText();


        // Finding the corresponding Offer object
        Offer correspondingOffer = null;
        for (Offer offer : server.findAllOffers()) {
            String productNames = Arrays.stream(offer.getProductIds().split(","))
                    .map(idStr -> {
                        try {
                            int id = Integer.parseInt(idStr);
                            Product product = server.findProduct(id);
                            return product != null ? product.getName() : "Unknown Product";
                        } catch (NumberFormatException | Exceptions ex) {
                            return "Invalid ID";
                        }
                    })
                    .collect(Collectors.joining(", "));
            if (productNames.equals(selectedOffer)) {
                correspondingOffer = offer;
                break;
            }
        }

        if (correspondingOffer != null) {
            // Creating and sending the Order object
            Order order = new Order(correspondingOffer.getProductIds(), correspondingOffer.getSalesAgentUsername(), correspondingOffer.getClientUsername(), message);
            server.saveOrder(order.getProductIds(), order.getSalesAgentUsername(), order.getClientUsername(), order.getMessage());

            Util.showWarning("","Order Sent.");
        } else {
            // Handle the case where no corresponding Offer object is found
        }
    }


    @Override
    public void offerSent(Offer offer) throws Exceptions {
        Platform.runLater(() -> {
            offersList.getItems().clear();
            try {
                this.loadOffers();
            } catch (Exceptions e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Override
    public void orderSent(Order order) throws Exceptions {
        // Saving the order
       // server.saveOrder(order.getProductIds(), order.getSalesAgentUsername(), order.getClientUsername(), order.getMessage());

        // Updating the offers list for all clients
        Platform.runLater(() -> {
            offersList.getItems().clear();
            try {
                this.loadOffers();
            } catch (Exceptions e) {
                throw new RuntimeException(e);
            }

        });


    }

    @Override
    public void updateProduct(Product product) throws Exceptions {

    }

}
