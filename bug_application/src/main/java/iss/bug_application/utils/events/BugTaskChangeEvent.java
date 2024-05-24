package iss.bug_application.utils.events;


import iss.bug_application.domain.Bug;

public class BugTaskChangeEvent extends TaskChangeEvent<Bug> {
    private ChangeEventType type;
    private Bug data, oldData;

    public BugTaskChangeEvent(ChangeEventType type, Bug data) {
        super(type, data);
    }
    public BugTaskChangeEvent(ChangeEventType type, Bug data,  Bug oldData) {
        super(type, data, oldData);
    }
}
