package lab5.view;

import lab5.application.Repository;
import lab5.domain.Player;
import lab5.domain.PlayerAndGame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerView {
    private JPanel mainPanel;
    private JPanel fieldsPanel;
    private JPanel buttonsPanel;
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField postalCodeField;
    private JTextField provinceField;
    private JTextField phoneNumberField;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel idLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel addressLabel;
    private JLabel postalCodeLabel;
    private JLabel provinceLabel;
    private JLabel phoneNumberLabel;
    private JScrollPane tableScrollPanel;
    private JPanel tablePanel;
    private JButton deleteButton;
    private JButton deleteGamePlayedButton;
    private JButton addGamePlayedButton;
    private JPanel leftButtonPanel;
    private JPanel rightButtonPanel;
    private JTable gamePlayedTable;

    private Player player;
    private final LinkedHashMap<Integer, PlayerAndGame> playerAndGames = new LinkedHashMap<>();
    private final Map<Integer, PlayerAndGame> deletedPlayerAndGames = new HashMap<>();

    private final JDialog dialog;
    private Repository repository;
    private Mode mode;

    private final AddPlayerGameView addPlayerGameView;

    private final DefaultTableModel gamePlayedTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            int columnCount = getColumnCount();
            return (column == columnCount - 1 || column == columnCount - 2);
        }
    };

    public PlayerView(Window owner) {
        addPlayerGameView = new AddPlayerGameView(owner);
        okButton.addActionListener(this::onOkButtonClicked);
        cancelButton.addActionListener(this::onCancelButtonClicked);
        deleteButton.addActionListener(this::onDeleteButtonClicked);
        idField.setFocusable(false);
        idField.setEditable(false);
        deleteGamePlayedButton.addActionListener(this::onDeleteGamePlayedButtonClicked);
        addGamePlayedButton.addActionListener(this::onAddGamePlayedButtonClicked);
        dialog = new JDialog(owner);
        createUIComponents();
    }

    private void createUIComponents() {
        gamePlayedTable = new JTable(gamePlayedTableModel);
        gamePlayedTableModel.addColumn("ID");
        gamePlayedTableModel.addColumn("Game Title");
        gamePlayedTableModel.addColumn("Date Played");
        gamePlayedTableModel.addColumn("Score");
        tableScrollPanel.setViewportView(gamePlayedTable);
        gamePlayedTable.setShowGrid(true);
        gamePlayedTable.setPreferredScrollableViewportSize(new Dimension(300, 100));
        gamePlayedTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = gamePlayedTable.getSelectedRow();
            deleteGamePlayedButton.setEnabled(selectedRow != -1 && selectedRow < playerAndGames.size());
        });
        dialog.setContentPane(mainPanel);
        dialog.setModal(true);

    }

    private void onOkButtonClicked(ActionEvent e) {
        if (mode == Mode.CREATING) {
            player = repository.createPlayer(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    provinceField.getText(),
                    phoneNumberField.getText()
            );
            System.out.println("Created player: " + player);
        } else if (mode == Mode.EDITING) {
            player = repository.updatePlayer(
                    player.getId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    provinceField.getText(),
                    phoneNumberField.getText()
            );
            int rowCount = gamePlayedTableModel.getRowCount();
            int columnCount = gamePlayedTableModel.getColumnCount();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                Object id = gamePlayedTableModel.getValueAt(rowIndex, 0);
                PlayerAndGame playerAndGame = playerAndGames.get(id);
                Object playingDate = gamePlayedTableModel.getValueAt(rowIndex, columnCount - 2);
                Object score = gamePlayedTableModel.getValueAt(rowIndex, columnCount - 1);
                Date modifiedPlayingDate = null;
                if (!Objects.equals(playingDate, playerAndGame.getPlayingDate())) {
                    System.out.println("Not equal: " + playingDate + " " + playerAndGame.getPlayingDate());
                    try {
                        modifiedPlayingDate = new SimpleDateFormat("yyyy-MM-dd").parse((String) playingDate);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                Integer modifiedScore = null;
                if (!Objects.equals(score, playerAndGame.getScore())) {
                    try {
                        modifiedScore = Integer.parseInt((String) score);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if (modifiedPlayingDate != null || modifiedScore != null) {
                    PlayerAndGame newPlayerAndGame = repository.updatePlayerAndGame(
                        playerAndGame.getId(),
                        modifiedPlayingDate,
                        modifiedScore
                    );
                    System.out.println("Updated PlayerAndGame from: " + playerAndGame + " to: " + newPlayerAndGame);
                    playerAndGames.put(newPlayerAndGame.getId(), newPlayerAndGame);
                }
            }
            System.out.println("Deleted PlayerAndGames: " + deletedPlayerAndGames.values());
            if (deletedPlayerAndGames.size() > 0) {
                for (PlayerAndGame playerAndGame : deletedPlayerAndGames.values()) {
                    repository.deletePlayerAndGame(playerAndGame.getId());
                    System.out.println("Deleted PlayerAndGame: " + playerAndGame);
                }
                deletedPlayerAndGames.clear();
            }
            System.out.println("Updated player: " + player);
        }
        if (dialog != null) {
            dialog.setVisible(false);
        }
    }

    private void onCancelButtonClicked(ActionEvent e) {
        if (dialog != null) {
            dialog.setVisible(false);
        }
    }

    private void onDeleteButtonClicked(ActionEvent e) {
        int answer = JOptionPane.showConfirmDialog(this.dialog,
            "Are you sure you want to delete this player?", "Delete Player", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            repository.deletePlayerAndGamesByPlayerId(player.getId());
            repository.deletePlayer(player.getId());
            if (dialog != null) {
                dialog.setVisible(false);
            }
        }
    }

    private void onDeleteGamePlayedButtonClicked(ActionEvent e) {
        int selectedRow = gamePlayedTable.getSelectedRow();
        if (selectedRow != -1 && selectedRow < playerAndGames.size()) {
            Integer id = (Integer) gamePlayedTableModel.getValueAt(selectedRow, 0);
            PlayerAndGame playerAndGame = playerAndGames.get(id);
            int answer = JOptionPane.showConfirmDialog(this.dialog,
                "Are you sure you want to delete this game?", "Delete Game", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                deletedPlayerAndGames.put(id, playerAndGame);
                gamePlayedTableModel.removeRow(selectedRow);
                playerAndGames.remove(id);
            }
        }
    }

    private void onAddGamePlayedButtonClicked(ActionEvent e) {
        addPlayerGameView.setPlayer(player);
        addPlayerGameView.showDialog();
        populateGamesPlayed();

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void clearFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        postalCodeField.setText("");
        provinceField.setText("");
        phoneNumberField.setText("");
    }

    private void setFields(Player player) {
        idField.setText(Integer.toString(player.getId()));
        firstNameField.setText(player.getFirstName());
        lastNameField.setText(player.getLastName());
        addressField.setText(player.getAddress());
        postalCodeField.setText(player.getPostalCode());
        provinceField.setText(player.getProvince());
        phoneNumberField.setText(player.getPhoneNumber());
    }

    private void setFieldsEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        addressField.setEditable(editable);
        postalCodeField.setEditable(editable);
        provinceField.setEditable(editable);
        phoneNumberField.setEditable(editable);
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (player == null) {
            clearFields();
        } else {
            setFields(player);
        };
    }

    public Player getPlayer() {
        return player;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        addPlayerGameView.setRepository(repository);
    }

    public Repository getRepository() {
        return repository;
    }

    public void show(boolean create) {
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        if (create) {
            setCreatingMode();
        } else {
            setViewingMode();
            populateGamesPlayed();
        }
        dialog.setVisible(true);
    }

    private void setViewingMode() {
        mode = Mode.EDITING;
        dialog.setTitle("Player");
        deleteGamePlayedButton.setEnabled(false);
        deleteButton.setVisible(true);
        okButton.setVisible(true);
        cancelButton.setVisible(true);
        idField.setVisible(true);
        idLabel.setVisible(true);
        setFieldsEditable(true);
        populateGamesPlayed();
    }

    private void setCreatingMode() {
        mode = Mode.CREATING;
        dialog.setTitle("Create New Player");
        deleteGamePlayedButton.setEnabled(false);
        deleteButton.setVisible(false);
        okButton.setVisible(true);
        idField.setVisible(false);
        idLabel.setVisible(false);
        clearFields();
        setFieldsEditable(true);
        gamePlayedTableModel.setRowCount(0);
    }

    private void populateGamesPlayed() {
        gamePlayedTableModel.setRowCount(0);
        if (player != null) {
            playerAndGames.clear();
            for (PlayerAndGame playerAndGame : repository.getPlayerAndGamesByPlayerId(player.getId())) {
                if (playerAndGame.getPlayerId() == player.getId()) {
                    playerAndGames.put(playerAndGame.getId(), playerAndGame);
                    gamePlayedTableModel.addRow(new Object[]{
                            playerAndGame.getId(),
                            repository.getGameById(playerAndGame.getGameId()).getGameTitle(),
                            playerAndGame.getPlayingDate(),
                            playerAndGame.getScore()
                    });
                } else {
                    System.out.println("PlayerAndGame " + playerAndGame + " does not belong to player " + player);
                }
            }
        } else {
            playerAndGames.clear();
        }
    }

    private enum Mode {
        VIEWING,
        EDITING,
        CREATING,
    }
}
