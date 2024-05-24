package iss.bug_application.domain;

import java.util.Objects;

public class Bug extends Entity<Long> {
    private String name;
    private String description;
    private StatusType status;
    private Long resolved_by;

    public Bug(String name, String description, StatusType status, Long resolved_by) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.resolved_by = resolved_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Long getResolved_by() {
        return resolved_by;
    }

    public void setResolved_by(Long resolved_by) {
        this.resolved_by = resolved_by;
    }

    @Override
    public String toString() {
        return "Bug{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", resolved_by=" + resolved_by +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bug bug = (Bug) o;
        return Objects.equals(name, bug.name) && Objects.equals(description, bug.description) && status == bug.status && Objects.equals(resolved_by, bug.resolved_by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, resolved_by);
    }
}
