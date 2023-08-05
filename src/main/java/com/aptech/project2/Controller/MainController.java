package com.aptech.project2.Controller;

import com.aptech.project2.DAO.StaffDAO;
import com.aptech.project2.Model.FxmlLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnMenuOrder;

    @FXML
    private Button btnMenuCategory;

    @FXML
    private Button btnMenuDashBroad;

    @FXML
    private Button btnMenuProduct;

    @FXML
    private Button btnMenuPurchase;

    @FXML
    private Button btnMenuStaff;

    @FXML
    private SplitPane girdMenu;

    @FXML
    private Label txtAdminName;


    private Alert alert;


    public void Logout(ActionEvent event){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error Message");
        alert.setContentText("Are you sure want to logout?");
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.get()==ButtonType.OK){
            btnLogout.getScene().getWindow().hide();
            Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Login/Login.fxml");
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
    }
   public void ActionExit (ActionEvent event){
       Platform.exit();
       System.exit(0);
   }
    public void displayUserName(){
        String UserFullName = StaffDAO.staffSession.getFullName();
        txtAdminName.setText( StaffDAO.staffSession.getRole().toUpperCase()+" "+UserFullName);
    }

    public void displayByRole(){
        if(StaffDAO.staffSession.getRole().equals("staff")){
            btnMenuCategory.setVisible(false);
            btnMenuProduct.setVisible(false);
            btnMenuStaff.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUserName();
        displayByRole();
        displayDashBroad();
        btnMenuProduct.setOnAction(event -> {
            displayProduct();
        });
        btnMenuStaff.setOnAction(event -> {
            displayStaff();
        });
        btnMenuCategory.setOnAction(event -> {
            displayCategory();
        });
        btnMenuPurchase.setOnAction(event -> {
            displayPurchase();
        });
        btnMenuDashBroad.setOnAction(event -> {
            displayDashBroad();
        });
        btnMenuOrder.setOnAction(event -> {
            displayOrders();
        });
    }

    public void displayProduct(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Product/Index.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }
    public void displayDashBroad(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Menu/DashBroad.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }
    public void displayCategory(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Category/Index.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }
    public void displayStaff(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Staff/Index.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }
    public void displayPurchase(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Purchase/Purchase.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }
    public void displayOrders(){
        Parent root = FxmlLoader.getInstance().getViewPane("/com/aptech/project2/Orders/Orders.fxml");
        Scene scene = new Scene(root);
        girdMenu.getItems().set(1, scene.getRoot());
    }

}