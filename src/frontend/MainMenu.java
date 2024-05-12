package frontend;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A főmenü ablak, amely az UNO játék beállításait tartalmazza.
 */
public class MainMenu extends JFrame {
    private JComboBox<String> playersComboBox;
    private ArrayList<JTextField> playerNameFields;

    /**
     * Konstruktor a főmenű ablak létrehozásához.
     */
    public MainMenu() {
        setTitle("Uno Játék");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBackground(new Color(242, 241, 239));

        JButton newGameButton = new JButton("Új játék kezdése");
        JLabel playersLabel = new JLabel("Játékosok száma:");
        playersComboBox = new JComboBox<>(new String[]{"2", "3", "4", "5", "6", "7", "8"});
        JPanel playerNamePanel = new JPanel();
        JLabel playerNameLabel = new JLabel("Játékos nevek:");
        JButton exitButton = new JButton("Kilépés");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        newGameButton.setFont(buttonFont);
        playersLabel.setFont(buttonFont);
        playersComboBox.setFont(buttonFont);
        playerNameLabel.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        newGameButton.setBackground(new Color(255, 221, 102));
        playersLabel.setBackground(new Color(242, 241, 239));
        playersComboBox.setBackground(new Color(102, 204, 255));
        playerNameLabel.setBackground(new Color(242, 241, 239));
        exitButton.setBackground(new Color(255, 102, 102));

        newGameButton.setMargin(new Insets(10, 20, 10, 20));
        exitButton.setMargin(new Insets(10, 20, 10, 20));

        newGameButton.addActionListener(e -> startNewGame());

        exitButton.addActionListener(e -> System.exit(0));

        panel.add(newGameButton);

        JPanel comboBoxPanel = new JPanel(new GridBagLayout());
        comboBoxPanel.setBackground(new Color(242, 241, 239));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        comboBoxPanel.add(playersLabel, gbc);
        gbc.gridy = 1;
        comboBoxPanel.add(playersComboBox, gbc);

        panel.add(comboBoxPanel);

        playerNamePanel.setLayout(new GridLayout(2, 1));
        playerNamePanel.setBackground(new Color(242, 241, 239));
        playerNamePanel.add(playerNameLabel);

        JPanel nameFieldsPanel = new JPanel();
        nameFieldsPanel.setLayout(new GridLayout(1, 2)); // Alapértelmezettnek két mezőt jelenítünk meg
        playerNameFields = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            JTextField textField = new JTextField("Játékos " + (i + 1));
            playerNameFields.add(textField);
            nameFieldsPanel.add(textField);
        }
        playerNamePanel.add(nameFieldsPanel);

        panel.add(playerNamePanel);

        playersComboBox.addActionListener(e -> {
            int numPlayers = Integer.parseInt((String) playersComboBox.getSelectedItem());
            nameFieldsPanel.removeAll();
            playerNameFields.clear();
            nameFieldsPanel.setLayout(new GridLayout(1, numPlayers));
            for (int i = 0; i < numPlayers; i++) {
                JTextField textField = new JTextField("Játékos " + (i + 1));
                playerNameFields.add(textField);
                nameFieldsPanel.add(textField);
            }
            nameFieldsPanel.revalidate();
            nameFieldsPanel.repaint();
        });

        panel.add(exitButton);

        add(panel);
    }

    /**
     * Új játék indítása.
     */
    private void startNewGame() {
        int numPlayers = Integer.parseInt((String) playersComboBox.getSelectedItem());
        dispose();
        ArrayList<String> names = new ArrayList<>();
        for (JTextField field : playerNameFields) {
            names.add(field.getText());
        }
        UnoGame unoGame = new UnoGame(numPlayers, names);
        unoGame.setVisible(true);
    }
}


