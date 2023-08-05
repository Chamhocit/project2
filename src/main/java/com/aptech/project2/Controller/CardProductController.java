package com.aptech.project2.Controller;

import com.aptech.project2.DAO.CardDao;
import com.aptech.project2.DAO.ProductDAO;
import com.aptech.project2.DAO.StaffDAO;
import com.aptech.project2.Model.Card;
import com.aptech.project2.Model.FxmlLoader;
import com.aptech.project2.Model.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class CardProductController implements Initializable {
    @FXML
    private ImageView ProImage;

    @FXML
    private Spinner<Integer> ProSpinner;

    @FXML
    private Button btnAdd;

    @FXML
    private Label txtProName;

    @FXML
    private Label txtProPrice;
    private Image image;
    private int ProQuantity;
    private Alert alert;
    private Product product;
    private SpinnerValueFactory<Integer> spin;

    public void setData(Product product){
        this.product = product;
        txtProName.setText(product.getName());
        txtProPrice.setText("$" + String.valueOf(product.getPrice()));
        ProQuantity = product.getQuantity();
        String path = "File:"+product.getImage();
        image = new Image(path, 190, 94, false, true);
        ProImage.setImage(image);
    }

    public void setQuantity(){
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        ProSpinner.setValueFactory(spin);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
        btnAdd.setOnAction(event -> {
            if(ProSpinner.getValue()==0){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setContentText("Please enter the number of products.");
                alert.showAndWait();
            }else if (ProSpinner.getValue()!=0 && ProQuantity==0){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message!");
                alert.setContentText("This product is out of stock.");
                alert.showAndWait();
            }else {
                if(CardDao.getInstance().findByProId(product)!=0){
                    CardDao.getInstance().updateByProID(product, ProSpinner.getValue());
                }else {
                    Card card = new Card(0, StaffDAO.staffSession, product, ProSpinner.getValue(), product.getPrice());
                    CardDao.getInstance().insertCard(card);
                }
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message!");
                alert.setContentText("Add to cart successfully.");
                alert.showAndWait();
                ProSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));
            }
        });
    }
    

}
