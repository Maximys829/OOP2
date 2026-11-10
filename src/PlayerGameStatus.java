class PlayerGameStatus {
    private Player player;
    private Boolean docProt = false;
    private Boolean alive = true;
    private Boolean loverSkip = false;
    PlayerGameStatus(Player player) {
        this.player = player;
    }
    public void setDocProt(Boolean docProt) {
        this.docProt = docProt;
    }
    public void kill() {
        if (!docProt) {
            alive = false;
            player.setAlive(false);
        }
    }
    public Boolean isAlive() {
        return alive;
    }
    public Boolean isHealthy(){
        return !loverSkip;
    }
    public void setLoverSkip(Boolean loverSkip) {
        this.loverSkip = loverSkip;
    }
    public void morningRoutine(){
        this.docProt = false;
        this.loverSkip = false;
    }
}