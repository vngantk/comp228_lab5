package lab5.domain;

import lab5.application.Repository;

import java.util.Objects;

/**
 * A game.
 */
public class Game {
    private final int id;
    private final String gameTitle;

    public Game(int id, String gameTitle) {
        if (gameTitle == null) {
            throw new IllegalArgumentException("Game constructor arguments cannot be null");
        }
        this.id = id;
        this.gameTitle = gameTitle;
    }

    public int getId() {
        return id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", gameTitle='" + gameTitle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && gameTitle.equals(game.gameTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameTitle);
    }

    public static Game[] generateGames(Repository repository) {
        return new Game[] {
            repository.createGame("Monopoly"),
            repository.createGame("Scrabble"),
            repository.createGame("Chess"),
            repository.createGame("Checkers"),
            repository.createGame("Clue (or Cluedo)"),
            repository.createGame("Battleship"),
            repository.createGame("The Game of Life"),
            repository.createGame("Sorry!"),
            repository.createGame("Risk"),
            repository.createGame("Candy Land"),
        };
    }
}
