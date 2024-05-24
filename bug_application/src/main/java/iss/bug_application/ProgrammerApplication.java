package iss.bug_application;


import iss.bug_application.controller.ProgrammerController;
import iss.bug_application.repository.bugsRepository.BugsRepository;
import iss.bug_application.repository.userRepository.UserRepository;
import iss.bug_application.service.Service;
import iss.bug_application.service.ServiceInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProgrammerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        UserRepository userRepository = new UserRepository(props);
        BugsRepository bugsRepository = new BugsRepository(props);
        Service srv = new Service(bugsRepository, userRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("programmer-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 340);
        stage.setTitle("Hello Programmer!");
        stage.setScene(scene);
        ProgrammerController controller = fxmlLoader.getController();
        //controller.setService(srv);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
