package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AdminLoginCtrl {
    private final MainCtrl mainCtrl;

    private String pass;
    @FXML
    private TextField password;

    @Inject
    public AdminLoginCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void loginAsAdmin(){
        if(password.getText().equals(pass)){
            //TODO go to admin overview
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Password");
            alert.setContentText("Please try again with a different password");
            alert.show();
        }
    }

    public void goBack(){
        mainCtrl.showStartPage();
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}