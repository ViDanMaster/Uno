package frontend;

import backend.Player;

import javax.swing.*;
import java.awt.*;

/**
 * A játékos panel, amely a játékos nevét és a kezében lévő kártyákat jeleníti meg a felhasználói felületen.
 */
public class PlayerPanel extends JPanel {
    private JLabel playerNameLabel;
    private JPanel handPanel;
    private Player player;
    private UnoGame unoGame;

    /**
     * Konstruktor az PlayerPanel objektum létrehozásához.
     * @param player A játékos, akinek a panelt meg kell jeleníteni.
     * @param unoGame Az UnoGame objektum, amelynek a részeként a panel megjelenik.
     */
    public PlayerPanel(Player player, UnoGame unoGame) {
        this.player = player;
        this.unoGame = unoGame;
        setLayout(new BorderLayout());

        playerNameLabel = new JLabel(player.getName());

        handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout());

        updateHand();

        JPanel playerInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerInfoPanel.add(playerNameLabel);
        playerInfoPanel.add(handPanel);

        add(playerInfoPanel, BorderLayout.CENTER);
    }

    /**
     * Visszaadja a játékost, akinek a panelt megjeleníti.
     * @return A játékos, akinek a panelt megjeleníti.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Frissíti a játékos kezében lévő kártyák megjelenítését és csak annak a játékosnak a lapjait jeleníti meg, aki éppen a sorban jön.
     */
    public void updateHand() {
        handPanel.removeAll();
        boolean isCurrentPlayer = unoGame.getCurrentPlayerIndex() == unoGame.getPlayerPanels().indexOf(this);
        for (int i = 0; i < player.getHand().getCards().size(); i++) {
            CardPanel cardPanel = new CardPanel(player.getHand().getCards().get(i), unoGame);
            if (isCurrentPlayer) {
                handPanel.add(cardPanel);
            } else {
                cardPanel.setEnabled(false);
                cardPanel.setBackground(Color.LIGHT_GRAY);
                cardPanel.setEmptyLabel();
                handPanel.add(cardPanel);
            }
        }
        repaint();
    }
}
