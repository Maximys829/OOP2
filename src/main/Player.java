package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private String alignment;
    private String role;
    private List<Status> statuses = new ArrayList<>();
    private String name;
    private boolean alive = true;

    public Player(String name, String role, String alignment) {
        this.name = name;
        this.role = role;
        this.alignment = alignment;
    }

    public final boolean isAlive() {
        return alive;
    }
    public final void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void updateStatuses(RecordsObj records) {
        for (Status status : statuses) {
            for (RecordObj record : records.getDeathRecords()) {
                if(record.getPlayer1().equals(status.getName())) {
                    status.setAlive(false);
                }
            }
        }

    }

    public void updateSuspicion(Status st) {
        st.setSuspicion(st.getSuspicion());
        //
    }

    public void makeAccusations() {
        //
    }

    private Status getStatus(String name) {
        for (Status status : statuses) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }
    final public void setRole(String role) {
        this.role = role;
    }
    final public String getName() {
        return name;
    }
    final public void setName(String newName) {
        this.name = newName;
    }
    final public String getAlignment() {
        return alignment;
    }

    abstract public List<String> getNominations();

    //public boolean getOpinionOnNominated(Player player) {
        //return false;
    //}

    abstract public String getOpinionOnNominated(List<String> playerNames);

    abstract public String getRoleUsed();
    abstract public void getRoleUsed(String info);
    public String getRole() {
        return role;
    }
    public List<Status> getStatuses() {
        return statuses;
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof Player) {
            return name.equals(((Player)o).name);
        }
        return false;
    }
    @Override
    public String toString() {
        return name;
    }
}