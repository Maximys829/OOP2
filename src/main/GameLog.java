package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameLog {
    List<GameLogString> log = new ArrayList<GameLogString>();
    public GameLog() {

    }
    public void addLog(String log) {
        this.log.add(new GameLogString(log));
    }
    public void addLog(String log, String name) {
        this.log.add(new GameLogString(log, name));
    }
    public List<String> getLog() {
        List<String> list = new ArrayList<>();
        for (GameLogString log : log) {
            list.add(log.getLog());
        }
        return list;
    }
    public void printLog() {
        for (GameLogString s : log) {
            System.out.println(s.getLog());
        }
    }
    public List<String> getLogForPlayer(String name) {
        List<String> list = new ArrayList<>();
        for (GameLogString s : log) {
            if(s.getLogByName(name) != null) {
                list.add(s.getLogByName(name));
            }
        }
        return list;
    }
    public void printLogForPlayer(String name) {
        List<String> list = getLogForPlayer(name);
        for (String s : list) {
            System.out.println(s);
        }
    }
}
class GameLogString{
    private String log;
    private String name;
    GameLogString(String log, String player) {
        this.log = log;
        this.name = player;
    }
    GameLogString(String log) {
        this.log = log;
        this.name = "all";
    }
    public String getLog() {
        return log;
    }
    public String getLogByName(String name) {
        if(Objects.equals(this.name, "all") ||name.equals(this.name)) {
            return this.log;
        }
        return null;
    }
}