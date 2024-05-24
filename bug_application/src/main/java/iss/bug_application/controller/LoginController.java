package iss.bug_application.controller;


import iss.bug_application.ProgrammerApplication;
import iss.bug_application.TesterApplication;
import iss.bug_application.controller.alert.LoginActionAlert;
import iss.bug_application.domain.TypeEnum;
import iss.bug_application.domain.User;
import iss.bug_application.repository.utils.PasswordHashing;
import iss.bug_application.service.Service;
import iss.bug_application.service.ServiceInterface;
import iss.bug_application.utils.events.BugTaskChangeEvent;
import iss.bug_application.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController{

    private Service service;
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<TypeEnum> function_box;

    @FXML
    private Button login;


    public void setService(Service service){
        this.service = service;
        set_combo_box();
    }

    public void handle_login(ActionEvent actionEvent) {
        String username_field = username.getText();
        String password_field = password.getText();
        TypeEnum type = function_box.getValue();
        if (password_field.isEmpty()) {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Password cannot be empty!");
            return;
        }
        if(username_field.isEmpty())
        {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! Username cannot be empty");
            return;
        }
        if (type == null) {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! A role must be selected!");
            return;
        }


        User user = service.findUserByData(username_field, type);

        if(!user.getPassword().equals(PasswordHashing.hashPassword(password_field))) {
            LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Eroare! Nu exista un cont asociat acestor date!");
            username.clear();
            password.clear();
        }
        else
        {
            Long id_user = user.getId();
            Stage stage = new Stage();

            if (user.getType() == TypeEnum.PROGRAMMER)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(ProgrammerApplication.class.getResource("programmer-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 595, 413);
                    stage.setScene(scene);
                    stage.setTitle("Welcome " + user.getUsername() + "!");

                    ProgrammerController adminController = fxmlLoader.getController();
                    adminController.setService(service,user);
                    stage.show();
                }
                catch (IOException e) {
                    LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                }
                username.clear();
                password.clear();
            }
            else if (user.getType() == TypeEnum.TESTER){
                FXMLLoader fxmlLoader = new FXMLLoader(TesterApplication.class.getResource("tester-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 593, 390);
                    stage.setScene(scene);

                    stage.setTitle("Welcome " + user.getUsername() + "!");
                    TesterController adminController = fxmlLoader.getController();
                    adminController.setService(service,user);
                    stage.show();
                }
                catch (IOException e) {
                    LoginActionAlert.showMessage(null, Alert.AlertType.ERROR, "Error", e.getMessage());
                }
                username.clear();
                password.clear();
            }
        }
    }

    public void set_combo_box()
    {
        function_box.setItems(FXCollections.observableArrayList(TypeEnum.values()));
    }

}
