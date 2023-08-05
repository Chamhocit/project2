package com.aptech.project2.Controller;


import com.aptech.project2.DAO.OrderDao;
import com.aptech.project2.DAO.StaffDAO;
import com.aptech.project2.Model.Product;
import com.aptech.project2.Model.Staff;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class StaffController implements Initializable {

        @FXML
        private AnchorPane CustomerForm;

        @FXML
        private TableColumn<Staff, String> columPhone;

        @FXML
        private TableColumn<Staff, String> columRole;

        @FXML
        private TableColumn<Staff, String> columUsername;

        @FXML
        private TableColumn<Staff, Integer> colunmId;

        @FXML
        private TableColumn<Staff, String> colunmName;

        @FXML
        private TableView<Staff> tableCustomer;

        @FXML
        private Button btnDelete;
        private Alert alert;

        public void showTableStaff(){
                ObservableList<Staff> staffs = StaffDAO.getInstance().getAll();
                colunmId.setCellValueFactory(new PropertyValueFactory<Staff, Integer>("id"));
                colunmName.setCellValueFactory(new PropertyValueFactory<Staff, String>("fullName"));
                columPhone.setCellValueFactory(new PropertyValueFactory<Staff, String>("phone"));
                columUsername.setCellValueFactory(new PropertyValueFactory<Staff, String>("username"));
                columRole.setCellValueFactory(new PropertyValueFactory<Staff, String>("role"));
                tableCustomer.setItems(staffs);
        }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
                showTableStaff();
                btnDelete.setOnAction(event -> {
                        Staff newSelection = tableCustomer.getSelectionModel().getSelectedItem();
                        if(newSelection==null){
                                alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning message");
                                alert.setContentText("Please choose 1 row on the staff table to delete.");
                                alert.showAndWait();
                        }else if(OrderDao.getInstance().countByStaff(newSelection)!=0) {
                                alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning message");
                                alert.setContentText("Cannot this Staff because having orders.");
                                alert.showAndWait();
                        }else {
                                alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirm Message");
                                alert.setContentText("Are you sure deleting this staff!");
                                Optional<ButtonType> result = alert.showAndWait();
                                if(result.get()==ButtonType.OK){
                                        StaffDAO.getInstance().delete(newSelection);
                                        alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Information message");
                                        alert.setContentText("Successfully");
                                        alert.showAndWait();
                                        showTableStaff();
                                }
                        }
                });
    }
}
