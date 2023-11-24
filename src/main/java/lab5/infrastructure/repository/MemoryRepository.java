package lab5.infrastructure.repository;

import lab5.application.Repository;
import lab5.domain.Game;
import lab5.domain.Player;
import lab5.domain.PlayerAndGame;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MemoryRepository implements Repository {

    private final Map<Integer, Game> gameMap = new HashMap<>();
    private final Map<Integer, Player> playerMap = new HashMap<>();
    private final Map<Integer, PlayerAndGame> playerAndGameMap = new HashMap<>();

    private int nextGameId = 0;
    private int nextPlayerId = 0;
    private int nextPlayerAndGameId = 0;

    @Override
    public Game createGame(String title) {
        Game game = new Game(nextGameId++, title);
        gameMap.put(game.getId(), game);
        return game;
    }

    @Override
    public Game updateGame(int id, String title) {
        Game game = gameMap.get(id);
        if (game != null) {
            game = new Game(id,
                title != null ? title : game.getGameTitle()
            );
            gameMap.put(id, game);
        }
        return game;
    }

    @Override
    public boolean deleteGame(int id) {
        return gameMap.remove(id) != null;
    }

    @Override
    public Game getGameById(int id) {
        return gameMap.get(id);
    }

    @Override
    public Game[] getAllGames() {
        return gameMap.values().toArray(new Game[gameMap.size()]);
    }

    @Override
    public Player createPlayer(String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        Player player = new Player(nextPlayerId++, firstName, lastName, address, postalCode, province, phoneNumber);
        playerMap.put(player.getId(), player);
        return player;
    }

    @Override
    public Player updatePlayer(int id, String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        Player player = playerMap.get(id);
        if (player != null) {
            player = new Player(id,
                firstName != null ? firstName : player.getFirstName(),
                lastName != null ? lastName : player.getLastName(),
                address != null ? address : player.getAddress(),
                postalCode != null ? postalCode : player.getPostalCode(),
                province != null ? province : player.getProvince(),
                phoneNumber != null ? phoneNumber : player.getPhoneNumber()
            );
            playerMap.put(id, player);
        }
        return player;
    }

    @Override
    public boolean deletePlayer(int id) {
        return playerMap.remove(id) != null;
    }

    @Override
    public Player getPlayerById(int id) {
        return playerMap.get(id);
    }

    @Override
    public Player[] getAllPlayers() {
        return playerMap.values().toArray(new Player[playerMap.size()]);
    }

    @Override
    public PlayerAndGame createPlayerAndGame(int gameId, int playerId, Date playingDate, int score) {
        PlayerAndGame playerAndGame = new PlayerAndGame(nextPlayerAndGameId++, gameId, playerId, playingDate, score);
        playerAndGameMap.put(playerAndGame.getId(), playerAndGame);
        return playerAndGame;
    }

    @Override
    public PlayerAndGame updatePlayerAndGame(int id, Date playingDate, Integer score) {
        PlayerAndGame playerAndGame = playerAndGameMap.get(id);
        if (playerAndGame != null) {
            playerAndGame = new PlayerAndGame(id,
                playerAndGame.getGameId(),
                playerAndGame.getPlayerId(),
                playingDate != null ? playingDate : playerAndGame.getPlayingDate(),
                score != null ? score : playerAndGame.getScore()
            );
            playerAndGameMap.put(id, playerAndGame);
        }
        return playerAndGame;
    }

    @Override
    public boolean deletePlayerAndGame(int id) {
        return playerAndGameMap.remove(id) != null;
    }

    @Override
    public PlayerAndGame getPlayerAndGameById(int id) {
        return playerAndGameMap.get(id);
    }

    @Override
    public PlayerAndGame[] getAllPlayerAndGames() {
        return playerAndGameMap.values().toArray(new PlayerAndGame[playerAndGameMap.size()]);
    }

    @Override
    public void deleteAllGames() {
        gameMap.clear();
    }

    @Override
    public void deleteAllPlayers() {
        playerMap.clear();
    }

    @Override
    public void deleteAllPlayerAndGames() {
        playerAndGameMap.clear();
    }

}
