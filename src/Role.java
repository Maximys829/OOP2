import java.util.List;

abstract class Role{
    private String alignment;
    private String role;
    private Player player;
    private PlayerGameStatus status;
    Role(Player player){
        this.player = player;
        this.status = new PlayerGameStatus(player);
    }
    Role(){}
    final public void tryToActivateRole(List<PlayerWrapper> playersWrappers){
        if(this.getPlayerGameStatus().isHealthy()&&this.getPlayerGameStatus().isAlive()){
            activateRole(playersWrappers);
        }
    }
    abstract public void activateRole(List<PlayerWrapper> playersWrappers);
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
    final public PlayerWrapper getPlayerWrapper(Player player, List<PlayerWrapper> playersWrappers) {
        for (PlayerWrapper p : playersWrappers) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }
    final public PlayerWrapper getTargetPlayerWrapper(List<PlayerWrapper> playersWrappers) {
        return getPlayerWrapper((Player)this.getPlayer().getRoleUsed(), playersWrappers);
    }
}

class Comissar extends Role{
    Comissar(){super();
        setRole("Comissar");
        setAlignment("good");}
    Comissar(Player player){
        super( player);
        setRole("Comissar");
        setAlignment("good");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {
        Player target = this.getPlayer().getRoleUsed();
        getPlayer().getRoleUsed(target.getAlignment());
    }
}
class Don extends Role{
    Don(){super();setRole("Don");
        setAlignment("evil");}
    Don( Player player){
        super( player);
        setRole("Don");
        setAlignment("evil");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {
        Player target = this.getPlayer().getRoleUsed();
        PlayerWrapper targetWrapper = getPlayerWrapper(target, playersWrappers);
        targetWrapper.getPlayerGameStatus().kill();
    }
}
class Evil extends Role{
    Evil(){super();setRole("Evil");
        setAlignment("evil");}
    Evil(Player player){
        super( player);
        setRole("Evil");
        setAlignment("evil");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {
    }
}
class Good extends Role{
    Good(){super();setRole("Good");
        setAlignment("good");}
    Good(Player player){
        super(player);
        setRole("Good");
        setAlignment("good");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {}
}
class Doctor extends Role{
    Doctor(){super();setRole("Doctor");
        setAlignment("good");}
    Doctor(Player player){
        super( player);
        setRole("Doctor");
        setAlignment("good");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {
        Player target = this.getPlayer().getRoleUsed();
        PlayerWrapper targetWrapper = getPlayerWrapper(target, playersWrappers);
        targetWrapper.getPlayerGameStatus().setDocProt(true);
    }
}
class Lover extends Role{
    Lover(){super();setRole("Lover");
        setAlignment("good");}
    Lover(Player player){
        super( player);
        setRole("Lover");
        setAlignment("good");
    }
    public void activateRole(List<PlayerWrapper> playersWrappers) {
        PlayerWrapper targetWrapper = getTargetPlayerWrapper(playersWrappers);
        targetWrapper.getPlayerGameStatus().setLoverSkip(true);
    }
}
