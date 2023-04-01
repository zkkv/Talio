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

    /**
     * Allows admin to proceed to the next screen if the
     * password is correct or shows an alert otherwise.
     *
     * @author      Kirill Zhankov
     */
    public void loginAsAdmin(){
        if(password.getText().equals(pass)){
            mainCtrl.showAdminOverview();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Password");
            alert.setContentText("Please try again with a different password");
            alert.show();
        }
    }

    /**
     * Returns admin to the board selection page.
     *
     * @author      Kirill Zhankov
     */
    public void goBack(){
        mainCtrl.showStartPage();
    }

    /**
     * Sets admin password to pass.
     *
     * @param pass  string to set password to
     * @author      Kirill Zhankov
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}