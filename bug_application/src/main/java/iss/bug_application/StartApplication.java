package iss.bug_application;


import iss.bug_application.controller.LoginController;
import iss.bug_application.repository.bugsRepository.BugsRepository;
import iss.bug_application.repository.userRepository.UserRepository;
import iss.bug_application.service.Service;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("bd.config"));
            System.out.println("Properties OK!");
            properties.list(System.out);
        }
        catch (IOException e) {
            System.out.println("Cannot oppen file!");
            return;
        }

        UserRepository userRepository = new UserRepository(properties);
        BugsRepository bugsRepository = new BugsRepository(properties);
        Service srv = new Service(bugsRepository, userRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 400);
        stage.setTitle("LogIn");
        stage.setScene(scene);
        LoginController controller = fxmlLoader.getController();
        controller.setService(srv);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
