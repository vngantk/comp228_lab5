package lab5.application;

import lab5.domain.PlayerAndGame;
import lab5.infrastructure.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerAndGameRepositoryTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void testConstructPlayerAndGame() {
        PlayerAndGame playerAndGame = new PlayerAndGame(0, 0, 0, new Date(0L), 0);
        assertEquals(0, playerAndGame.getId());
        assertEquals(0, playerAndGame.getGameId());
        assertEquals(0, playerAndGame.getPlayerId());
        assertEquals(new Date(0L), playerAndGame.getPlayingDate());
        assertEquals(0, playerAndGame.getScore());
        assertThrows(IllegalArgumentException.class, () ->
            new PlayerAndGame(0, 0, 0, null, 0)
        );

    }


    @Test
    void testCreatePlayerAndGameWithValidTitle() {
        PlayerAndGame createdPlayerAndGame = repository.createPlayerAndGame(0, 0, new Date(0L), 0    );
        assertNotNull(createdPlayerAndGame);

        PlayerAndGame playerAndGame = repository.getPlayerAndGameById(createdPlayerAndGame.getId());
        assertEquals(createdPlayerAndGame, playerAndGame);
    }


    @Test
    void testCreateMultiplePlayerAndGames() {
        PlayerAndGame playerAndGame1 = repository.createPlayerAndGame(0, 0, new Date(0L), 0    );
        PlayerAndGame playerAndGame2 = repository.createPlayerAndGame(0, 1, new Date(1L), 100    );
        PlayerAndGame playerAndGame3 = repository.createPlayerAndGame(1, 2, new Date(2L), 200    );
        assertNotEquals(playerAndGame1, playerAndGame2);
        assertNotEquals(playerAndGame1, playerAndGame3);
        assertNotEquals(playerAndGame2, playerAndGame3);
        assertNotEquals(playerAndGame1.getId(), playerAndGame2.getId());
        assertNotEquals(playerAndGame1.getId(), playerAndGame3.getId());
        assertNotEquals(playerAndGame2.getId(), playerAndGame3.getId());
        PlayerAndGame[] playerAndGames = repository.getAllPlayerAndGames();
        Set<PlayerAndGame> playerAndGameSet = new HashSet<>(Arrays.asList(playerAndGames));
        assertEquals(3, playerAndGames.length);
        assertTrue(playerAndGameSet.contains(playerAndGame1));
        assertTrue(playerAndGameSet.contains(playerAndGame2));
        assertTrue(playerAndGameSet.contains(playerAndGame3));
    }

    @Test
    void testUpdateAnExistingPlayerAndGame() {
        PlayerAndGame playerAndGame1 = repository.createPlayerAndGame(0, 0, new Date(0L), 0    );
        PlayerAndGame playerAndGame2 = repository.createPlayerAndGame(0, 1, new Date(1L), 100    );
        PlayerAndGame playerAndGame3 = repository.createPlayerAndGame(1, 2, new Date(2L), 200    );
        PlayerAndGame updatedPlayerAndGame = repository.updatePlayerAndGame(playerAndGame2.getId(), new Date(200), 1000);
        assertEquals(playerAndGame2.getId(), updatedPlayerAndGame.getId());
        assertEquals(playerAndGame2.getGameId(), updatedPlayerAndGame.getGameId());
        assertEquals(playerAndGame2.getPlayerId(), updatedPlayerAndGame.getPlayerId());
        assertEquals(new Date(200), updatedPlayerAndGame.getPlayingDate());
        assertEquals(1000, updatedPlayerAndGame.getScore());
        PlayerAndGame[] playerAndGames = repository.getAllPlayerAndGames();
        Set<PlayerAndGame> playerAndGameSet = new HashSet<>(Arrays.asList(playerAndGames));
        assertEquals(3, playerAndGames.length);
        assertTrue(playerAndGameSet.contains(playerAndGame1));
        assertTrue(playerAndGameSet.contains(updatedPlayerAndGame));
        assertTrue(playerAndGameSet.contains(playerAndGame3));
        assertFalse(playerAndGameSet.contains(playerAndGame2));

    }

    @Test
    void testPlayerAndGameEqualsHashCodeAndToString() {
        PlayerAndGame playerAndGame1 = new PlayerAndGame(0, 0, 0, new Date(0L), 0    );
        PlayerAndGame playerAndGame2 = new PlayerAndGame(0, 0, 0, new Date(0L), 0    );
        PlayerAndGame playerAndGame3 = new PlayerAndGame(1, 2, 0, new Date(2L), 200    );
        PlayerAndGame playerAndGame4 = new PlayerAndGame(1, 2, 0, new Date(2L), 200    );

        assertEquals(playerAndGame1, playerAndGame2);
        assertNotEquals(playerAndGame1, playerAndGame3);
        assertNotEquals(playerAndGame1, playerAndGame4);
        assertNotEquals(playerAndGame1, null);
        assertNotEquals(null, playerAndGame1);
        assertNotEquals(playerAndGame1, new Object());
        assertEquals(playerAndGame1.hashCode(), playerAndGame1.hashCode());
        String str = "PlayerAndGame{" +
                "id='" + playerAndGame1.getId() + '\'' +
                ", gameId='" + playerAndGame1.getGameId() + '\'' +
                ", playerId='" + playerAndGame1.getPlayerId() + '\'' +
                ", playingDate='" + playerAndGame1.getPlayingDate() + '\'' +
                ", score='" + playerAndGame1.getScore() + '\'' +
                '}';
        assertEquals(str, playerAndGame1.toString());
    }

    @Test
    void testUpdateANonExistingPlayerAndGame() {
        PlayerAndGame updatedPlayerAndGame = repository.updatePlayerAndGame(0, new Date(0L), 0    );
        assertNull(updatedPlayerAndGame);
    }

    @Test
    void testDeleteAnExistingPlayerAndGame() {
        PlayerAndGame playerAndGame1 = repository.createPlayerAndGame(0, 0, new Date(0L), 0    );
        PlayerAndGame playerAndGame2 = repository.createPlayerAndGame(0, 1, new Date(1L), 100    );
        PlayerAndGame playerAndGame3 = repository.createPlayerAndGame(1, 2, new Date(2L), 200    );

        assertTrue(repository.deletePlayerAndGame(playerAndGame2.getId()));
        PlayerAndGame[] playerAndGames = repository.getAllPlayerAndGames();
        Set<PlayerAndGame> playerAndGameSet = new HashSet<>(Arrays.asList(playerAndGames));
        assertEquals(2, playerAndGames.length);
        assertTrue(playerAndGameSet.contains(playerAndGame1));
        assertTrue(playerAndGameSet.contains(playerAndGame3));
        assertFalse(playerAndGameSet.contains(playerAndGame2));
    }

    @Test
    void testDeleteANonExistingPlayerAndGame() {
        assertFalse(repository.deletePlayerAndGame(0));
    }

    @Test
    void testGetAllPlayerAndGamesFromEmptyRepository() {
        PlayerAndGame[] playerAndGames = repository.getAllPlayerAndGames();
        assertEquals(0, playerAndGames.length);
        assertNull(repository.getPlayerAndGameById(0));
    }

    @Test
    void testGetPlayerAndGameByIdFromEmptyRepository() {
        assertNull(repository.getPlayerAndGameById(0));
    }

}
