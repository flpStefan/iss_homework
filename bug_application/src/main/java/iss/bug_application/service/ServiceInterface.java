package iss.bug_application.service;

import iss.bug_application.domain.Bug;
import iss.bug_application.domain.TypeEnum;
import iss.bug_application.domain.User;

public interface ServiceInterface {
    User findUserByData(String username, TypeEnum type);

    Iterable<Bug> getAllBugs();

    void addBug(Bug bug);

    void modifyBug(Bug bug);

    void solveBug(Bug bug);
}
