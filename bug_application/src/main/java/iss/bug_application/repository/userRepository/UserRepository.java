package iss.bug_application.repository.userRepository;


import iss.bug_application.domain.User;
import iss.bug_application.domain.TypeEnum;
import iss.bug_application.repository.utils.JdbcUtils;
import iss.bug_application.repository.utils.PasswordHashing;
import iss.bug_application.repository.RepositoryInterface;

import java.sql.*;
import java.util.Properties;

public class UserRepository implements UserInterface {
    private JdbcUtils jdbcUtils;

    public UserRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public User findOne(Long aLong) {
        if(aLong == null)
            throw new IllegalArgumentException("Error! Id cannot be null!");


        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from \"USER\" " +
                "where id = ?")){

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                TypeEnum type = TypeEnum.valueOf(resultSet.getString("type"));

                User user = new User(username, password, type);
                user.setId(aLong);

                return user;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public void save(User entity) {
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement prepStatement = con.prepareStatement(
                "INSERT INTO \"USER\" (\"username\",\"password\",\"type\") VALUES (?, ?, ?)")) {
            prepStatement.setString(1, entity.getUsername());
            String hashedPassword = PasswordHashing.hashPassword(entity.getPassword());
            prepStatement.setString(2, hashedPassword);
            prepStatement.setString(3,entity.getType().toString());
            prepStatement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Error from DataBase: " + e);
        }


    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User findByData(String username, TypeEnum type) {
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("select * from \"USER\" " +
                "where \"username\" = ? and \"type\" = ?")){

            statement.setString(1,username );
            statement.setString(2,type.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                User user = new User(username,password,type);
                user.setId(id);

                return user;
            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            System.out.println("error db"+e);
        }

        return null;
    }
}
