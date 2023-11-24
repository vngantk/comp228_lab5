package lab5.infrastructure.repository;

import lab5.application.Repository;
import lab5.domain.Game;
import lab5.domain.Player;
import lab5.domain.PlayerAndGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class JDBCRepository implements Repository {

    private static final String PLAYER_SEQ = "player_seq";
    private static final String GAME_SEQ = "game_seq";
    private static final String PLAYER_AND_GAME_SEQ = "player_and_game_seq";

    private static final String CREATE_PLAYER_TABLE =
        "CREATE TABLE IF NOT EXISTS player (" +
            "player_id INTEGER PRIMARY KEY," +
            "first_name VARCHAR NOT NULL," +
            "last_name VARCHAR NOT NULL," +
            "address VARCHAR NOT NULL," +
            "postal_code VARCHAR NOT NULL," +
            "province VARCHAR NOT NULL," +
            "phone_number VARCHAR NOT NULL" +
        ")";

    private static final String CREATE_GAME_TABLE =
        "CREATE TABLE IF NOT EXISTS game (" +
            "game_id INTEGER PRIMARY KEY," +
            "game_title VARCHAR NOT NULL" +
        ")";

    private static final String CREATE_PLAYER_AND_GAME_TABLE =
        "CREATE TABLE IF NOT EXISTS player_and_game (" +
            "player_game_id INTEGER PRIMARY KEY," +
            "game_id INTEGER NOT NULL," +
            "player_id INTEGER NOT NULL," +
            "playing_date DATE NOT NULL," +
            "score INTEGER NOT NULL," +
            "PRIMARY KEY (player_game_id)," +
            "FOREIGN KEY (player_id) REFERENCES player(player_id)," +
            "FOREIGN KEY (game_id) REFERENCES game(game_id)" +
        ")";

    private static final String CREATE_PLAYER_SEQ =
        "CREATE SEQUENCE IF NOT EXISTS player_seq AS INTEGER START WITH 0";

    private static final String CREATE_GAME_SEQ =
        "CREATE SEQUENCE IF NOT EXISTS game_seq AS INTEGER START WITH 0";

    private static final String CREATE_PLAYER_AND_GAME_SEQ =
        "CREATE SEQUENCE IF NOT EXISTS player_and_game_seq AS INTEGER START WITH 0";

    private static final String RESTART_PLAYER_SEQ =
        "ALTER SEQUENCE player_seq RESTART WITH 0";

    private static final String RESTART_GAME_SEQ =
        "ALTER SEQUENCE game_seq RESTART WITH 0";

    private static final String RESTART_PLAYER_AND_GAME_SEQ =
        "ALTER SEQUENCE player_and_game_seq RESTART WITH 0";


    private final Connection connection;

    private PreparedStatement selectPlayerSeqStatement;
    private PreparedStatement selectGameSeqStatement;
    private PreparedStatement selectPlayerAndGameSeqStatement;

    public interface Initializer {
        void initialize(JDBCRepository repository) throws SQLException;
    }

    public JDBCRepository(Connection connection, Initializer initializer) throws SQLException {
        this.connection = connection;
        initTables();
        initSequences();
        initPreparedStatements();
        if (initializer != null) {
            initializer.initialize(this);
        }
    }

    public JDBCRepository(Connection connection) throws SQLException {
        this(connection, null);
    }

    public void initTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PLAYER_TABLE);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_GAME_TABLE);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PLAYER_AND_GAME_TABLE);
        }
    }

    public void initSequences() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PLAYER_SEQ);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_GAME_SEQ);
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PLAYER_AND_GAME_SEQ);
        }
    }

    public void restartPlayerSequence() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(RESTART_PLAYER_SEQ);
        }
    }

    public void restartGameSequence() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(RESTART_GAME_SEQ);
        }
    }

    public void restartPlayerAndGameSequence() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(RESTART_PLAYER_AND_GAME_SEQ);
        }
    }

    private void initPreparedStatements() throws SQLException {
        selectPlayerSeqStatement = connection.prepareStatement("SELECT nextval('" + PLAYER_SEQ + "')");
        selectGameSeqStatement = connection.prepareStatement("SELECT nextval('" + GAME_SEQ + "')");
        selectPlayerAndGameSeqStatement = connection.prepareStatement("SELECT nextval('" + PLAYER_AND_GAME_SEQ + "')");
    }

    public int getNextPlayerId() throws SQLException {
        try (ResultSet resultSet = selectPlayerSeqStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public int getNextGameId() throws SQLException {
        try (ResultSet resultSet = selectGameSeqStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public int getNextPlayerAndGameId() throws SQLException {
        try (ResultSet resultSet = selectPlayerAndGameSeqStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    @Override
    public Player createPlayer(String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player (player_id, first_name, last_name, address, postal_code, province, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)")
        ) {
            int id = getNextPlayerId();
            statement.setInt(1, id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, address);
            statement.setString(5, postalCode);
            statement.setString(6, province);
            statement.setString(7, phoneNumber);
            statement.executeUpdate();
            return new Player(id, firstName, lastName, address, postalCode, province, phoneNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player updatePlayer(int id, String firstName, String lastName, String address, String postalCode, String province, String phoneNumber) {
        String sbuf = "UPDATE player SET " +
            (firstName != null ? "first_name = ?, " : "first_name = first_name, ") +
            (lastName != null ? "last_name = ?, " : "last_name = last_name, ") +
            (address != null ? "address = ?, " : "address = address, ") +
            (postalCode != null ? "postal_code = ?, " : "postal_code = postal_code, ") +
            (province != null ? "province = ?, " : "province = province, ") +
            (phoneNumber != null ? "phone_number = ? " : "phone_number = phone_number ") +
            "WHERE player_id = ?";
        try (
            PreparedStatement statement = connection.prepareStatement(sbuf)
        ) {
            int index = 1;
            if (firstName != null) {
                statement.setString(index++, firstName);
            }
            if (lastName != null) {
                statement.setString(index++, lastName);
            }
            if (address != null) {
                statement.setString(index++, address);
            }
            if (postalCode != null) {
                statement.setString(index, postalCode);
            }
            if (province != null) {
                statement.setString(index++, province);
            }
            if (phoneNumber != null) {
                statement.setString(index++, phoneNumber);
            }
            statement.setInt(index, id);
            statement.executeUpdate();
            return getPlayerById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deletePlayer(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player WHERE player_id = ?")
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Player getPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        return new Player(
            resultSet.getInt("player_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("address"),
            resultSet.getString("postal_code"),
            resultSet.getString("province"),
            resultSet.getString("phone_number")
        );
    }

    @Override
    public Player getPlayerById(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE player_id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getPlayerFromResultSet(resultSet);
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player[] getAllPlayers() {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player")
        ) {
            List<Player> playerList = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    playerList.add(getPlayerFromResultSet(resultSet));
                }
                return playerList.toArray(new Player[playerList.size()]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game createGame(String title) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO game (game_id, game_title) VALUES (?, ?)")
        ) {
            int id = getNextGameId();
            statement.setInt(1, id);
            statement.setString(2, title);
            statement.executeUpdate();
            return new Game(id, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game updateGame(int id, String title) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE game SET game_title = ? WHERE game_id = ?")
        ) {
            statement.setString(1, title);
            statement.setInt(2, id);
            statement.executeUpdate();
            return getGameById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteGame(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM game WHERE game_id = ?")
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Game getGameFromResultSet(ResultSet resultSet) throws SQLException {
        return new Game(
            resultSet.getInt("game_id"),
            resultSet.getString("game_title")
        );
    }

    @Override
    public Game getGameById(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE game_id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getGameFromResultSet(resultSet);
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Game[] getAllGames() {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game")
        ) {
            List<Game> gameList = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    gameList.add(getGameFromResultSet(resultSet));
                }
                return gameList.toArray(new Game[gameList.size()]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerAndGame createPlayerAndGame(int gameId, int playerId, Date playingDate, int score) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player_and_game (player_game_id, game_id, player_id, playing_date, score) VALUES (?, ?, ?, ?, ?)")
        ) {
            int id = getNextPlayerAndGameId();
            statement.setInt(1, id);
            statement.setInt(2, gameId);
            statement.setInt(3, playerId);
            statement.setDate(4, new java.sql.Date(playingDate.getTime()));
            statement.setInt(5, score);
            statement.executeUpdate();
            return new PlayerAndGame(id, gameId, playerId, playingDate, score);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerAndGame updatePlayerAndGame(int id, Date playingDate, Integer score) {
        String sbuf = "UPDATE player_and_game SET " +
            (playingDate != null ? "playing_date = ?, " : "playing_date = playing_date, ") +
            (score != null ? "score = ? " : "score = score ") +
            "WHERE player_game_id = ?";
        try (
            PreparedStatement statement = connection.prepareStatement(sbuf)
        ) {
            int index = 1;
            if (playingDate != null) {
                statement.setDate(index++, new java.sql.Date(playingDate.getTime()));
            }
            if (score != null) {
                statement.setInt(index++, score);
            }
            statement.setInt(index, id);
            statement.executeUpdate();
            return getPlayerAndGameById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deletePlayerAndGame(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_and_game WHERE player_game_id = ?")
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PlayerAndGame getPlayerAndGameFromResultSet(ResultSet resultSet) throws SQLException {
        return new PlayerAndGame(
            resultSet.getInt("player_game_id"),
            resultSet.getInt("game_id"),
            resultSet.getInt("player_id"),
            resultSet.getDate("playing_date"),
            resultSet.getInt("score")
        );
    }

    @Override
    public PlayerAndGame getPlayerAndGameById(int id) {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player_and_game WHERE player_game_id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getPlayerAndGameFromResultSet(resultSet);
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerAndGame[] getAllPlayerAndGames() {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player_and_game")
        ) {
            List<PlayerAndGame> playerAndGameList = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    playerAndGameList.add(getPlayerAndGameFromResultSet(resultSet));
                }
                return playerAndGameList.toArray(new PlayerAndGame[playerAndGameList.size()]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllPlayers() {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player")
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllGames() {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM game")
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllPlayerAndGames() {
        try (
            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_and_game")
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
