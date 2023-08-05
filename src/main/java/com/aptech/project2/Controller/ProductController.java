package com.aptech.project2.Controller;
import com.aptech.project2.DAO.CategoryDAO;
import com.aptech.project2.DAO.OrderDao;
import com.aptech.project2.DAO.StaffDAO;
import com.aptech.project2.DAO.ProductDAO;
import com.aptech.project2.Model.Category;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Validation.Validate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private AnchorPane ProductForm;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnInSert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Product, String> colunmCatName = new TableColumn<>("Category");

    @FXML
    private TableColumn<Product, LocalDate> colunmDate;

    @FXML
    private TableColumn<Product, String> colunmId;

    @FXML
    private TableColumn<Product, String> colunmName;

    @FXML
    private TableColumn<Product, Double> colunmPrice;

    @FXML
    private TableColumn<Product, Integer> colunmQuantity;

    @FXML
    private ImageView imageView;

    @FXML
    private TableView<Product> tableProduct;

    @FXML
    private ComboBox<Category> txtCat;

    @FXML
    private DatePicker txtCreateDate;

    @FXML
    private TextField txtId;

    @FXML
    private Label txtMessage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;
    private ObservableList<Product> products = ProductDAO.getInstance().getAll();
    private Alert alert;
    public void choseImageCus(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OpenImage Files", "*png", "*jpg"));
        File file = fileChooser.showOpenDialog(btnImport.getScene().getWindow());
        if(file!=null){
            ProductDAO.pathImage = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString(), 131, 164, false, true);
            imageView.setImage(image);
        }
    }
    public void showTablePro(){
        colunmId.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        colunmName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        colunmCatName.setCellValueFactory(Product->new SimpleStringProperty(Product.getValue().getCategory().getName()));
        colunmQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        colunmPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        colunmDate.setCellValueFactory(new PropertyValueFactory<Product, LocalDate>("createDate"));
        tableProduct.setItems(products);
        tableProduct.setOnMouseClicked(event->{
            Product newSelection = tableProduct.getSelectionModel().getSelectedItem();
            if(newSelection!=null){
                txtId.setDisable(true);
                txtId.setText(newSelection.getId());
                txtName.setText(newSelection.getName());
                txtCat.setValue(newSelection.getCategory());
                txtCreateDate.setValue(newSelection.getCreateDate());
                txtPrice.setText(String.valueOf(newSelection.getPrice()));
                txtQuantity.setText(String.valueOf(newSelection.getQuantity()));
                ProductDAO.pathImage = newSelection.getImage();
                Image image = new Image(new File(ProductDAO.pathImage).toURI().toString(), 131, 164, false, true);
                imageView.setImage(image);
            }
        });
    }
    public void showCat(){
        ObservableList<Category> categories = CategoryDAO.getInstance().getList();
        txtCat.setItems(categories);
        txtCat.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }
            @Override
            public Category fromString(String string) {
                return null;
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showTablePro();
        showCat();
    }

    public void setBtnInSert(){
        txtId.setDisable(false);
        if(txtId.getText().isBlank()==true
                ||txtName.getText().isBlank()==true
                ||txtCat.getValue()==null
                ||txtQuantity.getText().isBlank()==true
                ||txtPrice.getText().isBlank()==true
                ||txtCreateDate.getValue()==null
                || ProductDAO.pathImage==null){
            txtMessage.setText("Please enter full textFiled.");
        }else {
            boolean f = true;
            String id = txtId.getText();
            String Quantity = txtQuantity.getText();
            String Price = txtPrice.getText();
            if(!Validate.checkProId(id)){
                f=false;
                txtMessage.setText("Product ID is invalid.");
            }
            if(ProductDAO.getInstance().findById(id)!=null){
                f = false;
                txtMessage.setText("Product ID is already.");
            }
            if(!Validate.checkProQuantity(Quantity)){
                f = false;
                txtMessage.setText("Product Quantity is invalid.");
            }
            if(!Validate.checkPriceProduct(Price)){
                f = false;
                txtMessage.setText("Product Price is invalid.");
            }
            if(f==true){
                txtMessage.setText("");
                String pro_id = txtId.getText();
                String name = txtName.getText();
                LocalDate date = txtCreateDate.getValue();
                int quantity = Integer.parseInt(txtQuantity.getText());
                double price = Double.valueOf(txtPrice.getText());
                Category category = txtCat.getValue();
                String image = ProductDAO.pathImage;
                Product product = new Product(pro_id, name, category, quantity, price, date, image);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Message");
                alert.setContentText("Do you want to create a product!");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK) {
                    ProductDAO.getInstance().insert(product);
                    products.add(0, product);
                    tableProduct.setItems(products);
                }
            }

        }
    }

    public void setBtnClear(){
        txtId.setDisable(false);
        tableProduct.getSelectionModel().clearSelection();
        txtId.setText("");
        txtName.setText("");
        txtCreateDate.setValue(null);
        txtQuantity.setText("");
        txtPrice.setText("");
        txtCat.setValue(null);
        ProductDAO.pathImage = "";
        imageView.setImage(null);
        txtMessage.setText("");
    }

    public void setBtnDelete(){
        if(txtId.getText().isBlank()==true){
            txtMessage.setText("Please enter the id of the product you want to delete.");
        }else {
            txtMessage.setText("");
            String id = txtId.getText();
            System.out.println(id);
            Optional<Product> productOptional = products.stream().filter(x->x.getId().equals(id)).findFirst();
            if(productOptional.isPresent()){
                Product product = productOptional.get();
                if(OrderDao.getInstance().countByProduct(product)!=0){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setContentText("Cannot this Product because is sale.");
                    alert.showAndWait();
                }else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Message");
                    alert.setContentText("Do you want to delete this product!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get()==ButtonType.OK){
                        ProductDAO.getInstance().delete(product);
                        products.remove(product);
                        tableProduct.setItems(products);
                        setBtnClear();
                    }
                }
            }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setContentText("Not found product");
                alert.showAndWait();
            }
        }
    }

    public void setBtnUpdate(){
        Product newSelection = tableProduct.getSelectionModel().getSelectedItem();
        if(newSelection==null){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning message");
            alert.setContentText("Please choose 1 row on the product table to update!");
            alert.showAndWait();
        }else {
            String id = txtId.getText();
            String name = txtName.getText();
            LocalDate date = txtCreateDate.getValue();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double price = Double.valueOf(txtPrice.getText());
            Category category = txtCat.getValue();
            String image = ProductDAO.pathImage;
            Product product = new Product(id, name, category, quantity, price, date, image);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Message");
            alert.setContentText("Do you want to update this product!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                ProductDAO.getInstance().update(product, newSelection.getId());
                newSelection.setId(product.getId());
                newSelection.setName(product.getName());
                newSelection.setCategory(product.getCategory());
                newSelection.setPrice(product.getPrice());
                newSelection.setQuantity(product.getQuantity());
                newSelection.setCreateDate(product.getCreateDate());
                newSelection.setImage(product.getImage());
                tableProduct.refresh();
            }
        }
    }

}
