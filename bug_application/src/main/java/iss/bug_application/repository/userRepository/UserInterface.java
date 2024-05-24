package iss.bug_application.repository.userRepository;

import iss.bug_application.domain.TypeEnum;
import iss.bug_application.domain.User;
import iss.bug_application.repository.RepositoryInterface;

public interface UserInterface extends RepositoryInterface<Long, User> {
    public User findByData(String username, TypeEnum type);
}
