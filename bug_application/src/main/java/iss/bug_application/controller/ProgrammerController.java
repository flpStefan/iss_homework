package iss.bug_application.controller;

import iss.bug_application.controller.alert.UserActionsAlert;
import iss.bug_application.domain.Bug;
import iss.bug_application.domain.StatusType;
import iss.bug_application.domain.User;
import iss.bug_application.service.Service;
import iss.bug_application.service.ServiceInterface;
import iss.bug_application.utils.events.BugTaskChangeEvent;
import iss.bug_application.utils.events.ChangeEventType;
import iss.bug_application.utils.observer.Observer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.StreamSupport;

public class ProgrammerController implements Observer<BugTaskChangeEvent> {
    @FXML
    private TextField programmerText;

    @FXML
    private Button exitButton;

    //--------------------------------------------------------------------------------\\

    ObservableList<Bug> modelBugs = FXCollections.observableArrayList();

    @FXML
    private TableView<Bug> bugsList;

    @FXML
    private TableColumn<Bug, String> nameColumn;

    @FXML
    private TableColumn<Bug, String> descriptionColumn;

    @FXML
    private TableColumn<Bug, String> statusColumn;

    @FXML
    private TableColumn<Bug, Long> solvedColumn;

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(cellData -> {
            String tuple = cellData.getValue().getName();
            return new SimpleObjectProperty<>(tuple);
        });

        descriptionColumn.setCellValueFactory(cellData -> {
            String tuple = cellData.getValue().getDescription();
            return new SimpleObjectProperty<>(tuple);
        });

        statusColumn.setCellValueFactory(cellData -> {
            String tuple = cellData.getValue().getStatus().toString();
            return new SimpleObjectProperty<>(tuple);
        });

        solvedColumn.setCellValueFactory(cellData ->{
            Long tuple = cellData.getValue().getResolved_by();
            if(tuple == null) tuple = -1l;
            return new SimpleObjectProperty<>(tuple);
        });

        bugsList.setItems(modelBugs);
    }

    private void initializeBugsList() {
        Iterable<Bug> allBugs = service.getAllBugs();

        List<Bug> allBugsList = StreamSupport.stream(allBugs.spliterator(), false).toList();
        modelBugs.setAll(allBugsList);
    }
//--------------------------------------------------------------------------------------------------------\\

    private Service service;
    private User user;
    public void setService(Service srv,User user)
    {
        this.service = srv;
        this.user = user;
        this.service.addObserver(this);
        initializeBugsList();
        programmerText.setText(user.getUsername());
    }

    @FXML
    private void handleResolve(){
        Bug bug = bugsList.getSelectionModel().getSelectedItem();
        if(bug == null){
            UserActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! You must select a bug to solve!");
            return;
        }

        if (bug.getStatus().equals(StatusType.RESOLVED)) {
            UserActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! This bug is already solved!");
            return;
        }

        bug.setStatus(StatusType.RESOLVED);
        bug.setResolved_by(user.getId());
        service.solveBug(bug);
        service.notifyObservers(new BugTaskChangeEvent(ChangeEventType.UPDATE,bug));
        //initializeBugsList();
    }

    @FXML
    private void handleExit(){
        Node src = exitButton;
        Stage stage = (Stage) src.getScene().getWindow();

        stage.close();
    }

    @Override
    public void update(BugTaskChangeEvent bugTaskChangeEvent) {
        initializeBugsList();
    }
}
