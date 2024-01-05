package com.example.oussama;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {



    @FXML
    private Button updateButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label messageRegistration;

    @FXML
    private TextField fristename;

    @FXML
    private TextField lastename;

    @FXML
    private TextField numberId;

    @FXML
    private TableView<Person> tableData;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> numberIdColumn;

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();
    //special type of list that allows listeners to track changes when elements are added, removed, or modified.
    private ArrayList<Person> personList = new ArrayList<>();

    private Connection connection = null;
    private PersonDAO personDAO;
    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection connectNow = new DatabaseConnection();
        this.connection = connectNow.getConnection();
        updateButton.setOnAction(this::updateButtonOnAction);

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        numberIdColumn.setCellValueFactory(new PropertyValueFactory<>("numberID"));
        //assosiation des collums woth Person class properties.

        personDAO = new PersonDAOImpl(connection); //Creates an instance of the PersonDAOImpl

        personList.addAll(personDAO.getAllPersons());
        personObservableList.addAll(personList);
        tableData.setItems(personObservableList);
        //display the data from the personObservableList
    }
    @FXML
    public void updateButtonOnAction(ActionEvent event) {
        Person selectedPerson = tableData.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {

            fristename.setText(selectedPerson.getFirstname());
            lastename.setText(selectedPerson.getLastname());
            numberId.setText(selectedPerson.getNumberID());

            // Disable the register button while updating
            registerButton.setDisable(true);

            // Enable the update button
            updateButton.setDisable(false);
        } else {
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Select a person to update");
        }
    }

    @FXML
    public void saveChangesButtonOnAction(ActionEvent event) {
        Person selectedPerson = tableData.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {

            String updatedFirstname = fristename.getText();
            String updatedLastname = lastename.getText();
            String updatedNumberId = numberId.getText();


            selectedPerson.setFirstname(updatedFirstname);
            selectedPerson.setLastname(updatedLastname);
            selectedPerson.setNumberID(updatedNumberId);
            // Update the person in the database
            personDAO.updatePerson(selectedPerson);
            // Enable the register button
            registerButton.setDisable(false);

            fristename.clear();
            lastename.clear();
            numberId.clear();

            // Refresh the table
            refreshTable();

            // Display a success message
            messageRegistration.setTextFill(Color.GREEN);
            messageRegistration.setText("User information has been updated successfully!");
        } else {
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Select a person to update");
        }
    }



    @FXML
    public void registerButtonOnAction(ActionEvent event) {
        if (fristename.getText().isEmpty() || lastename.getText().isEmpty() || numberId.getText().isEmpty()) {
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Enter all details");
        } else {
            Person newPerson = new Person(fristename.getText(), lastename.getText(), numberId.getText());
            personDAO.addPerson(newPerson);

            messageRegistration.setTextFill(Color.GREEN);
            messageRegistration.setText("User has been registered successfully!");

            refreshTable();

            fristename.clear();
            lastename.clear();
            numberId.clear();
        }
    }

    @FXML
    public void deleteButtonOnAction(ActionEvent event) {
        Person selectedPerson = tableData.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            personDAO.deletePerson(selectedPerson);

            messageRegistration.setTextFill(Color.GREEN);
            messageRegistration.setText("User has been deleted successfully!");

            refreshTable();
        } else {
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Select a person ");
        }
    }

    @FXML
    private void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void refreshTable() {

        personList.clear();
        personList.addAll(personDAO.getAllPersons());

        personObservableList.clear();
        personObservableList.addAll(personList); // li ghatban

        tableData.setItems(personObservableList);// tban
    }
}
