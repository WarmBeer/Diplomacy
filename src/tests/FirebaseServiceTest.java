package tests;

import services.FirebaseService;

import static org.junit.Assert.*;

public class FirebaseServiceTest {

    FirebaseService fb;

    @org.junit.Before
    public void setUp() throws Exception {
        fb = FirebaseService.getInstance();
        fb.addNewPlayerInFirebase("2323r2aff232x","testplayer");
    }

    @org.junit.Test
    public void playerUIDtoPlayername() {
        String playerName = fb.playerUIDtoPlayername("2323r2aff232x");
        assertEquals("testplayer", playerName);
    }
}