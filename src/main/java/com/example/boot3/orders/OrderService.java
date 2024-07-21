package com.example.boot3.orders;

import com.example.boot3.customer.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @EventListener
    public void onCustomerCreated(CustomerCreatedEvent customerCreatedEvent){
        log.info(customerCreatedEvent.getSource().name());
    }
}
