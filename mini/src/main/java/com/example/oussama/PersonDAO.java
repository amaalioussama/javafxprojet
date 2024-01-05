
package com.example.oussama;

import java.util.List;

public interface PersonDAO {
    List<Person> getAllPersons();
    void addPerson(Person person);
    void deletePerson(Person person);
    void updatePerson(Person person);

}
