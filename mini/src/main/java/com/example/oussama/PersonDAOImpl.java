// PersonDAOImpl.java
package com.example.oussama;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {

    private Connection connection;

    public PersonDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Person> getAllPersons() {
        List<Person> personList = new ArrayList<>();
        String query = "SELECT firstname, lastname, numbrerID FROM miniprojet.employee";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String queryFirstname = resultSet.getString("firstname");
                String queryLastname = resultSet.getString("lastname");
                String queryNumberId = resultSet.getString("numbrerID");

                personList.add(new Person(queryFirstname, queryLastname, queryNumberId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personList;
    }

    @Override
    public void addPerson(Person person) {
        String insert = "INSERT INTO miniprojet.employee (firstname, lastname, numbrerID) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, person.getFirstname());
            preparedStatement.setString(2, person.getLastname());
            preparedStatement.setString(3, person.getNumberID());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePerson(Person person) {
        String delete = "DELETE FROM miniprojet.employee WHERE numbrerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
            preparedStatement.setString(1, person.getNumberID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePerson(Person person) {
        String update = "UPDATE miniprojet.employee SET firstname = ?, lastname = ?, numbrerID = ? WHERE numbrerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, person.getFirstname());
            preparedStatement.setString(2, person.getLastname());
            preparedStatement.setString(3, person.getNumberID());
            preparedStatement.setString(4, person.getNumberID());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
