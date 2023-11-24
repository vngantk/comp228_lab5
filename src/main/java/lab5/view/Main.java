package lab5.view;

import lab5.application.Repository;
import lab5.domain.Game;
import lab5.domain.Player;
import lab5.infrastructure.repository.JDBCRepository;
import org.h2.tools.Server;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    private static void startServer() throws Exception {
        Server.createTcpServer("-tcpPort", "9092", "-ifNotExists", "-trace").start();
        Server.createWebServer("-webPort", "8082", "-ifNotExists", "-trace").start();
    }

    private static void initLookAndFeel() {
        com.formdev.flatlaf.FlatLightLaf.setup();
    }

    private static Repository initRepository() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/lab5", "sa", "");
        return new JDBCRepository(conn, (repo -> {
            repo.deleteAllPlayers();
            repo.deleteAllGames();
            repo.deleteAllPlayerAndGames();
            repo.restartPlayerSequence();
            repo.restartGameSequence();
            repo.restartPlayerAndGameSequence();
            Player.generatePlayers(repo);
            Game.generateGames(repo);
        }));
    }

    public static void main(String[] args) throws Exception {
        startServer();

        initLookAndFeel();
        JFrame frame = new JFrame("Players");
        PlayerTableView playerTableView = new PlayerTableView();
        frame.setContentPane(playerTableView.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Repository repository = initRepository();
        playerTableView.setRepository(repository);
    }
}
