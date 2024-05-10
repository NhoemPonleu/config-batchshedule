package com.example.batchconfig.test;

import java.util.List;

public interface PersonService {
    void savePerson(PersonDTO personDTO);
   // Person getPerson(Long id);
  //  Person findAllPersons();
   public List<PersonDTO> getAllPersons();
    List<Person> getAllPersons1();
}
