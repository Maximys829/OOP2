package main;

public class PlayerGameStatus {
    private Player player;
    private Boolean docProt = false;
    private Boolean alive = true;
    private Boolean loverSkip = false;
    private String alignment;
    public PlayerGameStatus(Player player) {
        this.player = player;
        this.alignment = player.getAlignment();
    }
    public String setDocProt(Boolean docProt) {
        this.docProt = docProt;
        return "protected "+player.getName();
    }
    public String kill() {
        if (!docProt) {
            alive = false;
            player.setAlive(false);
            return "killed "+player.getName();
        }
        return "failed to kill "+player.getName()+" due doctor";
    }
    public Boolean isAlive() {
        return alive;
    }
    public Boolean isHealthy(){
        return !loverSkip;
    }
    public String setLoverSkip(Boolean loverSkip) {
        this.loverSkip = loverSkip;
        return "blocked "+player.getName();
    }
    public void morningRoutine(){
        this.docProt = false;
        this.loverSkip = false;
    }
    public String getAlignment() {
        return alignment;
    }
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }
}