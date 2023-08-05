package com.aptech.project2.Controller;
import com.aptech.project2.DAO.OrderDao;
import com.aptech.project2.Model.Order;
import com.aptech.project2.Model.Product;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private AnchorPane ProductForm;

    @FXML
    private Button btnDetail;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Order, String> colunmDate;

    @FXML
    private TableColumn<Order, Double> colunmDiscount;

    @FXML
    private TableColumn<Order, String> colunmId;

    @FXML
    private TableColumn<Order, String> colunmStaff;

    @FXML
    private TableColumn<Order, Double> colunmSubTotal;

    @FXML
    private TableColumn<Order, Double> colunmTotal;

    @FXML
    private TableView<Order> tableOrders;

    @FXML
    private TextField txtValueSearch;

    private Alert alert;

    public void showTableOrders(){
        String keyword = txtValueSearch.getText();
        ObservableList<Order> orders = OrderDao.getInstance().getList(keyword);
        colunmId.setCellValueFactory(new PropertyValueFactory<Order, String>("id"));
        colunmDiscount.setCellValueFactory(new PropertyValueFactory<Order, Double>("discount"));
        colunmStaff.setCellValueFactory(Order->new SimpleStringProperty(Order.getValue().getStaff().getFullName()));
        colunmDate.setCellValueFactory(cellData -> {
            ObjectProperty<String> property = new SimpleObjectProperty<>();
            LocalDateTime createDate = cellData.getValue().getCreateDate();
            String dateString = createDate.toString();
            property.set(dateString);
            return property;
        });
        colunmTotal.setCellValueFactory(new PropertyValueFactory<Order, Double>("total"));
        colunmSubTotal.setCellValueFactory(new PropertyValueFactory<Order, Double>("subTotal"));
        tableOrders.setItems(orders);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showTableOrders();
        btnSearch.setOnAction(event -> {
            showTableOrders();
        });
        btnDetail.setOnAction(event -> {
            Order newSelection = tableOrders.getSelectionModel().getSelectedItem();
            if(newSelection==null){
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning message");
                alert.setContentText("Please choose 1 row on the orders table to show detail!");
                alert.showAndWait();
            }else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/aptech/project2/Orders/DetailForm.fxml"));
                try {
                    AnchorPane pane = loader.load();
                    OrderDetailController orderDetailController = loader.getController();
                    orderDetailController.setData(newSelection);
                    Stage dialogStage = new Stage();
                    Scene scene = new Scene(pane);
                    Stage stage = (Stage) btnDetail.getScene().getWindow();
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.initOwner(stage);
                    dialogStage.setScene(scene);
                    dialogStage.setTitle("Order Detail");
                    dialogStage.showAndWait();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}