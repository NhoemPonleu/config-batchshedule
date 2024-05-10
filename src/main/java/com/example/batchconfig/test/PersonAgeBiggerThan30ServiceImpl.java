package com.example.batchconfig.test;

import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.brand.BrandService;
import com.example.batchconfig.brand.BrandUtils;
import com.example.batchconfig.brand.brandShedule.TellerTypeCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonAgeBiggerThan30ServiceImpl implements PersonAgeBiggerThan30Service {

    private final PersonRepository personRepository;
    private final PersonAgeBiggerThan30Repository personAgeBiggerThan30Repository;
    private final PersonService personService;
    private final BrandService brandService;

    @Override
    public void savePersonBiggerThan30() {
        List<Person> allPersons = personService.getAllPersons1();
        List<Brand> brands = brandService.getAllBrandForBatch();

        BrandUtils.checkBrandClosed(brands);

        for (Person person : allPersons) {
            if (person.getAge() >= 30) {
                PersonAgeBiggerThan30 personAgeBiggerThan30 = new PersonAgeBiggerThan30();
                personAgeBiggerThan30.setAge(person.getAge());
                personAgeBiggerThan30.setRegisterDate(LocalDateTime.now());
                ZoneId cambodiaZone = ZoneId.of("Asia/Phnom_Penh");
                LocalTime cambodiaTime = LocalTime.now(cambodiaZone);
                personAgeBiggerThan30.setRegisterTime(cambodiaTime);
                personAgeBiggerThan30.setPerson(person);

                // Set the brand for the person
                Brand openBrand = getOpenBrand(brands);
                personAgeBiggerThan30.setBrand(openBrand);

                // Set the transaction details
                Long maxSeqNo = personAgeBiggerThan30Repository.findMaxSeqNoByPersonId(person.getId());
                personAgeBiggerThan30.setTransactionSeqNo(maxSeqNo != null ? maxSeqNo + 1 : 1);
                personAgeBiggerThan30.setTransactionDate(LocalDate.now());
                personAgeBiggerThan30.setRegisterTellerId(TellerTypeCode.BATCH_TELLER.getCode());
                personAgeBiggerThan30.setRegisterTellerName(TellerTypeCode.BATCH_TELLER.getDescription());

                personAgeBiggerThan30Repository.save(personAgeBiggerThan30);
                System.out.println("Person saved successfully");
            }
        }
    }

    private Brand getOpenBrand(List<Brand> brands) {
        for (Brand brand : brands) {
            if (brand.getCloseYN().equals("Y")) {
                return brand;
            }
        }
        return null;
    }
}
