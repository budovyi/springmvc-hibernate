package com.budovyy.dao.templates;

import com.budovyy.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

//@Component
@Deprecated
public class UserJdbcTemplate extends JdbcTemplate {

    public UserJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public User getUserByUsername(String username) {
        return queryForObject("SELECT * FROM USERS U WHERE U.USERNAME = ?", new Object[] {username}, new UserMapper());
    }

    private class UserMapper implements RowMapper<User> {

        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("ID"));
            user.setUsername(resultSet.getString("USERNAME"));
            user.setPassword(resultSet.getString("PASSWORD"));
            user.setToken(resultSet.getString("TOKEN"));
            user.setFirstName(resultSet.getString("FIRST_NAME"));
            user.setLastName(resultSet.getString("LAST_NAME"));
            return user;
        }
    }
}
