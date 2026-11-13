package bots;

import main.Player;

public class BotTest extends Player{
    private String s;
    private String res = "";
    public BotTest(String name, String role, String alignment){
        super(name, role, alignment);
    }

    public String getRoleUsed() {
        return s;
    }
    public void choose(String s){
        this.s = s;
    }
    public void getRoleUsed(String s){
        res = s;
    }
    public String getRes(){
        return res;
    }
}