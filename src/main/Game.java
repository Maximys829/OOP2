package main;

import bots.BotRandom;
import bots.BotTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Game {
    private boolean goodWin = false;
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

    public Game(List<PlayerWrapper> playersWrappers) throws IOException {
        this.playersWrappers = playersWrappers;
        scanner.close();
        scanner = new Scanner(pathWakeOrder);
        while (scanner.hasNext()) {
            String roleS = scanner.next();
            wakeOrder.add(roleS);
        }
        this.cnt = playersWrappers.size();
        players = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            players.add(playersWrappers.get(i).getPlayer());
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

    public boolean play() {
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
            if (checkWin()) {
                break;
            }
            updateStatuses();
            cond.advanceDay();
        }
        //gameLog.printLog();
        return goodWin;
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
            List<Player> nominated = getPlayersByNames(player.getPlayer().getNominations());
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
                gameLog.addLog("Draw between " + drawed.toString());
            } else {
                getPlayerWrapper(drawed.getFirst()).getPlayerGameStatus().kill();
                if (drawed.getFirst().getRole().equals("Don")) {
                    changeDon();
                }
                gameLog.addLog("Executed " + drawed.toString());
            }
        } else if (drawed.size() == 1) {
            getPlayerWrapper(drawed.getFirst()).getPlayerGameStatus().kill();
            if (drawed.getFirst().getRole().equals("Don")) {
                changeDon();
            }
            records.addRecord("execution", "None", 0, drawed.getFirst().getName());
            gameLog.addLog("Executed " + drawed.getFirst().getName());
        } else {
            records.addRecord("no executions", "None", 0, "None");
            gameLog.addLog("No nominations");
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
            PlayerWrapper attackedW = getPlayerWrapperByName(player.getPlayer().getOpinionOnNominated(getPlayerNames(nominatedPlayers)));
            Player attacked = null;
            if (attackedW != null) {
                attacked = attackedW.getPlayer();
            }
            if (attacked == null) {
                attacked = nominatedPlayers.getLast();
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
        if (roleIsDead("Don")) {
            changeDon();
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

//    public int playNomination(Player nominated) {
//        int voteCnt = 0;
//        for (Player player : players) {
//            voteCnt += player.getOpinionOnNominated(nominated) ? 1 : 0;
//        }
//        return voteCnt;
//    }

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

    public void changeDon() {
        for (PlayerWrapper pw : playersWrappers) {
            if (pw.getRole().getRole().equals("Evil") && pw.getPlayerGameStatus().isAlive()) {
                Role role = Role.parseRole("Don");
                role.setPlayer(pw.getPlayer());
                role.setPlayerGameStatus(pw.getPlayerGameStatus());
                pw.setRole(role);

                break;
            }
        }
    }

    public boolean roleIsDead(String role) {
        for (PlayerWrapper pw : playersWrappers) {
            if (pw.getRole().getRole().equals(role) && pw.getPlayerGameStatus().isAlive()) {
                return false;
            }
        }
        return true;
    }

    public List<String> getPlayerNames(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public PlayerWrapper getPlayerWrapperByName(String playerName) {
        for (PlayerWrapper pw : playersWrappers) {
            if (pw.getPlayer().getName().equals(playerName)) {
                return pw;
            }
        }
        return null;
    }

    public List<Player> getPlayersByNames(List<String> playerNames) {
        List<Player> players = new ArrayList<>();
        for (PlayerWrapper pw : playersWrappers) {
            if (playerNames.contains(pw.getPlayer().getName())) {
                players.add(pw.getPlayer());
            }
        }
        return players;
    }

    public boolean checkWin() {
        if (aliveCounter() <= aliveEvilCounter() * 2) {
            gameLog.addLog("EVIL WON");
            goodWin = false;
            return true;
        } else if (aliveEvilCounter() == 0) {
            gameLog.addLog("GOOD WON");
            goodWin = true;
            return true;
        }
        return false;
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