package main;

import java.util.List;
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

public abstract class Role{
    private String alignment;
    private String role;
    private Player player;
    private PlayerGameStatus status;
    Role(Player player){
        this.player = player;
        this.status = new PlayerGameStatus(player);
    }
    Role(){}
    final public void tryToActivateRole(List<PlayerWrapper> playersWrappers, GameLog log){
        if(this.getPlayerGameStatus().isHealthy()&&this.getPlayerGameStatus().isAlive()){
            log.addLog(activateRole(playersWrappers));
        }
    }
    abstract public String activateRole(List<PlayerWrapper> playersWrappers);
    public Player getPlayer(){
        return player;
    }
    public PlayerGameStatus getPlayerGameStatus(){
        return status;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getAlignment(){
        return alignment;
    }
    public void setAlignment(String alignment){
        this.alignment = alignment;
    }
    public void setPlayer(Player player){
        this.player = player;
    }
    public void setPlayerGameStatus(PlayerGameStatus status){
        this.status = status;
    }
    public static Role parseRole(String role){
        switch(role){
            case "Comissar":
                return new Comissar();
            case "Don":
                return new Don();
            case "Evil":
                return new Evil();
            case "Good":
                return new Good();
            case "Doctor":
                return new Doctor();
            case "Lover":
                return new Lover();
        }
        return null;
    }
    static public Player getPlayerFromName(String name, List<PlayerWrapper> playersWrappers){
        for(PlayerWrapper playerWrapper : playersWrappers){
            if(playerWrapper.getPlayer().getName().equals(name)){
                return playerWrapper.getPlayer();
            }
        }
        return null;
    }
    final public PlayerWrapper getPlayerWrapper(Player player, List<PlayerWrapper> playersWrappers) {
        for (PlayerWrapper p : playersWrappers) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }
    final public PlayerWrapper getTargetPlayerWrapper(List<PlayerWrapper> playersWrappers) {
        return getPlayerWrapper(getPlayerFromName(this.getPlayer().getRoleUsed(), playersWrappers), playersWrappers);
    }
}

class Comissar extends Role{
    Comissar(){super();
        setRole("main.Comissar");
        setAlignment("good");}
    Comissar(Player player){
        super( player);
        setRole("main.Comissar");
        setAlignment("good");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {
        PlayerWrapper target = getPlayerWrapper(getPlayerFromName(this.getPlayer().getRoleUsed(), playersWrappers), playersWrappers);
        getPlayer().getRoleUsed(target.getPlayerGameStatus().getAlignment());
        return this.getPlayer().getName()+" learned alignment of "+target.getPlayer().getName()+": "+target.getPlayerGameStatus().getAlignment();
    }
}
class Don extends Role{
    Don(){super();setRole("main.Don");
        setAlignment("evil");}
    Don( Player player){
        super( player);
        setRole("main.Don");
        setAlignment("evil");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {
        PlayerWrapper targetWrapper = getTargetPlayerWrapper(playersWrappers);
        return this.getPlayer().getName()+" "+ targetWrapper.getPlayerGameStatus().kill();

    }
}
class Evil extends Role{
    Evil(){super();setRole("main.Evil");
        setAlignment("evil");}
    Evil(Player player){
        super( player);
        setRole("main.Evil");
        setAlignment("evil");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {return "";}
}
class Good extends Role{
    Good(){super();setRole("main.Good");
        setAlignment("good");}
    Good(Player player){
        super(player);
        setRole("main.Good");
        setAlignment("good");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {return "";}
}
class Doctor extends Role{
    Doctor(){super();setRole("main.Doctor");
        setAlignment("good");}
    Doctor(Player player){
        super( player);
        setRole("main.Doctor");
        setAlignment("good");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {
        PlayerWrapper targetWrapper = getTargetPlayerWrapper(playersWrappers);
        return this.getPlayer().getName()+" "+targetWrapper.getPlayerGameStatus().setDocProt(true);
    }
}
class Lover extends Role{
    Lover(){super();setRole("main.Lover");
        setAlignment("good");}
    Lover(Player player){
        super( player);
        setRole("main.Lover");
        setAlignment("good");
    }
    public String activateRole(List<PlayerWrapper> playersWrappers) {
        PlayerWrapper targetWrapper = getTargetPlayerWrapper(playersWrappers);
        return this.getPlayer().getName()+" "+targetWrapper.getPlayerGameStatus().setLoverSkip(true);

    }
}
