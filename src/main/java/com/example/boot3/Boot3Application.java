package com.example.boot3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Boot3Application {


    public static void main(String[] args) {
//        var ac = SpringApplication.run(Boot3Application.class, args);
//        var cs = ac.getBean(CustomerService.class);
//        cs.add("Miles");

        SpringApplication.run(Boot3Application.class, args);
    }

//    @Bean
//    ApplicationRunner runner(CustomerService customerService) {
//        return event -> {
//            customerService.add("Miles");
//        };
//    }

//    @Bean
//    ApplicationListener<ApplicationReadyEvent> applicationListener(CustomerService cs){
//        return event -> {
//            cs.add("Miles");
//        };
//    }
//    @Bean
//    ApplicationListener<WebServerInitializedEvent> webServerInitializedEventApplicationListener(){
//        return event -> log.info("the web server is ready to serve HTTP traffic on port {}", event.getWebServer().getPort());
//    }
//
//    @Bean
//    ApplicationListener<AvailabilityChangeEvent<?>> availabilityChangeEventApplicationListener(){
//        return event -> log.info("the service is healthy {}", event.getState().toString());
//    }
}



//@RequiredArgsConstructor
//class MyApplicationRunner implements ApplicationRunner{
//
//    private final CustomerService customerService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        customerService.add("Miles");
//    }
//}
//@Configuration
//@EnableTransactionManagement
//@PropertySource("classpath:/application.properties")
//class DataConfiguration {
//    private static CustomerService transactionalCustomerService(CustomerService delegate, TransactionTemplate tt) {
//
//        var pfb = new ProxyFactoryBean();
//        pfb.setTarget(delegate);
//        pfb.setProxyTargetClass(true);
//        pfb.addAdvice((MethodInterceptor) invocation -> {
//            var method = invocation.getMethod();
//            var args = invocation.getArguments();
//            tt.execute(status -> {
//                try {
//                    return method.invoke(delegate, args);
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                } catch (InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            return null;
//        });
//
//        return (CustomerService) pfb.getObject();
//    }
//
////    @Bean
////    CustomerService defaultCustomerService(TransactionTemplate tt, JdbcTemplate jdbcTemplate){
////        return transactionalCustomerService(new DefaultCustomerService(jdbcTemplate), tt);
////    }
//
////    @Bean
////    TransactionBeanProcessor transactionBeanProcessor(BeanFactory beanFactory) {
////        return new TransactionBeanProcessor(beanFactory);
////    }
//
////    static class TransactionBeanProcessor implements BeanPostProcessor {
////        private final BeanFactory beanFactory;
////
////        public TransactionBeanProcessor(BeanFactory beanFactory) {
////            this.beanFactory = beanFactory;
////        }
////
////        @Override
////        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
////            if (bean instanceof CustomerService cs) {
////                return transactionalCustomerService(cs, beanFactory.getBean(TransactionTemplate.class));
////            }
////            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
////        }
////    }
//
////    @Bean
////    TransactionTemplate transactionTemplate(PlatformTransactionManager ptm) {
////        return new TransactionTemplate(ptm);
////    }
////
////    @Bean
////    DataSourceTransactionManager transactionManager(DataSource dataSource) {
////        return new DataSourceTransactionManager(dataSource);
////    }
////
////    @Bean
////    JdbcTemplate jdbcTemplate(DataSource dataSource) {
////        var template = new JdbcTemplate(dataSource);
////        template.afterPropertiesSet();
////
////        return template;
////    }
////
////    @Bean
////    DriverManagerDataSource dataSource(Environment environment) {
////        var driverSource = new DriverManagerDataSource(
////                environment.getProperty("spring.datasource.url"),
////                environment.getProperty("spring.datasource.username"),
////                environment.getProperty("spring.datasource.password"));
////
////        driverSource.setDriverClassName(Driver.class.getName());
////
////        return driverSource;
////    }
//
//
//}

