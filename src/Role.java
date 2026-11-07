import java.util.List;

abstract class Role{
    private String role;
    private Player player;
    private PlayerGameStatus status;
    Role(Player player){
        this.player = player;
        this.status = new PlayerGameStatus(player);
    }
    abstract public void activateRole(List<Player> players,Player p1);
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
}

class Comissar extends Role{
    Comissar(Player player){

        super( player);
        setRole("Comissar");
    }
    public void activateRole(List<Player> players, Player p1) {
        Player target = this.getPlayer().getRoleUsed();
        getPlayer().getRoleUsed(target.getAlignment());
    }
}
class Don extends Role{
    Don( Player player){
        super( player);
        setRole("Don");
    }
    public void activateRole(List<Player> players, Player p1) {
        Player target = this.getPlayer().getRoleUsed();

    }
}
class Good extends Role{
    Good(Player player){
        super(player);
        setRole("Good");
    }
    public void activateRole(List<Player> players, Player p1) {}
}