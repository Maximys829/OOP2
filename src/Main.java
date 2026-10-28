import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

interface checking{
    public boolean check(Player player);
}
interface saving{
    public void save(Player player);
}
interface whoring{
    public void smh(Player player);
}
interface killing{
    public void kill(Player player);
}
class Player{
    private List<Player> players;
    private List<Status> statuses;
    private String name;
    private boolean alive = true;
    private boolean protection = false;
    public Player(String name) {
        this.name = name;
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
    public void updateStatuses() {
        for (Player player : players) {
            Status st = getStatus(player.getName());
            st.setAlive(player.alive);
            updateSuspicion(st);
        }
    }
    public void updateSuspicion(Status st) {
        st.setSuspicion(st.getSuspicion());
        //
    }
    public void makeAccusations(){
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
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void nominate(Player player) {

    }
}

class BotRandom extends Player {
    private Random rand = new Random();

    public BotRandom(String name) {
        super(name);
    }

    public boolean vote(Player player) {
        return false;
    }
}

class Game {
    private int cnt;
    private List<Player> players;
    private Cond cond = new Cond();
    private int dayCnt = 1;
    public Game(int n) {
        cnt = n;
        players = new ArrayList<Player>();
        for (int i = 0; i < cnt; i++) {
            players.add(new Player("Player " + i));
        }
        for (Player player : players) {
            player.setPlayers(players);
        }

    }
    public void play(){
        while (true){
            switch (cond.getCondition()){
                case "morning":
                    //morning();
                    break;
                case "nominations":
                    nominations();
                    break;
            }
            updateStatuses();
            cond.advanceDay();
        }
    }
    public void morning(){
        for (Player player : players) {
            player.makeAccusations();
        }
    }
    public void nominations(){

    }
    public void updateStatuses() {
        for (Player player : players) {
            player.updateStatuses();
        }
    }

}

class Cond {
    private String condition;
    private final String[] conds = {"morning", "nominations", "execution", "night"};
    private int step = 0;

    public Cond() {
        this.condition = conds[0];
    }

    public void advanceDay() {
        step = step + 1 == conds.length ? 0 : step + 1;
        condition = conds[step];
    }
    public String getCondition() {
        return condition;
    }
}
class Status{
    private double suspicion;//-1 -- 1
    private boolean alive;
    private String name;
    private List<String> nominations = new ArrayList<>();
    private List<HashMap<String, Double>> accusations = new ArrayList<>();
    Status(double suspicion, boolean alive, String name) {
        this.name = name;
        this.suspicion = suspicion;
        this.alive = alive;
    }
    public double getSuspicion() {
        return suspicion;
    }
    public boolean isAlive() {
        return alive;
    }
    public String getName() {
        return name;
    }
    public void setSuspicion(double suspicion) {
        this.suspicion = suspicion;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getNominations() {
        return nominations;
    }
    public void addNomination(String nomination) {
        nominations.add(nomination);
    }
    public List<HashMap<String, Double>> getAccusations() {
        return accusations;
    }
    public void addAccusation(HashMap<String, Double> accusation) {
        accusations.add(accusation);
    }
}

class Main {
    public static void main(String[] args) {
        Game game = new Game(10);
        game.play();
    }
}