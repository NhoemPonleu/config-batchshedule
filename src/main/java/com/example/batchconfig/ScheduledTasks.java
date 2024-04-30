package com.example.batchconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    
    private final PersonService personService;
    private final PersonAgeBiggerThan30Service personAgeBiggerThan30Service;

    @Autowired
    public ScheduledTasks(PersonService personService, PersonAgeBiggerThan30Service personAgeBiggerThan30Service) {
        this.personService = personService;
        this.personAgeBiggerThan30Service = personAgeBiggerThan30Service;
    }

    @Scheduled(cron = "0 06 17 * * *") // Runs every day at 2:15 PM
    public void savePersonAt230PM() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("John");
        personDTO.setAge(30);
        personService.savePerson(personDTO);
    }
    @Scheduled(cron = "00 12 14 * * *", zone = "Asia/Phnom_Penh")
    // Runs every day at 9:12 AMuns every day at 5:06 PM
    public void savePersonAgeBiggerThan30At305PM() {
        personAgeBiggerThan30Service.savePersonBiggerThan30();
    }
}
