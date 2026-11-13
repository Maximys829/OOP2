package main;

import bots.BotRandom;
import bots.BotTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Game {
    private Long seed = (long) 1.;
    private Random random = new Random(seed);
    private int cnt;
    private List<String> wakeOrder = new ArrayList<>();
    private List<PlayerWrapper> playersWrappers;
    private List<Player> players;
    private Cond cond = new Cond();
    private RecordsObj records = new RecordsObj();
    private int dayCnt = 1;
    private Path pathConfig = Path.of("src/config/gameConfig.txt");
    private Path pathWakeOrder = Path.of("src/config/gameWakeOrder.txt");
    private Scanner scanner = new Scanner(pathConfig);
    private GameLog gameLog = new GameLog();

    public Game() throws IOException {
        cnt = scanner.nextInt();
        playersWrappers = new ArrayList<PlayerWrapper>();
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            String roleS = scanner.next();
            for (int i = 0; i < n; i++) {
                Role role = Role.parseRole(roleS);
                Player p1 = new BotRandom("Player", roleS, role.getAlignment(), seed);
                role.setPlayer(p1);
                role.setPlayerGameStatus(new PlayerGameStatus(p1));
                PlayerWrapper pw = new PlayerWrapper(p1, role);
                playersWrappers.add(pw);
            }
        }
        players = new ArrayList<>();
        Collections.shuffle(playersWrappers, random);
        for (int i = 0; i < cnt; i++) {
            playersWrappers.get(i).getPlayer().setName("Player_" + i);
            players.add(playersWrappers.get(i).getPlayer());
        }
        for (int i = 0; i < cnt; i++) {
            Player p1 = playersWrappers.get(i).getPlayer();
            List<Status> statuses1 = new ArrayList<>();
            for (int j = 0; j < cnt; j++) {
                statuses1.add(new Status(0, players.get(j).getName()));
            }
            p1.setStatuses(statuses1);
        }
        scanner.close();
        scanner = new Scanner(pathWakeOrder);
        while (scanner.hasNext()) {
            String roleS = scanner.next();
            wakeOrder.add(roleS);
        }
    }
    public static PlayerWrapper getWiredTestWrapper(String name, String roleS) {
        Role role = Role.parseRole(roleS);
        Player p1 = new BotTest(name, roleS, role.getAlignment());
        role.setPlayer(p1);
        role.setPlayerGameStatus(new PlayerGameStatus(p1));
        PlayerWrapper pw = new PlayerWrapper(p1, role);
        return pw;
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
                    System.out.println(dayCnt + " noms ended");
                    registerDeaths();
                    break;
                case "night":
                    if (dayCnt == 1) {
                        firstNight();
                    }
                    night();
                    gameLog.addLog("Ended day " + dayCnt);
                    printTable();
                    dayCnt++;
                    registerDeaths();
                    break;
            }
            if (aliveCounter() <= aliveEvilCounter() * 2) {
                System.out.println("EVIL WON");
                break;
            } else if (aliveEvilCounter() == 0) {
                System.out.println("GOOD WON");
                break;
            }
            updateStatuses();
            cond.advanceDay();
        }
        gameLog.printLog();
    }

    public void morning() {
        for (PlayerWrapper playerWrapper : playersWrappers) {
            playerWrapper.getPlayerGameStatus().morningRoutine();
            playerWrapper.getPlayer().makeAccusations();
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
        } else if (drawed.size() == 1) {
            getPlayerWrapper(drawed.getFirst()).getPlayerGameStatus().kill();
            records.addRecord("execution", "None", 0, drawed.getFirst().getName());
        } else {
            records.addRecord("no executions", "None", 0, "None");
        }
    }

    public HashMap<Player, Integer> getVotesOn(List<Player> nominatedPlayers) {
        HashMap<Player, Integer> votes = new HashMap<>();
        if (nominatedPlayers.isEmpty()) {
            return votes;
        }
        for (PlayerWrapper player : playersWrappers) {
            if (!player.getPlayerGameStatus().isAlive()) {
                continue;
            }
            Player attacked = player.getPlayer().getOpinionOnNominated(nominatedPlayers);
            if (attacked == null) {
                attacked = nominatedPlayers.get(nominatedPlayers.size() - 1);
            }
            if (!votes.containsKey(attacked)) {
                votes.put(attacked, 0);
            }
            votes.put(attacked, votes.get(attacked) + 1);
        }
        System.out.println(votes);
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
        for (String roleS : wakeOrder) {
            for (PlayerWrapper playerWrapper : playersWrappers) {
                if (playerWrapper.getPlayer().getRole().equals(roleS)) {
                    playerWrapper.getRole().tryToActivateRole(playersWrappers, gameLog);
                }
            }
        }
    }

    public void firstNight() {
        StringBuilder s = new StringBuilder("evil:");
        List<Player> evilPlayers = new ArrayList<>();
        for (PlayerWrapper playerWrapper : playersWrappers) {
            if (playerWrapper.getPlayer().getAlignment().equals("evil")) {
                s.append(" ").append(playerWrapper.getPlayer().getName());
                evilPlayers.add(playerWrapper.getPlayer());
            }
        }
        System.out.println(s);
        for (Player player : evilPlayers) {
            player.getRoleUsed(s.toString());
        }

    }

    public void updateStatuses() {
        for (Player player : players) {
            player.updateStatuses(records);
        }
    }

    public int aliveCounter() {
        int aliveCnt = 0;
        for (Player player : players) {
            if (player.isAlive()) {
                aliveCnt++;
            }
        }
        return aliveCnt;
    }

    public int aliveEvilCounter() {
        int aliveCnt = 0;
        for (Player player : players) {
            if (player.isAlive() && Objects.equals(player.getAlignment(), "evil")) {
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

    public void printTable() {
        for (Player player : players) {
            System.out.println(player.getName() + " " + player.getRole() + " " + (player.isAlive() ? "alive" : "dead"));
        }
    }

    public void registerDeaths() {
        for (PlayerWrapper playerWrapper : playersWrappers) {
            if (!playerWrapper.getPlayer().isAlive()) {
                if (!records.playerIsDead(playerWrapper.getPlayer().getName())) {
                    records.addRecord("death", playerWrapper.getPlayer().getName(), 0, "");
                }
            }
        }
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
        if (step == conds.length - 1) {
            step = 0;
        } else {
            step++;
        }
        condition = conds[step];
    }

    public String getCondition() {
        return condition;
    }
}