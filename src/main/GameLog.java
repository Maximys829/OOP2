package main;

import java.util.ArrayList;
import java.util.List;

public class GameLog {
    List<String> log = new ArrayList<String>();
    public GameLog() {

    }
    public void addLog(String log) {
        this.log.add(log);
    }
    public List<String> getLog() {
        return log;
    }
    public void printLog() {
        for (String s : log) {
            System.out.println(s);
        }
    }
}