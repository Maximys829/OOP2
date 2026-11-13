package tests;
import bots.BotTest;
import main.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ModuleTest{
    Game game = new Game();

    ModuleTest() throws IOException {
    }

    @Test
    public void ComissarCheckGood() {
        PlayerWrapper p1 = Game.getWiredTestWrapper("p1", "Comissar");
        BotTest b1 = (BotTest) p1.getPlayer();
        b1.choose("p2");
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
        b1.choose("p2");
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
        b1.choose("p2");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose("p1");
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
        b1.choose("p2");
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
        b1.choose("p2");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Doctor");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose("p2");
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
        b1.choose("p2");
        PlayerWrapper p2 = Game.getWiredTestWrapper("p2", "Lover");
        BotTest b2 = (BotTest) p2.getPlayer();
        b2.choose("p1");
        List<PlayerWrapper> lst = new ArrayList<>();
        lst.add(p2);
        lst.add(p1);
        GameLog log = new GameLog();
        p2.getRole().tryToActivateRole(lst, log);
        p1.getRole().tryToActivateRole(lst, log);

        Assertions.assertEquals(true, b2.isAlive());
    }
}
