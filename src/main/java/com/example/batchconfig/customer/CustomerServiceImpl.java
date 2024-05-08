package com.example.batchconfig.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getName());
        customer.setLastName(customerDTO.getName());
        customer.setAddress(customerDTO.getName());

        for (VillageDTO villageDTO : customerDTO.getVillages()) {
            Village village = new Village();
            village.setName(villageDTO.getName());
            customer.addVillage(village);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return mapToDTO(savedCustomer);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getFirstName());
        customerDTO.setName(customer.getLastName());
        customerDTO.setName(customer.getAddress());
        // You may need to map villages as well, depending on your requirements
        return customerDTO;
    }
}
