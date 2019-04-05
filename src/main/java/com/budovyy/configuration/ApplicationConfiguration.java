package com.budovyy.configuration;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;
import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.budovyy")
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration extends WebMvcConfigurerAdapter implements WebApplicationInitializer {

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /*@Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("messages");
        return ms;
    }*/

    @Bean
    public DataSource dataSource(Environment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc-driver"));
        dataSource.setUrl(env.getProperty("jdbc-url"));
        dataSource.setUsername(env.getProperty("jdbc-name"));
        dataSource.setPassword(env.getProperty("jdbc-password"));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource, Environment env) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(getHibernateProps(env));
        sessionFactory.setPackagesToScan("com.budovyy.model");
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager tm = new HibernateTransactionManager();
        tm.setSessionFactory(sessionFactory);
        return tm;
    }

    @Bean
    public JavaMailSender mailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("smtp.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("smtp.port")));
        mailSender.setUsername(env.getProperty("email.address"));
        mailSender.setPassword(env.getProperty("email.password"));
        mailSender.setJavaMailProperties(getMailProps(env));
        return mailSender;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.budovyy.configuration");

        container.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = container
                .addServlet("dispatcher", new DispatcherServlet(context));

        container.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private Properties getMailProps(Environment env) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug", env.getProperty("mail.debug"));
        return props;
    }

    private Properties getHibernateProps(Environment env) {
        Properties props = new Properties();
        props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        return props;
    }
}
