package com.example.batchconfig;

import lombok.extern.java.Log;

import java.util.List;

public interface PersonService {
    void savePerson(PersonDTO personDTO);
   // Person getPerson(Long id);
  //  Person findAllPersons();
   public List<PersonDTO> getAllPersons();
    List<Person> getAllPersons1();
}
