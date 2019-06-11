package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import models.GameModel;
import models.Model;


import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private Model gameModel;

    @FXML
    public Button button;

    @FXML
    public Button bOrder;

    @FXML
    public ComboBox comboxAction;

    @FXML
    public ComboBox comboxProv1;

    @FXML
    public ComboBox comboxProv2;

    @FXML
    public ListView lvOrders;

    @FXML
    public TextField tfMessage; // Value injected by FXMLLoader

    @FXML
    public TextArea taUpdates; // Value injected by FXMLLoader

    @FXML
    public void clickedAddOrder() {
        System.out.println("ADD ORDER YUY");
        System.out.println("clicked AddOrder!");
        try {
            String action = comboxAction.getValue().toString();
            String prov1 = comboxProv1.getValue().toString();
            String prov2 = comboxProv2.getValue().toString();
            String order = action + "_" + prov1 + "_" + prov2;

            lvOrders.getItems().add(order);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        comboxAction.getItems().addAll("Move", "Support", "Embark", "Hold");
        comboxProv1.getItems().addAll("Province1a", "Province1b", "Province1c", "Province1d", "Province1e");
        comboxProv2.getItems().addAll("Province2a", "Province2b", "Province2c", "Province2d", "Province2e");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("clicked Send!");
                tfMessage.setText("It works!!!");
            }
        });
//        bOrder.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent event) {
//                System.out.println("clicked AddOrder!");
//                try {
//                    String action = comboxAction.getValue().toString();
//                    String prov1 = comboxProv1.getValue().toString();
//                    String prov2 = comboxProv2.getValue().toString();
//                    String order = action + "_" + prov1 + "_" + prov2;
//
//                    lvOrders.getItems().add(order);
//                } catch (Exception e) {
//                    //e.printStackTrace();
//                }
//            }
//        });
        lvOrders.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(final KeyEvent keyEvent )
            {
                if (lvOrders.getSelectionModel().getSelectedItem() != null )
                {
                    if (keyEvent.getCode().equals(KeyCode.DELETE ) )
                    {
                        lvOrders.getItems().remove(lvOrders.getSelectionModel().getSelectedItem());
                    }
                }
            }
        } );
    }
}