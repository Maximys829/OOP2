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
    private List<PlayerWrapper> playersWrappers;
    private List<Player> players;
    private Cond cond = new Cond();
    private RecordsObj records = new RecordsObj();
    private int dayCnt = 1;

    public Game(int n) {
        cnt = n;
        playersWrappers = new ArrayList<PlayerWrapper>();
        for (int i = 0; i < cnt; i++) {
            Player p1 = new Player("Player " + i, "mir", "good");
            playersWrappers.add(new PlayerWrapper(p1, new Good(p1)));
        }
        players = new ArrayList<>();
        for (PlayerWrapper p : playersWrappers) {
            players.add(p.getPlayer());
        }
    }
    public PlayerWrapper getPlayerWrapper(Player player) {
        for (PlayerWrapper p : playersWrappers) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
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
        for (PlayerWrapper player : playersWrappers) {
            if (!player.getPlayer().isAlive()) {
                continue;
            }
            List<Player> nominated = player.getPlayer().getNominations();
            for (Player nominatedPlayer : nominated) {
                if (!nominatedPlayers.contains(nominatedPlayer)) {
                    nominatedPlayers.add(nominatedPlayer);
                    records.addRecord("nomination", player.getPlayer().getName(), 0, nominatedPlayer.getName());
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
            getPlayerWrapper(drawed.getFirst()).getPlayerGameStatus().kill();
            records.addRecord("execution", "None", 0, drawed.getFirst().getName());
        }
    }

    public HashMap<Player, Integer> getVotesOn(List<Player> nominatedPlayers) {
        HashMap<Player, Integer> votes = new HashMap<>();
        for (PlayerWrapper player : playersWrappers) {
            if (!player.getPlayerGameStatus().isAlive()) {
                continue;
            }
            Player attacked = player.getPlayer().getOpinionOnNominated(nominatedPlayers);
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
            player.updateStatuses(records);
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
            if (player.isAlive()&&player.getAlignment()=="evil") {
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



class Main {
    public static void main(String[] args) {
        Game game = new Game(10);
        game.play();
    }
}