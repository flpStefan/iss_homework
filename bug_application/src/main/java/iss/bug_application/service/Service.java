package iss.bug_application.service;

import iss.bug_application.domain.Bug;
import iss.bug_application.domain.TypeEnum;
import iss.bug_application.domain.User;
import iss.bug_application.repository.bugsRepository.BugsInterface;
import iss.bug_application.repository.userRepository.UserInterface;
import iss.bug_application.utils.events.BugTaskChangeEvent;
import iss.bug_application.utils.observer.Observable;
import iss.bug_application.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable<BugTaskChangeEvent> {
    private BugsInterface bugRepo;
    private UserInterface userRepo;

    private List<Observer<BugTaskChangeEvent>> observers=new ArrayList<>();
    public Service(BugsInterface bugRepo, UserInterface userRepo) {
        this.bugRepo = bugRepo;
        this.userRepo = userRepo;
    }


    public User findUserByData(String username, TypeEnum type) {
        return userRepo.findByData(username, type);
    }

    public Iterable<Bug> getAllBugs() {
        return bugRepo.findAll();
    }

    public void addBug(Bug bug) {
        bugRepo.save(bug);
    }

    public void modifyBug(Bug bug) {
        bugRepo.update(bug);
    }

    public void solveBug(Bug bug) {
        bugRepo.solveBug(bug);
    }


    @Override
    public void addObserver(Observer<BugTaskChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<BugTaskChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(BugTaskChangeEvent t) {
        observers.forEach(x->x.update(t));
    }
}
