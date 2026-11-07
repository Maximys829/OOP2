import java.util.ArrayList;
import java.util.List;

class Player {
    private String alignment;
    private String role;
    private List<Status> statuses;
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
    public boolean vote(Player player) {
        return false;
    }

    public List<Player> getPlayersToVote() {
        return new ArrayList<>();
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public void updateStatuses(RecordsObj records) {
        for (RecordObj record : records.records) {
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

    public String getName() {
        return name;
    }
    public String getAlignment() {
        return alignment;
    }

    public Player getNomination() {
        return null;
    }

    public List<Player> getNominations() {
        return new ArrayList<>();
    }

    public boolean getOpinionOnNominated(Player player) {
        return false;
    }

    public Player getOpinionOnNominated(List<Player> players) {
        return null;
    }
    public Player getRoleUsed(){
        return null;
    }
    public void getRoleUsed(String info) {}
}