package lab5.view;

import lab5.application.Repository;
import lab5.domain.Player;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerTableView {
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel centerPanel;
    private JButton refreshButton;
    private JButton newButton;
    private JButton UpdateButton;
    private JButton deleteButton;
    private JScrollPane scrollPanel;
    private JButton editButton;

    private final PlayerTableModel tableModel = new PlayerTableModel();
    private final JTable table = new JTable(tableModel);
    private Repository repository;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public PlayerTableView() {
        createUIComponents();
    }

    private void createUIComponents() {
        scrollPanel.setViewportView(table);
        refreshButton.addActionListener(this::onRefreshButtonClicked);
        newButton.addActionListener(this::onNewButtonClicked);
        deleteButton.addActionListener(this::onDeleteButtonClicked);
        deleteButton.setEnabled(false);
        editButton.addActionListener(this::onEditButtonClicked);
        editButton.setEnabled(false);
        scrollPanel.setPreferredSize(new Dimension(800, 600));
        customizeTable();
    }

    private void customizeTable() {
        table.setShowGrid(true);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(250);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(200);
        columnModel.getColumn(6).setPreferredWidth(150);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            Player player = getSelectedPlayer();
            if (player != null) {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                int selectedRowIndex = table.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && selectedRowIndex != -1) {
                    onTableMouseDoubleClicked();
                }
            }
        });
    }

    public void refresh() {
        if (repository != null) {
            tableModel.setPlayers(repository.getAllPlayers());
        }
        tableModel.fireTableDataChanged();
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
        if (repository != null) {
            refresh();
        }
    }

    public Repository getRepository() {
        return repository;
    }

    private void onTableMouseDoubleClicked() {
        Player player = getSelectedPlayer();
        if (player != null) {
            showPlayerView(player, PlayerViewMode.VIEW);
        }
    }

    private void onRefreshButtonClicked(ActionEvent e) {
        refresh();
    }

    private void onNewButtonClicked(ActionEvent e) {
        showPlayerView(null, PlayerViewMode.CREATE);
    }

    private void showPlayerView(Player player, PlayerViewMode mode) {
        PlayerView playerView = new PlayerView();
        playerView.setPlayer(player);
        playerView.setMode(mode);
        playerView.setRepository(repository);
        Window window = SwingUtilities.getWindowAncestor(mainPanel);
        playerView.show(window);
        refresh();
    }

    private void onDeleteButtonClicked(ActionEvent e) {
        Player player = getSelectedPlayer();
        if (player != null) {
            showPlayerView(player, PlayerViewMode.DELETE);
        }
    }

    private void onEditButtonClicked(ActionEvent e) {
        Player player = getSelectedPlayer();
        if (player != null) {
            showPlayerView(player, PlayerViewMode.EDIT);
        }
    }

    private Player getSelectedPlayer() {
        if (table.getSelectedRow() >= 0 && table.getSelectedRow() < tableModel.getPayerCount()) {
            return tableModel.getPlayer(table.getSelectedRow());
        }
        return null;
    }
}

class PlayerTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "First Name", "Last Name", "Address", "Postal Code", "Province", "Phone Number"};
    private final ArrayList<Player> players = new ArrayList<>();

    public PlayerTableModel() {
        super();
    }

    public void setPlayers(Player[] players) {
        this.players.clear();
        this.players.addAll(Arrays.asList(players));
        fireTableDataChanged();
    }

    public Player getPlayer(int row) {
        return players.get(row);
    }

    public int getPayerCount() {
        return players.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return players.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Player player = players.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return player.getId();
            case 1:
                return player.getFirstName();
            case 2:
                return player.getLastName();
            case 3:
                return player.getAddress();
            case 4:
                return player.getPostalCode();
            case 5:
                return player.getProvince();
            case 6:
                return player.getPhoneNumber();
        }
        return null;
    }
}
