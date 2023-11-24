package lab5.application;

import lab5.domain.Game;
import lab5.infrastructure.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameRepositoryTest {

    private Repository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryRepository();
    }

    @Test
    void testGameConstructorWithNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Game(0, null));
    }

    @Test
    void testGameConstructorWithValidTitle() {
        String title = "Chess";
        Game game = new Game(0, title);
        assertEquals(title, game.getGameTitle());
        assertEquals(0, game.getId());
    }

    @Test
    void testGameEqualsHashCodeAndToString() {
        Game game1 = new Game(0, "Chess");
        Game game2 = new Game(0, "Chess");
        Game game3 = new Game(1, "Chess");
        Game game4 = new Game(0, "Checkers");
        assertEquals(game1, game2);
        assertNotEquals(game1, game3);
        assertNotEquals(game1, game4);
        assertNotEquals(game1, null);
        assertNotEquals(null, game1);
        assertNotEquals(game1, new Object());
        assertEquals(game1.hashCode(), game1.hashCode());
        String str = "Game{" +
            "id='" + game1.getId() + '\'' +
            ", gameTitle='" + game1.getGameTitle() + '\'' +
            '}';
        assertEquals(str, game1.toString());
    }

    @Test
    void testCreateGameWithValidTitle() {
        String title = "Chess";
        Game createdGame = repository.createGame(title);
        assertNotNull(createdGame);
        assertEquals(title, createdGame.getGameTitle());
        Game game = repository.getGameById(createdGame.getId());
        assertEquals(createdGame, game);
    }


    @Test
    void testCreateMultipleGames() {
        Game chessGame = repository.createGame("Chess");
        Game checkersGame = repository.createGame("Checkers");
        Game monopolyGame = repository.createGame("Monopoly");
        assertNotEquals(chessGame, checkersGame);
        assertNotEquals(chessGame, monopolyGame);
        assertNotEquals(checkersGame, monopolyGame);
        Game[] games = repository.getAllGames();
        Set<Game> gameSet = new HashSet<>(Arrays.asList(games));
        assertEquals(3, games.length);
        assertTrue(gameSet.contains(chessGame));
        assertTrue(gameSet.contains(checkersGame));
        assertTrue(gameSet.contains(monopolyGame));
    }

    @Test
    void testUpdateAnExistingGame() {
        Game chessGame = repository.createGame("Chess");
        Game checkersGame = repository.createGame("Checkers");
        Game monopolyGame = repository.createGame("Monopoly");
        Game updatedCheckersGame = repository.updateGame(checkersGame.getId(), "Checkers Deluxe");
        assertEquals(checkersGame.getId(), updatedCheckersGame.getId());
        assertEquals("Checkers Deluxe", updatedCheckersGame.getGameTitle());
        Game[] games = repository.getAllGames();
        Set<Game> gameSet = new HashSet<>(Arrays.asList(games));
        assertEquals(3, games.length);
        assertTrue(gameSet.contains(chessGame));
        assertTrue(gameSet.contains(updatedCheckersGame));
        assertTrue(gameSet.contains(monopolyGame));
        assertFalse(gameSet.contains(checkersGame));
    }

    @Test
    void testUpdateANonExistingGame() {
        Game updatedGame = repository.updateGame(0, "Chess");
        assertNull(updatedGame);
    }

    @Test
    void testDeleteAnExistingGame() {
        Game chessGame = repository.createGame("Chess");
        Game checkersGame = repository.createGame("Checkers");
        Game monopolyGame = repository.createGame("Monopoly");
        assertTrue(repository.deleteGame(checkersGame.getId()));
        Game[] games = repository.getAllGames();
        Set<Game> gameSet = new HashSet<>(Arrays.asList(games));
        assertEquals(2, games.length);
        assertTrue(gameSet.contains(chessGame));
        assertTrue(gameSet.contains(monopolyGame));
        assertFalse(gameSet.contains(checkersGame));
    }

    @Test
    void testDeleteANonExistingGame() {
        assertFalse(repository.deleteGame(0));
    }

    @Test
    void testGetAllGamesFromEmptyRepository() {
        Game[] games = repository.getAllGames();
        assertEquals(0, games.length);
        assertNull(repository.getGameById(0));
    }

    @Test
    void testGetGameByIdFromEmptyRepository() {
        assertNull(repository.getGameById(0));
    }

}
