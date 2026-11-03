import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

interface checking {
    public boolean check(Player player);
}

interface saving {
    public void save(Player player);
}

interface whoring {
    public void smh(Player player);
}

interface killing {
    public void kill(Player player);
}

class Player {
    private String alignment;
    private String role;
    private List<Player> players;
    private List<Status> statuses;
    private String name;
    private boolean alive = true;
    private boolean protection = false;

    public Player(String name, String role, String alignment) {
        this.name = name;
        this.role = role;
        this.alignment = alignment;
    }
    public final void execute() {
        alive = false;
    }

    public final boolean isAlive() {
        return alive;
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
            updateSuspicion(st);
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
    public void setPlayers(List<Player> players) {
        this.players = players;
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

class BotRandom extends Player {
    private Random rand = new Random();

    public BotRandom(String name, String role, String alignment) {
        super(name, role, alignment);
    }

    public boolean vote(Player player) {
        return false;
    }
}

class Game {
    private List<String> roleSet = new ArrayList<>();
    private int cnt;
    private List<Player> players;
    private Cond cond = new Cond();
    private RecordsObj records = new RecordsObj();
    private int dayCnt = 1;

    public Game(int n) {
        cnt = n;
        players = new ArrayList<Player>();
        for (int i = 0; i < cnt; i++) {
            players.add(new Player("Player " + i, "mir", "good"));
        }
        for (Player player : players) {
            player.setPlayers(players);
        }

    }

    public void play() {
        while (true) {
            switch (cond.getCondition()) {
                case "morning":
                    morning();
                    break;
                case "nominations":
                    nominations();
                    break;
                case "night":
                    night();
                    break;
            }
            if(aliveCounter()<=aliveEvilCounter()*2){
                System.out.println("EVIL WON");
                break;
            } else if (aliveEvilCounter()==0){
                System.out.println("GOOD WON");
                break;
            }
            updateStatuses();
            cond.advanceDay();
        }
    }

    public void morning() {
        for (Player player : players) {
            player.makeAccusations();
        }
    }

    public void nominations() {
        List<Player> nominatedPlayers = new ArrayList<>();
        for (Player player : players) {
            if (!player.isAlive()) {
                continue;
            }
            List<Player> nominated = player.getNominations();
            for (Player nominatedPlayer : nominated) {
                if (!nominatedPlayers.contains(nominatedPlayer)) {
                    nominatedPlayers.add(nominatedPlayer);
                    records.addRecord("nomination", player.getName(), 0, nominatedPlayer.getName());
                }
            }
        }

        HashMap<Player, Integer> votes = getVotesOn(nominatedPlayers);

        List<Player> drawed = getMaxedNominated(votes);

        if (drawed.size() > 1) {
            votes = getVotesOn(drawed);
            drawed = getMaxedNominated(votes);
            if (drawed.size() > 1) {
                records.addRecord("draw", "None", 0, "None");
            }
        } else {
            drawed.getFirst().execute();
            records.addRecord("execution", "None", 0, drawed.getFirst().getName());
        }
    }

    public HashMap<Player, Integer> getVotesOn(List<Player> nominatedPlayers) {
        HashMap<Player, Integer> votes = new HashMap<>();
        for (Player player : players) {
            if (!player.isAlive()) {
                continue;
            }
            Player attacked = player.getOpinionOnNominated(nominatedPlayers);
            if (!votes.containsKey(attacked)) {
                votes.put(attacked, 0);
            }
            votes.put(attacked, votes.get(attacked) + 1);
        }
        return votes;
    }

    public List<Player> getMaxedNominated(HashMap<Player, Integer> votes) {
        int voteCnt = 0;
        List<Player> drawed = new ArrayList<>();
        for (Player player : votes.keySet()) {
            Integer voteP = votes.get(player);
            if (voteP > voteCnt) {
                voteCnt = voteP;
                drawed = new ArrayList<>();
                drawed.add(player);
            } else if (voteP == voteCnt) {
                drawed.add(player);
            }
        }
        return drawed;
    }
    public void night() {
        // to do: activate all roles in order
    }
    public void updateStatuses() {
        for (Player player : players) {
            player.updateStatuses();
        }
    }
    public int aliveCounter(){
        int aliveCnt = 0;
        for (Player player : players) {
            if (player.isAlive()) {
                aliveCnt++;
            }
        }
        return aliveCnt;
    }
    public int aliveEvilCounter(){
        int aliveCnt = 0;
        for (Player player : players) {
            if (player.isAlive()) {
                aliveCnt++;
            }
        }
        return aliveCnt;
    }
    public int playNomination(Player nominated) {
        int voteCnt = 0;
        for (Player player : players) {
            voteCnt += player.getOpinionOnNominated(nominated) ? 1 : 0;
        }
        return voteCnt;
    }

    public void execute(Player player) {

    }
}

class RecordsObj {
    List<RecordObj> records;
    List<RecordObj> recordsNoms;
    List<RecordObj> recordsAccuses;

    public RecordsObj() {
        records = new ArrayList<>();
        recordsNoms = new ArrayList<>();
        recordsAccuses = new ArrayList<>();
    }

    public void addRecord(String type, String p1, double x, String p2) {
        RecordObj record = new RecordObj(type, p1, x, p2);
        records.add(record);
        switch (type) {
            case "nomination":
                recordsNoms.add(record);
                break;
            case "accusation":
                recordsAccuses.add(record);
                break;
        }
    }

    public List<RecordObj> getRecords() {
        return records;
    }

    public List<RecordObj> getNomRecords() {
        return recordsNoms;
    }

    public List<RecordObj> getAccuseRecords() {
        return recordsAccuses;
    }
}

class RecordObj {
    private String type;
    private String player1;
    private String player2;
    private double score;

    public RecordObj(String type, String player1, double score, String player2) {
        this.type = type;
        this.player1 = player1;
        this.score = score;
        this.player2 = player2;
    }

    public String getType() {
        return type;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public double getScore() {
        return score;
    }
}

class Cond {
    private String condition;
    private final String[] conds = {"morning", "nominations", "night"};
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

class Status {
    private double suspicion;//-1 -- 1
    private String name;
    private List<String> nominations = new ArrayList<>();
    private List<HashMap<String, Double>> accusations = new ArrayList<>();

    Status(double suspicion, String name) {
        this.name = name;
        this.suspicion = suspicion;
    }

    public double getSuspicion() {
        return suspicion;
    }

    public String getName() {
        return name;
    }

    public void setSuspicion(double suspicion) {
        this.suspicion = suspicion;
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