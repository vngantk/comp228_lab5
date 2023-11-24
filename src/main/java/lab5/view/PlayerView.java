package lab5.view;

import lab5.application.Repository;
import lab5.domain.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
    private JButton editButton;
    private JLabel idLabel;

    private Player player;

    private JDialog dialog;
    private Repository repository;
    private PlayerViewMode mode;

    public PlayerView() {
        okButton.addActionListener(this::onOkButtonClicked);
        cancelButton.addActionListener(this::onCancelButtonClicked);
        editButton.addActionListener(this::onEditButtonClicked);
        idField.setFocusable(false);
        idField.setEditable(false);
    }

    private void onOkButtonClicked(ActionEvent e) {
        if (mode == PlayerViewMode.CREATE) {
            player = repository.createPlayer(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    provinceField.getText(),
                    phoneNumberField.getText()
            );
            System.out.println("Created player: " + player);
        } else if (mode == PlayerViewMode.EDIT) {
            player = repository.updatePlayer(
                    player.getId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    addressField.getText(),
                    postalCodeField.getText(),
                    provinceField.getText(),
                    phoneNumberField.getText()
            );
            System.out.println("Updated player: " + player);
        } else if (mode == PlayerViewMode.DELETE) {
            boolean deleted = repository.deletePlayer(player.getId());
            System.out.println("Deleted player: " + deleted);
        }
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
        }
    }

    private void onCancelButtonClicked(ActionEvent e) {
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
    }

    private void onEditButtonClicked(ActionEvent e) {
        if (mode == PlayerViewMode.VIEW) {
            mode = PlayerViewMode.EDIT;
            showPlayerForEdit();
        }
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
    }

    public Repository getRepository() {
        return repository;
    }

    public void setMode(PlayerViewMode mode) {
        this.mode = mode;
    }

    public PlayerViewMode getMode() {
        return mode;
    }

    public void show(Window owner) {
        if (dialog != null) {
            dialog.dispose();
        }
        dialog = new JDialog();
        dialog.setContentPane(getMainPanel());
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        switch (mode) {
            case VIEW:
                showPlayerForView();
                break;
            case EDIT:
                showPlayerForEdit();
                break;
            case CREATE:
                showPlayerForCreate();
                break;
            case DELETE:
                showPlayerForDelete();
                break;
        }
        dialog.setVisible(true);
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
    }

    private void showPlayerForView() {
        dialog.setTitle("Player");
        idField.setVisible(true);
        idLabel.setVisible(true);
        setFieldsEditable(false);
        editButton.setVisible(true);
        okButton.setVisible(false);
    }

    private void showPlayerForEdit() {
        dialog.setTitle("Edit Player");
        idField.setVisible(true);
        idLabel.setVisible(true);
        setFieldsEditable(true);
        editButton.setVisible(false);
        okButton.setVisible(true);
    }

    private void showPlayerForCreate() {
        dialog.setTitle("Create New Player");
        idField.setVisible(false);
        idLabel.setVisible(false);
        clearFields();
        setFieldsEditable(true);
        editButton.setVisible(false);
        okButton.setVisible(true);
    }

    private void showPlayerForDelete() {
        dialog.setTitle("Confirm to Delete Player...");
        idField.setVisible(true);
        idLabel.setVisible(true);
        setFieldsEditable(false);
        editButton.setVisible(false);
        okButton.setVisible(true);
    }
}
