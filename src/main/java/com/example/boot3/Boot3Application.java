package com.example.boot3;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.postgresql.Driver;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

@Slf4j
public class Boot3Application {

    private static CustomerService transactionalCustomerService(CustomerService delegate, TransactionTemplate tt){

        var pfb = new ProxyFactoryBean();
        pfb.setTarget(delegate);
        pfb.setProxyTargetClass(true);
        pfb.addAdvice((MethodInterceptor) invocation -> {
           var method =  invocation.getMethod();
           var args = invocation.getArguments();
            tt.execute(status -> {
                try {
                    return method.invoke(delegate, args);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
            return null;
        });

        return (CustomerService) pfb.getObject();
    }
    public static void main(String[] args) {
        var datasource = new DriverManagerDataSource(
                "jdbc:postgresql://localhost/postgres",
                "myuser", "mypassword"
        );
        datasource.setDriverClassName(Driver.class.getName());

        var template = new JdbcTemplate(datasource);
        template.afterPropertiesSet();

        var ptm = new DataSourceTransactionManager(datasource);

        var tt = new TransactionTemplate(ptm);

        var dCustomer =  transactionalCustomerService(new DefaultCustomerService(template),tt);

        var all = dCustomer.all();


        all.forEach(c -> log.info(c.name()));
    }
}

interface CustomerService{
    Customer add(String name);
    Customer byId(Integer id);
    Collection<Customer> all();
}

@Slf4j
class DefaultCustomerService implements CustomerService{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
        var id = rs.getInt("id");
        var name = rs.getString("name");
        return new Customer(id, name);
    };

    DefaultCustomerService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        return byId(number.intValue());
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
record Customer(Integer id, String name) {
}