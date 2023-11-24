package lab5.application;

import lab5.domain.Game;
import lab5.domain.Player;
import lab5.domain.PlayerAndGame;

import java.util.Date;

public interface Repository {

    /**
     * Create a new game with the given title.
     * @param title the title of the game; must not be null
     * @return the new game
     */
    public Game createGame(String title);

    /**
     * Update the game with the given id with the given title.
     * @param id the id of the game to update
     * @param title the new title of the game; must not be null
     * @return the updated game; null if the game was not found
     */
    public Game updateGame(int id, String title);

    /**
     * Delete the game with the given id.
     * @param id the id of the game to delete
     * @return true if the game was deleted, false otherwise
     */
    public boolean deleteGame(int id);

    /**
     * Get the game with the given id.
     * @param id the id of the game to get
     * @return the game with the given id, or null if no such game exists
     */
    public Game getGameById(int id);

    /**
     * Get all games.
     * @return all games
     */
    public Game[] getAllGames();

    /**
     * Create a new player with the given information.
     * @param firstName the first name of the player; must not be null
     * @param lastName the last name of the player; must not be null
     * @param address the address of the player; must not be null
     * @param postalCode the postal code of the player; must not be null
     * @param province the province of the player; must not be null
     * @param phoneNumber the phone number of the player; must not be null
     * @return the new player
     */
    public Player createPlayer(String firstName, String lastName, String address, String postalCode, String province, String phoneNumber);

    /**
     * Update the player with the given id with the given information.
     * @param id the id of the player to update
     * @param firstName the new first name of the player; if null, the first name is not updated
     * @param lastName the new last name of the player; if null, the last name is not updated
     * @param address the new address of the player; if null, the address is not updated
     * @param postalCode the new postal code of the player; if null, the postal code is not updated
     * @param province the new province of the player; if null, the province is not updated
     * @param phoneNumber the new phone number of the player; if null, the phone number is not updated
     * @return the updated player; null if the player was not found
     */
    public Player updatePlayer(int id, String firstName, String lastName, String address, String postalCode, String province, String phoneNumber);

    /**
     * Delete the player with the given id.
     * @param id the id of the player to delete
     * @return true if the player was deleted, false otherwise
     */
    public boolean deletePlayer(int id);

    /**
     * Get the player with the given id.
     * @param id the id of the player to get
     * @return the player with the given id, or null if no such player exists
     */
    public Player getPlayerById(int id);

    /**
     * Get all players.
     * @return all players
     */
    public Player[] getAllPlayers();

    /**
     * Create a new player and game with the given information.
     * @param gameId the id of the game
     * @param playerId the id of the player
     * @param playingDate the date the game was played; must not be null
     * @param score the score of the game
     * @return the new player and game
     */
    public PlayerAndGame createPlayerAndGame(int gameId, int playerId, Date playingDate, int score);

    /**
     * Update the player and game with the given id with the given information.
     * @param id the id of the player and game to update
     * @param playingDate the new date the game was played; if null, the playing date is not updated
     * @param score the new score of the game; if null, the score is not updated
     * @return the updated player and game; null if the player and game was not found
     */
    public PlayerAndGame updatePlayerAndGame(int id, Date playingDate, Integer score);

    /**
     * Delete the player and game with the given id.
     * @param id the id of the player and game to delete
     * @return true if the player and game was deleted, false otherwise
     */
    public boolean deletePlayerAndGame(int id);

    /**
     * Get the player and game with the given id.
     * @param id the id of the player and game to get
     * @return the player and game with the given id, or null if no such player and game exists
     */
    public PlayerAndGame getPlayerAndGameById(int id);

    /**
     * Get all player and games.
     * @return all player and games
     */
    public PlayerAndGame[] getAllPlayerAndGames();

}
