package iss.bug_application.utils.observer;


import iss.bug_application.utils.events.Event;

public interface Observer<E extends Event> {

    void update(E e);



}
