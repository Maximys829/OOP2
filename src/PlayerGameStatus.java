class PlayerGameStatus {
    private Player player;
    private Boolean docProt = false;
    private Boolean alive = true;
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
}