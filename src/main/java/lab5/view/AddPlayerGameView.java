package lab5.view;

import lab5.application.Repository;
import lab5.domain.Game;
import lab5.domain.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddPlayerGameView {
    private JTextField dateField;
    private JTextField scoreField;
    private JComboBox<String> gameComboBox;
    private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton okButton;
    private JPanel mainPanel;
    private JPanel fieldPanel;

    private Repository repository;
    private LinkedHashMap<String, Game> gameByTitle = new LinkedHashMap<>();

    private Player player;

    private final JDialog dialog;

    public AddPlayerGameView(Window owner) {
        dialog = new JDialog(owner, "Add Player Game", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setModal(true);
        dialog.setContentPane(mainPanel);
        gameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        scoreField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        dateField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cancelButton.addActionListener(this::onCancelButtonClicked);
        okButton.addActionListener(this::onOkButtonClicked);

    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        gameByTitle.clear();
        gameComboBox.removeAllItems();
        for (Game game : repository.getAllGames()) {
            gameByTitle.put(game.getGameTitle(), game);
            gameComboBox.addItem(game.getGameTitle());
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void showDialog() {
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setVisible(true);
    }


    private void onCancelButtonClicked(ActionEvent e) {
        dialog.setVisible(false);
    }

    private void onOkButtonClicked(ActionEvent e) {
        String gameTitle = (String) gameComboBox.getSelectedItem();
        Game game = gameByTitle.get(gameTitle);
        if (game == null) {
            JOptionPane.showMessageDialog(dialog, "Game not found");
            return;
        }
        int score;
        try {
            score = Integer.parseInt(scoreField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Score must be a number");
            return;
        }
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Date must be in format yyyy-MM-dd");
            return;
        }
        repository.createPlayerAndGame(game.getId(), player.getId(), date, score);
        dialog.setVisible(false);
    }
}
