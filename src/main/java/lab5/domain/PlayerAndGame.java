package lab5.domain;

import java.util.Date;
import java.util.Objects;

/**
 * A player and game.
 */
public class PlayerAndGame {
    private final int id;
    private final int gameId;
    private final int playerId;
    private final Date playingDate;
    private final int score;

    public PlayerAndGame(int id, int gameId, int playerId, Date playingDate, int score) {
        if (playingDate == null) {
            throw new IllegalArgumentException("PlayerAndGame constructor arguments cannot be null");
        }
        this.id = id;
        this.gameId = gameId;
        this.playerId = playerId;
        this.playingDate = playingDate;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Date getPlayingDate() {
        return playingDate;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAndGame that = (PlayerAndGame) o;
        return id == that.id && gameId == that.gameId && playerId == that.playerId && score == that.score && Objects.equals(playingDate, that.playingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, playerId, playingDate, score);
    }

    @Override
    public String toString() {
        return "PlayerAndGame{" +
                "id='" + id + '\'' +
                ", gameId='" + gameId + '\'' +
                ", playerId='" + playerId + '\'' +
                ", playingDate='" + playingDate + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
