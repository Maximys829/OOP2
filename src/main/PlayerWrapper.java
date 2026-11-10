package main;

public class PlayerWrapper {
    private Player player;
    private Role role;
    public PlayerWrapper(Player player, Role role) {
        this.player = player;
        this.role = role;
    }
    public Player getPlayer() {
        return player;
    }
    public Role getRole() {
        return role;
    }
    public PlayerGameStatus getPlayerGameStatus() {
        return role.getPlayerGameStatus();
    }
}