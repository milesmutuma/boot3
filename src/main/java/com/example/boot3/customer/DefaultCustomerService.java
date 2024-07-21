package com.example.boot3.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@ComponentScan
@Transactional
@RequiredArgsConstructor
class DefaultCustomerService implements CustomerService {

    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;


    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
        var id = rs.getInt("id");
        var name = rs.getString("name");
        return new Customer(id, name);
    };


    @Override
    public Customer add(String name) {
        var al = new ArrayList<Map<String, Object>>();
        var hm = new HashMap<String, Object>();

        hm.put("id", Long.class);
        var keyholder = new GeneratedKeyHolder(al);
        this.jdbcTemplate.update(
                con -> {
                    var ps = con.prepareStatement("insert into customers" +
                            " (name) values(?)", Statement.RETURN_GENERATED_KEYS);

                    ps.setString(1, name);
                    return ps;
                },
                keyholder
        );

        var generateId = keyholder.getKeys().get("id");
        log.info("generated Id: {}", generateId.toString());

        Assert.state(generateId instanceof Number, "the generateId must be a Number");
        Number number = (Number) generateId;
        Customer customer = byId(number.intValue());
        applicationEventPublisher.publishEvent(new CustomerCreatedEvent(customer));
        return customer;
    }

    @Override
    public Customer byId(Integer id) {
        return this.jdbcTemplate.queryForObject("SELECT id, name FROM customers where id=?", this.customerRowMapper, id);
    }

    @Override
    public Collection<Customer> all() {
        return this.jdbcTemplate.query("SELECT id,name FROM customers",
                this.customerRowMapper);
    }


}
