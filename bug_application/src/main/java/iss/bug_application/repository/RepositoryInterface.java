package iss.bug_application.repository;

import iss.bug_application.domain.Entity;
import java.sql.SQLException;

public interface RepositoryInterface <ID, E extends Entity<ID>>{

    E findOne(ID id);

    Iterable<E> findAll();

    void save(E entity);

    void delete(ID id);

    void update(E entity);

}
