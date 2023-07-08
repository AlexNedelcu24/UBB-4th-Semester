package com.example.gui;

import domain.Client;
import domain.SalesAgent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.Exceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.IServices;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    public Login(){

    }
    private IServices server;

    private Agent2 agent2Controller;

    private ClientWindow clientController;

    private SalesAgent crtSalesAgent;

    private Client crtClient;

    Parent mainChatParent;

    Parent parentClient;

    private String currentUserType = "";

    @FXML
    private Button login;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] users = {"sales agent","admin","client"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        choiceBox.getItems().addAll(users);
        choiceBox.setOnAction(this::getUserType);
    }

    public void setServer(IServices s){
        server=s;
    }

    public void setParent(Parent p){
        mainChatParent=p;
    }

    public void setParentClient(Parent p){
        parentClient=p;
    }

    public void setAgent2(Agent2 agent2Controller){ this.agent2Controller = agent2Controller;}

    public void setClient(ClientWindow clientController){
        this.clientController = clientController;
    }

    public void getUserType(ActionEvent a){
        currentUserType = choiceBox.getValue();
    }

    public void userLogin(ActionEvent event) throws IOException {
        checkLogin(event);
    }

    private void checkLogin(ActionEvent actionEvent) throws IOException{

        if(username.getText().isEmpty() || password.getText().isEmpty()){
            Util.showWarning("","Please enter your data.");
        }
        else {
            try {
                if(currentUserType == "sales agent"){
                    crtSalesAgent = new SalesAgent(username.getText(), password.getText());
                    server.loginSA(crtSalesAgent,agent2Controller);

                    Stage stage=new Stage();
                    stage.setTitle("Agent options Window for " +crtSalesAgent.getUsername());
                    stage.setScene(new Scene(mainChatParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            agent2Controller.logout();
                            System.exit(0);
                        }
                    });

                    stage.show();
                    agent2Controller.setUser(crtSalesAgent);
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }

                if(currentUserType == "client"){
                    crtClient = new Client(username.getText(), password.getText());
                    server.loginCL(crtClient,clientController);

                    Stage stage=new Stage();
                    stage.setTitle("Client Window for " +crtClient.getUsername());
                    stage.setScene(new Scene(parentClient));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            clientController.logout();
                            System.exit(0);
                        }
                    });

                    stage.show();
                    clientController.setUser(crtClient);
                    clientController.loadOffers();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }


            } catch (IllegalArgumentException e) {
                Util.showWarning("","Wrong username or password!");
            } catch (Exceptions e) {
                Util.showWarning("","Wrong username or password!");
                //throw new RuntimeException(e);
            }
        }


    }


}
