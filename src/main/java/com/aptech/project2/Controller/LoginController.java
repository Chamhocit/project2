package com.aptech.project2.Controller;

import com.aptech.project2.DAO.StaffDAO;
import com.aptech.project2.Model.Staff;
import com.aptech.project2.Validation.Validate;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private AnchorPane FormLogin;
    @FXML
    private Button btnAlreadyAccount;
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnLogin;


    @FXML
    private Button btnSignUp;

    @FXML
    private ComboBox<String> comboxUserRole;

    @FXML
    private TextField txtFullname;

    @FXML
    private Label txtMesLogin;

    @FXML
    private Label txtMesRegist;

    @FXML
    private PasswordField txtPassRegist;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtUserRigist;

    @FXML
    private TextField txtUsername;

    @FXML
    private AnchorPane sideForm;
    @FXML
    private AnchorPane loginForm;

    @FXML
    private AnchorPane registForm;

    @FXML
    private PasswordField txtAnswerSecret;
    private Alert alert;
    @FXML
    public void setBtnCancel(ActionEvent event){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void switchForm(ActionEvent event){
        TranslateTransition slider = new TranslateTransition();
        if(event.getSource()==btnCreateAccount){
            slider.setNode(sideForm);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.2));
            slider.setOnFinished((ActionEvent e)->{
                btnAlreadyAccount.setVisible(true);
                btnCreateAccount.setVisible(false);
                registForm.setVisible(true);
                loginForm.setVisible(false);
            });
            slider.play();
        }else if(event.getSource()==btnAlreadyAccount){
            slider.setNode(sideForm);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.2));
            slider.setOnFinished((ActionEvent e)->{
                btnAlreadyAccount.setVisible(false);
                btnCreateAccount.setVisible(true);
                registForm.setVisible(false);
                loginForm.setVisible(true);
            });
            slider.play();
        }
    }
    public void showRoleUser(){
        ObservableList<String> userRoles = FXCollections.observableArrayList("Admin", "Staff");
        comboxUserRole.setItems(userRoles);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showRoleUser();
    }

    public void setBtnSignUp(){
        if(txtUserRigist.getText().isBlank()==true
        ||txtFullname.getText().isBlank()==true
        ||txtPassRegist.getText().isBlank()==true
        ||txtPhone.getText().isBlank()==true
        ||comboxUserRole.getValue()==null
        ||txtAnswerSecret.getText().isBlank()==true){
            txtMesRegist.setText("Please enter full field.");
        }else {
            String answerSecret = txtAnswerSecret.getText();
            String username = txtUserRigist.getText();
            String password = txtPassRegist.getText();
            String fullName = txtFullname.getText();
            String phone = txtPhone.getText();
            String role = comboxUserRole.getValue().toLowerCase();
            boolean flat = true;
            if(!BCrypt.checkpw(answerSecret, "$2a$10$60E6BOyeLh4aONUuHxmW8OUetgVJD987uGxToxOnjLgqW3Zp0SJgq")){
                flat=false;
                txtMesRegist.setText("Answer secret is in Not correct.");
            }
            if(!Validate.checkFullName(fullName)){
                flat = false;
                txtMesRegist.setText("FullName can not including number and special characters.");
            }
            if(!Validate.checkPass(password)){
                flat = false;
                txtMesRegist.setText("Password must contain at least 8 characters including alphanumeric characters.");
            }
            if(!Validate.checkPhone(phone)){
                flat = false;
                txtMesRegist.setText("Phone number is invalid.");
            }
            if(StaffDAO.getInstance().findUserName(username)!=null){
                flat = false;
                txtMesRegist.setText("UserName is already.");
            }
            if(flat==true){
                String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
                txtMesRegist.setText("");
                Staff staff = new Staff(0, username, passwordHash, fullName, phone, role);
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Message");
                alert.setContentText("Do you want to Registration User.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK){
                    StaffDAO.getInstance().insert(staff);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Registration is success.");
                    alert.showAndWait();
                }
            }
        }
    }


    public void setBtnLogin(ActionEvent event) throws IOException {
        if(txtUsername.getText().isBlank()==true
        || txtPassword.getText().isBlank()==true){
            txtMesLogin.setText("Please enter Username and Password.");
        }else {
            String userName = txtUsername.getText();
            String password = txtPassword.getText();
            Staff staff = StaffDAO.getInstance().findUserName(userName);
//            boolean flat = true;
            if (staff == null) {
//                flat=false;
                txtMesLogin.setText("Incorrect account or password");
            } else {
                if (!BCrypt.checkpw(password, staff.getPassword())) {
                    txtMesLogin.setText("Incorrect account or password.");
                } else {
                    StaffDAO.staffSession = staff;
                    btnLogin.getScene().getWindow().hide();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/com/aptech/project2/Menu/Menu.fxml"));
                    SplitPane pane = loader.load();
//                    MainController mainController = loader.getController();
//                    mainController.setStaff(staff);
                    Scene home = new Scene(pane);
                    Stage stage = new Stage();
                    stage.setScene(home);
                    stage.setTitle("Sale Shop Management Systems");
                    stage.show();
                }
            }
        }
    }




}
