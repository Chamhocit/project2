package com.aptech.project2.Controller;


import com.aptech.project2.DAO.CategoryDAO;
import com.aptech.project2.DAO.ProductDAO;
import com.aptech.project2.Model.Category;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Validation.Validate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Category, LocalDate> columCreateDate;

    @FXML
    private TableColumn<Category, String> columnId;

    @FXML
    private TableColumn<Category, String> columnName;

    @FXML
    private TableColumn<Category, String> columnParentCat = new TableColumn<>("Category");

    @FXML
    private ComboBox<Category> comBoxParentCat;

    @FXML
    private TableView<Category> tableCat;

    @FXML
    private DatePicker txtCreateDate;

    @FXML
    private TextField txtId;

    @FXML
    private Label txtMessage;

    @FXML
    private TextField txtName;
    private ObservableList<Category> listCategory = CategoryDAO.getInstance().getList();
    private Alert alert;

    @FXML
    void setBtnDelete(ActionEvent event) {
        if(txtId.getText().isBlank()==true){
            txtMessage.setText("Please enter the id of the category you want to delete.");
        }else {
            txtMessage.setText("");
            String id = txtId.getText();
//            System.out.println(id);
            Optional<Category> categoryOptional = listCategory.stream().filter(x->x.getId().equals(id)).findFirst();
            if(categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                if (ProductDAO.getInstance().findByCatId(category) != 0) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Confirm Error");
                    alert.setContentText("You can't this category including product.");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Message");
                    alert.setContentText("Do you want to delete this category.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        CategoryDAO.getInstance().deleteCat(category);
                        listCategory.remove(category);
                        tableCat.setItems(listCategory);
                        setBtnClear();
                    }
                }
            }
        }
    }

    @FXML
    void setBtnInsert(ActionEvent event) {
        txtId.setDisable(false);
        if(txtId.getText().isBlank()==true
                ||txtName.getText().isBlank()==true
                ||txtCreateDate.getValue()==null){
            txtMessage.setText("Please enter full text field.");
        }else {
            boolean f = true;
            String id = txtId.getText();
            if(!Validate.checkCatId(id)){
                f = false;
                txtMessage.setText("Category Id is invalid");
            }
            if(CategoryDAO.getInstance().findById(id)!=null){
                f = false;
                txtMessage.setText("Category ID is already.");
            }
            if(f==true){
                txtMessage.setText("");
                String catid = txtId.getText();
                String name = txtName.getText();
                LocalDate date = txtCreateDate.getValue();
                Category parentCat = comBoxParentCat.getValue();
                Category category = new Category(id, name, parentCat,date);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Message");
                alert.setContentText("Do you want to create a category!");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK) {
                    CategoryDAO.getInstance().insertCat(category);
                    listCategory.add(0, category);
                    tableCat.setItems(listCategory);
                    comBoxParentCat.setItems(listCategory);
                    setBtnClear();
                }
            }
        }
    }

    @FXML
    void setBtnUpdate(ActionEvent event) {
        Category newSelection = tableCat.getSelectionModel().getSelectedItem();
        if(newSelection==null){
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning message");
            alert.setContentText("Please choose 1 row on the category table to update!");
            alert.showAndWait();
        }else {
            String id = txtId.getText();
            String name = txtName.getText();
            LocalDate date = txtCreateDate.getValue();
            Category categoryParent = comBoxParentCat.getValue();
            Category category = new Category(id, name, categoryParent, date);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Message");
            alert.setContentText("Do you want to update this category!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                CategoryDAO.getInstance().updateCat(newSelection.getId(), category);
                newSelection.setId(category.getId());
                newSelection.setName(category.getName());
                newSelection.setParentCat(category.getParentCat());
                newSelection.setCreateDate(category.getCreateDate());
                tableCat.refresh();
            }
        }
    }
    @FXML
    public void setBtnClear(){
        txtId.setDisable(false);
        tableCat.getSelectionModel().clearSelection();
        txtId.setText("");
        txtName.setText("");
        txtCreateDate.setValue(null);
        comBoxParentCat.setValue(null);
        txtMessage.setText("");
    }
    public void showTableCat(){
        columnId.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        columnParentCat.setCellValueFactory(Category->new SimpleStringProperty(Category.getValue().getParentCat()!=null?Category.getValue().getParentCat().getName():""));
        columCreateDate.setCellValueFactory(new PropertyValueFactory<Category, LocalDate>("createDate"));
        tableCat.setItems(listCategory);
        tableCat.setOnMouseClicked(event->{
            Category newSelection = tableCat.getSelectionModel().getSelectedItem();
            if(newSelection!=null){
                txtId.setDisable(true);
                txtId.setText(newSelection.getId());
                txtName.setText(newSelection.getName());
                txtCreateDate.setValue(newSelection.getCreateDate());
                Category parentCat = newSelection.getParentCat();
                comBoxParentCat.setValue(parentCat);
            }
        });

    }
    public void showParentCat(){
        comBoxParentCat.setItems(listCategory);
        comBoxParentCat.setConverter(new StringConverter<Category>() {
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
        showTableCat();
        showParentCat();
    }
}
