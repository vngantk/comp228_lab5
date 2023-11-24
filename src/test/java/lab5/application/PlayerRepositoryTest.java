package lab5.application;

import lab5.domain.Player;
import lab5.infrastructure.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerRepositoryTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void testConstructPlayer() {
        Player player = new Player(0, "Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        assertEquals("Peter", player.getFirstName());
        assertEquals("Pan", player.getLastName());
        assertEquals("1 Paradise Road", player.getAddress());
        assertEquals("Z9Z 9Z9", player.getPostalCode());
        assertEquals("Galaxy", player.getProvince());
        assertEquals("(999)9999-999)", player.getPhoneNumber());
        assertThrows(IllegalArgumentException.class, () ->
            new Player(0, null, "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)")
        );
        assertThrows(IllegalArgumentException.class, () ->
            new Player(0, "Peter", null, "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)")
        );
        assertThrows(IllegalArgumentException.class, () ->
            new Player(0, "Peter", "Pan", null, "Z9Z 9Z9", "Galaxy", "(999)9999-999)")
        );
        assertThrows(IllegalArgumentException.class, () ->
            new Player(0, "Peter", "Pan", "1 Paradise Road", null, null, null)
        );
    }


    @Test
    void testCreatePlayerWithValidTitle() {
        Player createdPlayer = repository.createPlayer("Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        assertNotNull(createdPlayer);
        assertEquals("Peter", createdPlayer.getFirstName());
        assertEquals("Pan", createdPlayer.getLastName());
        assertEquals("1 Paradise Road", createdPlayer.getAddress());
        assertEquals("Z9Z 9Z9", createdPlayer.getPostalCode());
        assertEquals("Galaxy", createdPlayer.getProvince());
        assertEquals("(999)9999-999)", createdPlayer.getPhoneNumber());
        Player player = repository.getPlayerById(createdPlayer.getId());
        assertEquals(createdPlayer, player);
    }


    @Test
    void testCreateMultiplePlayers() {
        Player player1 = repository.createPlayer("Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player2 = repository.createPlayer("John", "Doe", "2 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player3 = repository.createPlayer("Paul", "Smith", "3 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        assertNotEquals(player1, player2);
        assertNotEquals(player1, player3);
        assertNotEquals(player2, player3);
        assertNotEquals(player1.getId(), player2.getId());
        assertNotEquals(player1.getId(), player3.getId());
        assertNotEquals(player2.getId(), player3.getId());
        Player[] players = repository.getAllPlayers();
        Set<Player> playerSet = new HashSet<>(Arrays.asList(players));
        assertEquals(3, players.length);
        assertTrue(playerSet.contains(player1));
        assertTrue(playerSet.contains(player2));
        assertTrue(playerSet.contains(player3));
    }

    @Test
    void testUpdateAnExistingPlayer() {
        Player player1 = repository.createPlayer("Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player2 = repository.createPlayer("John", "Doe", "2 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player3 = repository.createPlayer("Paul", "Smith", "3 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player updatedPlayer = repository.updatePlayer(player2.getId(), "John", "William", "2 Heaven Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        assertEquals(player2.getId(), updatedPlayer.getId());
        assertEquals("John", updatedPlayer.getFirstName());
        assertEquals("William", updatedPlayer.getLastName());
        assertEquals("2 Heaven Road", updatedPlayer.getAddress());
        assertEquals("Z9Z 9Z9", updatedPlayer.getPostalCode());
        assertEquals("Galaxy", updatedPlayer.getProvince());
        assertEquals("(999)9999-999)", updatedPlayer.getPhoneNumber());
        Player[] players = repository.getAllPlayers();
        Set<Player> playerSet = new HashSet<>(Arrays.asList(players));
        assertEquals(3, players.length);
        assertTrue(playerSet.contains(player1));
        assertTrue(playerSet.contains(updatedPlayer));
        assertTrue(playerSet.contains(player3));
        assertFalse(playerSet.contains(player2));

    }

    @Test
    void testPlayerEqualsHashCodeAndToString() {
        Player player1 = new Player(0, "Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player2 = new Player(0, "Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player3 = new Player(1, "Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player4 = new Player(0, "John", "Doe", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");

        assertEquals(player1, player2);
        assertNotEquals(player1, player3);
        assertNotEquals(player1, player4);
        assertNotEquals(player1, null);
        assertNotEquals(null, player1);
        assertNotEquals(player1, new Object());
        assertEquals(player1.hashCode(), player1.hashCode());
        String str = "Player{" +
            "id='" + player1.getId() + '\'' +
            ", firstName='" + player1.getFirstName() + '\'' +
            ", lastName='" + player1.getLastName() + '\'' +
            ", address='" + player1.getAddress() + '\'' +
            ", postalCode='" + player1.getPostalCode() + '\'' +
            ", province='" + player1.getProvince() + '\'' +
            ", phoneNumber='" + player1.getPhoneNumber() + '\'' +
            '}';
        assertEquals(str, player1.toString());
    }

    @Test
    void testUpdateANonExistingPlayer() {
        Player updatedPlayer = repository.updatePlayer(0, "Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        assertNull(updatedPlayer);
    }

    @Test
    void testDeleteAnExistingPlayer() {
        Player player1 = repository.createPlayer("Peter", "Pan", "1 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player2 = repository.createPlayer("John", "Doe", "2 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");
        Player player3 = repository.createPlayer("Paul", "Smith", "3 Paradise Road", "Z9Z 9Z9", "Galaxy", "(999)9999-999)");

        assertTrue(repository.deletePlayer(player2.getId()));
        Player[] players = repository.getAllPlayers();
        Set<Player> playerSet = new HashSet<>(Arrays.asList(players));
        assertEquals(2, players.length);
        assertTrue(playerSet.contains(player1));
        assertTrue(playerSet.contains(player3));
        assertFalse(playerSet.contains(player2));
    }

    @Test
    void testDeleteANonExistingPlayer() {
        assertFalse(repository.deletePlayer(0));
    }

    @Test
    void testGetAllPlayersFromEmptyRepository() {
        Player[] players = repository.getAllPlayers();
        assertEquals(0, players.length);
        assertNull(repository.getPlayerById(0));
    }

    @Test
    void testGetPlayerByIdFromEmptyRepository() {
        assertNull(repository.getPlayerById(0));
    }

}
