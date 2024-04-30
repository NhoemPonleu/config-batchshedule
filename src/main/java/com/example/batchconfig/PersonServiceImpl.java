package com.example.batchconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {
    
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    @Override
    public void savePerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        personRepository.save(person);
        System.out.printf("Person Save Successfully!\n");
    }
    @Override
    public List<PersonDTO> getAllPersons() {
        List<Person> allPersons = personRepository.findAll();
        List<PersonDTO> olderThan30 = allPersons.stream()
                .filter(person -> person.getAge() >= 30)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        List<PersonDTO> youngerThanOrEqual30 = allPersons.stream()
                .filter(person -> person.getAge() <= 30)
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Perform division if needed
        if (!youngerThanOrEqual30.isEmpty()) {
            // Perform division operation or logic here
            // For example, dividing by 2
            // You can implement your own logic based on your requirement
            // Here, we're simply dividing the size of the youngerThanOrEqual30 list by 2
            int divisionResult = youngerThanOrEqual30.size() / 2;
            System.out.println("Division Result: " + divisionResult);
        }

        return olderThan30;
    }

    @Override
    public List<Person> getAllPersons1() {
       List<Person>personList= personRepository.findAll();
        return personList;
    }

    private PersonDTO convertToDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(person.getName());
        personDTO.setAge(person.getAge());
        return personDTO;
    }
}
