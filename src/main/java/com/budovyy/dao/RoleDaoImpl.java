package com.budovyy.dao;

import com.budovyy.model.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Role getByRolename(String rolename) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Role r where r.roleName=:rolename", Role.class)
                .setParameter("rolename",rolename)
                .uniqueResult();
    }
}
