package com.example.boot3.customer;

import java.util.Collection;

interface CustomerService {
    Customer add(String name);

    Customer byId(Integer id);

    Collection<Customer> all();
}
