package bots;

import main.Player;

import java.util.ArrayList;
import java.util.List;

public class BotTest extends Player {
    private List<String> choices = new ArrayList<String>();
    private String res = "";

    public BotTest(String name, String role, String alignment) {
        super(name, role, alignment);
    }

    public List<String> getNominations() {
        return List.of(getChoice());
    }

    public String getOpinionOnNominated(List<String> playerNames) {
        return getChoice();
    }

    public String getRoleUsed() {
        return getChoice();

    }

    public String getChoice() {
        if (choices.size() == 1) {
            return choices.get(0);
        } else if (choices.size() >= 2) {
            String choice = choices.getFirst();
            choices.removeFirst();
            return choice;
        } else {
            return "";
        }
    }

    public void choose(List<String> playerNames) {
        choices.addAll(playerNames);

    }

    public void getRoleUsed(String s) {
        res = s;
    }

    public String getRes() {
        return res;
    }

}