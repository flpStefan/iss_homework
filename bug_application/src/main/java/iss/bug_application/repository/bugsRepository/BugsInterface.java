package iss.bug_application.repository.bugsRepository;

import iss.bug_application.domain.Bug;
import iss.bug_application.repository.RepositoryInterface;

public interface BugsInterface extends RepositoryInterface<Long, Bug> {
    void solveBug(Bug bug);
}
