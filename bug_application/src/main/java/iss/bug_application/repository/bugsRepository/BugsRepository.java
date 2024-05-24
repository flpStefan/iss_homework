package iss.bug_application.repository.bugsRepository;


import iss.bug_application.domain.Bug;
import iss.bug_application.domain.StatusType;
import iss.bug_application.repository.utils.JdbcUtils;
import iss.bug_application.repository.RepositoryInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class BugsRepository implements BugsInterface {
    private JdbcUtils jdbcUtils;

    public BugsRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }


    @Override
    public Bug findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Bug> findAll() {
        List<Bug> bugs = new ArrayList<>();
        Connection con = jdbcUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from \"BUG\" ")){

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                StatusType status = StatusType.valueOf(resultSet.getString("status"));
                Long solved_by = resultSet.getLong("resolved_by");

                Bug bug = new Bug(name, description, status, solved_by);
                bug.setId(id);
                bugs.add(bug);

            }

            return bugs;
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Bug entity) {
        Connection con = jdbcUtils.getConnection();

        try(PreparedStatement prepStatement = con.prepareStatement("insert into \"BUG\"(\"name\",\"description\",\"status\",\"resolved_by\") "
                + "values (?,?,?,?)")){
            prepStatement.setString(1,entity.getName());
            prepStatement.setString(2,entity.getDescription());
            prepStatement.setString(3,entity.getStatus().toString());
            prepStatement.setNull(4, java.sql.Types.BIGINT);

            int affectedRows = prepStatement.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Bug entity) {
        if(entity.getId() == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("update \"BUG\" " +
                "set \"name\" = ?,\"description\" = ? where \"id\" = ?")) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getDescription());
            statement.setInt(3, entity.getId().intValue());

            int affectedRows = statement.executeUpdate();
            if(affectedRows != 0) System.out.println("updated succesfully");
            else System.out.println("could not update");
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void solveBug(Bug bug) {
        if(bug.getId() == null) {
            throw new IllegalArgumentException("Error! Id cannot be null!");
        }

        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement statement = con.prepareStatement("update \"BUG\" " +
                "set \"status\" = ?,\"resolved_by\" = ? where \"id\" = ?")) {

            statement.setString(1, bug.getStatus().toString());
            statement.setLong(2, bug.getResolved_by());
            statement.setInt(3, bug.getId().intValue());

            int affectedRows = statement.executeUpdate();
            if(affectedRows != 0) System.out.println("updated succesfully");
            else System.out.println("could not update");
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
