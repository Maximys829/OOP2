package tests;
import bots.BotTest;
import main.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class AbilitiesTest {

    AbilitiesTest() throws IOException {
    }

    @Test
    public void ComissarCheckGood() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Comissar");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Good");
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals("good", b1.getRes());
    }
    @Test
    public void ComissarCheckEvil() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Comissar");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Evil");
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals("evil", b1.getRes());
    }
    @Test
    public void ComissarCheckUnderLover() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Comissar");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p1"));
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p2.getRole().tryToActivateRole(lst, log);
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals("", b1.getRes());
    }
    @Test
    public void DocProtUnderLover() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Doctor");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p1"));
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p2.getRole().tryToActivateRole(lst, log);
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals("", b1.getRes());
    }
    @Test
    public void DonShot() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();

        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();

        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals(false, b2.isAlive());
    }
    @Test
    public void DocProt() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Doctor");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p2"));
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p2.getRole().tryToActivateRole(lst, log);
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals(true, b2.isAlive());
    }
    @Test
    public void DonShotUnderLover() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose(List.of("p1"));
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p2.getRole().tryToActivateRole(lst, log);
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals(true, b2.isAlive());
    }
    @Test
    public void DonJumpAfterDeathShot() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p1"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Evil");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.night();
        Assertions.assertEquals("Don", p2.getRole().getRole());
    }
    @Test
    public void DonJumpAfterDeathNom() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p1"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Evil");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.nominations();
        Assertions.assertEquals("Don", p2.getRole().getRole());
    }
    @Test
    public void DonShotDead() throws IOException {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Don");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose(List.of("p2"));
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Evil");
        PlayerWrapper p3 = Game.getWiredTestWrapper("p3", "Good");
        PlayerWrapper p4 = Game.getWiredTestWrapper("p4", "Good");
        PlayerWrapper p5 = Game.getWiredTestWrapper("p5", "Good");

        List<PlayerWrapper> lst = List.of(p1, p2, p3, p4, p5);
        Game game = new Game(lst);
        game.nominations();
        game.night();
        Assertions.assertEquals(4, game.aliveCounter());
    }

}
