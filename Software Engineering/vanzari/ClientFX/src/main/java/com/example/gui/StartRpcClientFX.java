package com.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.rpcprotocol.ServiceRpcProxy;
import services.IServices;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new ServiceRpcProxy(serverIP, serverPort);



        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();


        Login ctrl =
                loader.<Login>getController();
        ctrl.setServer(server);




        FXMLLoader loader2 = new FXMLLoader(
                getClass().getClassLoader().getResource("agent2.fxml"));
        Parent root2=loader2.load();


        Agent2 ctrl2 =
                loader2.<Agent2>getController();
        ctrl2.setServer(server);




        FXMLLoader loader3 = new FXMLLoader(
                getClass().getClassLoader().getResource("sendOffers.fxml"));
        Parent root3=loader3.load();

        SendOfferWindow ctrl3 =
                loader3.<SendOfferWindow>getController();
        ctrl3.setServer(server);



        FXMLLoader loaderMakeOrderWindow = new FXMLLoader(
                getClass().getClassLoader().getResource("makeOrders.fxml"));
        Parent rootMakeOrderWindow=loaderMakeOrderWindow.load();

        MakeOrderWindow ctrlMakeOrderWindow =
                loaderMakeOrderWindow.<MakeOrderWindow>getController();
        ctrlMakeOrderWindow.setServer(server);


        FXMLLoader loaderClientWindow = new FXMLLoader(
                getClass().getClassLoader().getResource("client.fxml"));
        Parent rootClientWindow=loaderClientWindow.load();

        ClientWindow ctrlClientWindow =
                loaderClientWindow.<ClientWindow>getController();
        ctrlClientWindow.setServer(server);



        ctrl.setAgent2(ctrl2);
        ctrl.setParent(root2);

        ctrl2.setSendOfferWindow(ctrl3);
        ctrl2.setParent(root3);

        ctrl2.setMakeOrderWindow(ctrlMakeOrderWindow);
        ctrl2.setParentOrder(rootMakeOrderWindow);

        ctrl.setClient(ctrlClientWindow);
        ctrl.setParentClient(rootClientWindow);

        //primaryStage.setTitle("ISS");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();




    }


}


