package com.budovyy.dao;

import com.budovyy.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public User getByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User as u inner join fetch u.roles as r where u.username =:username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public User addUser(User user) {
        Long id = (Long) sessionFactory.getCurrentSession().save(user);
        user.setId(id);
        return user;
    }

    @Override
    public User getByToken(String token) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User u where u.token =:token", User.class)
                //.createNativeQuery("select * from users where u.token = 'token'")
                .setParameter("token", token)
                .uniqueResult();
    }

    public User updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);
        return user;
    }
}