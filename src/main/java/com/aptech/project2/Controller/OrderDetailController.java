package com.aptech.project2.Controller;

import com.aptech.project2.DAO.OrderDetailDao;
import com.aptech.project2.Model.Order;
import com.aptech.project2.Model.OrderDetail;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class OrderDetailController implements Initializable {

    @FXML
    private AnchorPane ProductForm;
    @FXML
    private Label txtDiscount;

    @FXML
    private TableColumn<OrderDetail, Integer> colunmId;

    @FXML
    private TableColumn<OrderDetail, Double> colunmPrice;

    @FXML
    private TableColumn<OrderDetail, String> colunmProduct;

    @FXML
    private TableColumn<OrderDetail, Integer> colunmQuantity;

    @FXML
    private TableView<OrderDetail> tableOrders;

    @FXML
    private Label txtDate;

    @FXML
    private Label txtId;

    @FXML
    private Label txtStaff;

    @FXML
    private Label txtTotal;
    private ObservableList<OrderDetail> orderDetails;
    public void setData(Order order){
        txtId.setText(order.getId());
        txtStaff.setText(order.getStaff().getFullName());
        txtDate.setText(order.getCreateDate().toString());
        txtDiscount.setText(String.valueOf(order.getDiscount())+"$");
        txtTotal.setText(String.valueOf(order.getTotal())+"$");
        orderDetails = OrderDetailDao.getInstance().findByOrderId(order);
        colunmId.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return javafx.beans.binding.Bindings.createObjectBinding(() -> rowIndex + 1);
        });
        colunmPrice.setCellValueFactory(new PropertyValueFactory<OrderDetail, Double>("price"));
        colunmQuantity.setCellValueFactory(new PropertyValueFactory<OrderDetail, Integer>("quantity"));
        colunmProduct.setCellValueFactory(OrderDetail->new SimpleStringProperty(OrderDetail.getValue().getProduct().getName()));
        tableOrders.setItems(orderDetails);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
