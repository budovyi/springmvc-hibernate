package com.budovyy.dao;

import com.budovyy.model.Role;

public interface RoleDao {
    Role getByRolename(String rolename);
}
