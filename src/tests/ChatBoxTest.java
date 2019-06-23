package tests;

import models.ChatBox;
import org.junit.Before;
import org.junit.Test;
import services.FirebaseService;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ChatBoxTest {

    FirebaseService fb;
    ChatBox chat;

    @Before
    public void setUp() throws Exception {
        fb = FirebaseService.getInstance();
        chat = new ChatBox(fb);
    }

    @Test
    public void makeFirstMessage() {
        String testTekst = chat.makeFirstMessage("Thomas");
        String uitkomst = "System: Player  Thomas  joined the game!";
        assertEquals(uitkomst,testTekst);
    }
}