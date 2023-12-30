package com.example.oussama;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements Initializable {

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
    private TextField searchTextField;

    @FXML
    private TableView<Person> tableData;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Person, String> numberIdColumn;
    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();


    private ArrayList<Person> personList = new ArrayList<>();

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        String persondata = "SELECT firstname, lastname, numbrerID FROM miniprojet.employee";

        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(persondata);

            while (queryOutput.next()) {
                String queryFirstname = queryOutput.getString("firstname");
                String queryLastname = queryOutput.getString("lastname");
                String queryNumberId = queryOutput.getString("numbrerID");

                personList.add(new Person(queryFirstname, queryLastname, queryNumberId));
            }

            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            numberIdColumn.setCellValueFactory(new PropertyValueFactory<>("numberID"));

            tableData.getItems().addAll(personList);

            FilteredList<Person> filteredList = new FilteredList<>(FXCollections.observableArrayList(personObservableList), b -> true);

            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(person -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (person.getFirstname().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else if (person.getLastname().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else return person.getNumberID().toLowerCase().contains(searchKeyword);
                });
            });

            SortedList<Person> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(tableData.comparatorProperty());
            tableData.setItems(sortedList);

        } catch (SQLException e) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    public void registerButtonOnAction(ActionEvent event) {
        if (fristename.getText().isEmpty() || lastename.getText().isEmpty() || numberId.getText().isEmpty()) {
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Enter all details");
        } else {
            registerUser();
        }
    }

    @FXML
    public void deleteButtonOnAction(ActionEvent event) {
        Person selectedPerson = tableData.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            deletePerson(selectedPerson);
        } else {
            showAlert("No person selected", "Please select a person to delete.");
        }
    }

    @FXML
    public void closeButtonOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to quit?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            Platform.exit();
        } else {
            alert.close();
        }
    }

    public void registerUser() {
        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        String firstname = fristename.getText();
        String lastname = lastename.getText();
        String numberIdValue = numberId.getText();

        String insertFields = "INSERT INTO miniprojet.employee (firstname, lastname, numbrerID) VALUES (?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(insertFields);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, numberIdValue);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                messageRegistration.setTextFill(Color.GREEN);
                messageRegistration.setText("User registered successfully!");
                refreshTable();
            } else {
                messageRegistration.setTextFill(Color.TOMATO);
                messageRegistration.setText("User registration failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageRegistration.setTextFill(Color.TOMATO);
            messageRegistration.setText("Error: " + e.getMessage());
        }
    }

    private void deletePerson(Person person) {
        try {
            String query = "DELETE FROM miniprojet.employee WHERE numbrerID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, person.getNumberID());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                showMessage("Person deleted successfully!", Color.GREEN);
                refreshTable();
            } else {
                showMessage("Failed to delete the person.", Color.TOMATO);
            }
        } catch (SQLException e) {
            showMessage("Error: " + e.getMessage(), Color.TOMATO);
        }
    }

    @FXML
    private void refreshTable() {
        try {
            personList.clear();

            String query = "SELECT * FROM miniprojet.employee";
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                personList.add(new Person(
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("numbrerID")));
            }

            tableData.getItems().clear();
            tableData.getItems().addAll(personList);
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMessage(String text, Color color) {
        messageRegistration.setTextFill(color);
        messageRegistration.setText(text);
    }

    private void showAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}