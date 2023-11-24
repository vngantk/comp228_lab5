package lab5.view;

import lab5.application.Repository;
import lab5.domain.Player;
import lab5.infrastructure.repository.MemoryRepository;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        PlayerTableView playerTableView = new PlayerTableView();
        JFrame frame = new JFrame("Players");
        frame.setContentPane(playerTableView.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        Repository repository = new MemoryRepository();
        Player.generatePlayers(repository);
        playerTableView.setRepository(repository);
    }
}
