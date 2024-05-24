package iss.bug_application.controller;


import iss.bug_application.controller.alert.TesterActionsAlert;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.StreamSupport;

public class TesterController implements Observer<BugTaskChangeEvent> {
    @FXML
    private TextField testerText;

    @FXML
    private TextField nameText;

    @FXML
    private TextArea descriptionText;

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


    //----------------------------------------------------------------\\
    private Service service;
    private User user;
    public void setService(Service srv, User user)
    {
        this.service = srv;
        this.user = user;
        this.service.addObserver(this);
        initializeBugsList();
        testerText.setText(user.getUsername());
    }


    @FXML
    private void handleAdd(){
        String name = nameText.getText();
        String description = descriptionText.getText();

        if(name.isEmpty() || description.isEmpty()){
            TesterActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! Information cannot be empty!");
            return;
        }

        Bug bug = new Bug(name, description, StatusType.UNRESOLVED, -1L);
        service.addBug(bug);
        //initializeBugsList();
        service.notifyObservers(new BugTaskChangeEvent(ChangeEventType.ADD,bug));
        nameText.clear();
        descriptionText.clear();
    }


    @FXML
    private void handleModify(){
        String name = nameText.getText();
        String description = descriptionText.getText();

        if(name.isEmpty() || description.isEmpty()){
            TesterActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! Information cannot be empty!");
            return;
        }

        Bug bug = bugsList.getSelectionModel().getSelectedItem();
        nameText.setText(bug.getName());
        descriptionText.setText(bug.getDescription());
        if(bug == null){
            TesterActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! You must select a bug to modify!");
            return;
        }

        bug.setName(name);
        bug.setDescription(description);
        service.modifyBug(bug);
        //initializeBugsList();
        service.notifyObservers(new BugTaskChangeEvent(ChangeEventType.UPDATE,bug));
        nameText.clear();
        descriptionText.clear();
    }

    @FXML
    private void handleExit(){
        Node src = exitButton;
        Stage stage = (Stage) src.getScene().getWindow();

        stage.close();
    }

    @FXML
    private void handleInfor(){
        Bug bug = bugsList.getSelectionModel().getSelectedItem();
        if(bug == null){
            TesterActionsAlert.showMessage(null, Alert.AlertType.ERROR, "Error", "Error! There must be a bug selected!");
            return;
        }

        TesterActionsAlert.showMessage(null, Alert.AlertType.INFORMATION, "Bug Info", "Full bug information:\n"
                + "Bug name: " + bug.getName() + "\n"
                + "Bug description: " + bug.getDescription() + "\n"
                + "Bug status: " + bug.getStatus() + "\n"
                + "Bug solved by: " + bug.getResolved_by());
    }

    @Override
    public void update(BugTaskChangeEvent bugTaskChangeEvent) {
        initializeBugsList();
    }
}
