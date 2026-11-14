package tests;
import bots.BotTest;
import main.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NominationsTest {


    NominationsTest() throws IOException {
    }
    @Test
    public void endGameGoodWin() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Comissar");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);

        Game game = new Game(lst);
        Assertions.assertEquals(true, game.play());
    }
    @Test
    public void endGameEvilWin() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Evil");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);

        Game game = new Game(lst);
        Assertions.assertEquals(false, game.play());
    }
    @Test
    public void endGameGoodWinThroughNoms() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Evil");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p1"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3);

        Game game = new Game(lst);
        Assertions.assertEquals(true, game.play());
    }
    @Test
    public void endGameEvilWinThroughNoms() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Evil");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3);

        Game game = new Game(lst);
        Assertions.assertEquals(false, game.play());
    }
    @Test
    public void endGameEvilWinThroughNomsAndKill() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Good");
        BotTest b1 = (BotTest) p1.getPlayer();
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Evil");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p3", "p1"));
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4);
        Game game = new Game(lst);

        Assertions.assertEquals(false, game.play());
    }
    @Test
    public void endGameGoodWinThroughNomsAndKillAndNoms() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p3", "p3", "p4"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p3", "p3", "p1"));
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);

        Assertions.assertEquals(true, game.play());
    }
    @Test
    public void drawNoKill() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p3", "p3", "p3", "p3", "p4"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p3", "p3","p3", "p3", "p1"));
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        BotTest b3 = (BotTest) p3.getPlayer();
        b3.choose(List.of("p4", "p4"));
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        BotTest b4 = (BotTest) p4.getPlayer();
        b4.choose(List.of("p4","p4"));

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4);
        Game game = new Game(lst);
        game.nominations();
        Assertions.assertEquals(4, game.aliveCounter());
    }
    @Test
    public void drawKill() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p3", "p3"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p3", "p3"));
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        BotTest b3 = (BotTest) p3.getPlayer();
        b3.choose(List.of("p4", "p4"));
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        BotTest b4 = (BotTest) p4.getPlayer();
        b4.choose(List.of("p4","p4"));
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");
        BotTest b5 = (BotTest) p5.getPlayer();
        b5.choose(List.of("p1","p1", "p3"));

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.nominations();
        Assertions.assertEquals(4, game.aliveCounter());
    }
    @Test
    public void noNomNoKill() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.nominations();
        Assertions.assertEquals(5, game.aliveCounter());
    }
    @Test
    public void diffInNomAndVote() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p1", "p1"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p3", "p1"));
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        BotTest b3 = (BotTest) p3.getPlayer();
        b3.choose(List.of("p3", "p1"));
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        BotTest b4 = (BotTest) p4.getPlayer();
        b4.choose(List.of("p3"));
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");
        BotTest b5 = (BotTest) p5.getPlayer();
        b5.choose(List.of("p3"));

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.nominations();
        Assertions.assertEquals(false, p1.getPlayerGameStatus().isAlive());
    }
}