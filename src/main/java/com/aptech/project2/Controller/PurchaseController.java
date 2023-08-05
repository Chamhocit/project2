package com.aptech.project2.Controller;

import com.aptech.project2.DAO.*;
import com.aptech.project2.Model.Card;
import com.aptech.project2.Model.Order;
import com.aptech.project2.Model.OrderDetail;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Validation.Validate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    public static PurchaseController getInstance(){
        return new PurchaseController();
    }
    @FXML
    private Button btnPutOut;
    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnRemove;

    @FXML
    private GridPane MenuProGird;

    @FXML
    private TableView<Card> TableProduct;

    @FXML
    private Button btnPay;

    @FXML
    private Button btnReceipt;

    @FXML
    private Button getBtnRemove;

    @FXML
    private TableColumn<Card, String> columName;

    @FXML
    private TableColumn<Card, Double> columPrice;

    @FXML
    private TableColumn<Card, Integer> columQuantity;

    @FXML
    private TextField txtAmount;

    @FXML
    private Label txtHaveToPay;

    @FXML
    private Label txtTotal;

    private Alert alert;
    public void menuDisplayCard(){
        ObservableList<Product> products = ProductDAO.getInstance().getAll();
        int row = 0;
        int colum  = 0;
        MenuProGird.getRowConstraints().clear();
        MenuProGird.getColumnConstraints().clear();
        for (int i=0; i<products.size(); i++){
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/aptech/project2/Product/CardProduct.fxml"));
                AnchorPane pane = loader.load();
                CardProductController cardProductController = loader.getController();
                cardProductController.setData(products.get(i));
                if(colum==3){
                    colum=0;
                    row+=1;
                }
                MenuProGird.add(pane, colum++, row);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void disPlayTableCard(){
        ObservableList<Card> cards = CardDao.getInstance().getAllByStaff(StaffDAO.staffSession);
        columName.setCellValueFactory(Card->new SimpleStringProperty(Card.getValue().getProduct().getName()));
        columQuantity.setCellValueFactory(new PropertyValueFactory<Card, Integer>("proQuantity"));
        columPrice.setCellValueFactory(new PropertyValueFactory<Card, Double>("proPrice"));
        TableProduct.setItems(cards);
        double total = cards.stream().mapToDouble(x->x.getProQuantity()*x.getProPrice()).sum();
        txtTotal.setText(String.valueOf(total)+"$");
        double amount = 0;
        if(txtAmount.getText().isBlank()==true){
            amount=0;
        }else if(!Validate.checkPriceProduct(txtAmount.getText())){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message!");
            alert.setContentText("Discount value is not correct.");
            alert.showAndWait();
        }else {
            amount=Double.valueOf(txtAmount.getText());
        }

        Double haveToPay = total-amount;
        txtHaveToPay.setText(String.valueOf(haveToPay)+"$");
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuDisplayCard();
        disPlayTableCard();
        btnPutOut.setOnAction(event -> {
            Card newSelection = TableProduct.getSelectionModel().getSelectedItem();
            if(newSelection==null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setContentText("Select 1 product in the table to retrieve.");
                alert.showAndWait();
            }else {
                CardDao.getInstance().deleteOneCardByProId(newSelection);
                disPlayTableCard();
            }
        });
        btnRefresh.setOnAction(event -> {
            disPlayTableCard();
        });

        btnPay.setOnAction(event -> {
            if(txtTotal.getText().equals("0.0$")){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setContentText("Please choose a product to buy.");
                alert.showAndWait();
            }else if (!Validate.checkPriceProduct(txtAmount.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setContentText("Discount value is not correct.");
                alert.showAndWait();
            }else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Message");
                alert.setContentText("Are you sure.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK){
                    String ordId = "ORD"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    double subtotal = Double.parseDouble(txtTotal.getText().replace("$", ""));
                    double discount = 0;
                    if(txtAmount.getText().isBlank()==true){
                        discount=0;
                    }else {
                        discount=Double.parseDouble(txtAmount.getText());
                    }
                    double total = subtotal-discount;
                    Order order = new Order(ordId, StaffDAO.staffSession, subtotal,discount, total, LocalDateTime.now());
                    OrderDao.getInstance().insertOrder(order);
                    ObservableList<Card> cards = CardDao.getInstance().getAllByStaff(StaffDAO.staffSession);
                    cards.forEach(x->{
                        OrderDetail orderDetail = new OrderDetail(0, order, x.getProduct(), x.getProQuantity(), x.getProPrice());
                        OrderDetailDao.getInstance().insertOrdDetail(orderDetail);
                    });
                    CardDao.getInstance().deleteByStaff(StaffDAO.staffSession);
                    disPlayTableCard();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message!");
                    alert.setContentText("Successfully.");
                    alert.showAndWait();
                }
            }
        });
    }

}
