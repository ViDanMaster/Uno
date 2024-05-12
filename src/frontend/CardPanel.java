package frontend;

import backend.Card;
import Types.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * KártyaPanel osztály, mely egy UNO kártyát reprezentál a felhasználói felületen.
 */
public class CardPanel extends JPanel {
    private Card card;
    private JLabel numberLabel;

    /**
     * KártyaPanel osztály konstruktora.
     *
     * @param card     Az UNO kártya, amit a panel reprezentál.
     * @param unoGame  Az UNO játék objektuma, ahol a kártya elhelyezkedik.
     */
    public CardPanel(Card card, UnoGame unoGame) {
        this.card = card;
        setPreferredSize(new Dimension(80, 90));

        switch (card.getCardColor()) {
            case RED:
                setBackground(Color.RED.getColor());
                break;
            case GREEN:
                setBackground(Color.GREEN.getColor());
                break;
            case LIGHT_BLUE:
                setBackground(Color.LIGHT_BLUE.getColor());
                break;
            case YELLOW:
                setBackground(Color.YELLOW.getColor());
                break;
            case BLACK:
                setBackground(Color.BLACK.getColor());
                break;
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        numberLabel = new JLabel(String.valueOf(card.getCardText()));
        numberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        if(this.card.getCardColor() == Color.BLACK){
            numberLabel.setForeground(java.awt.Color.white);
        }
        add(numberLabel, gbc);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                unoGame.playCard(CardPanel.this);
            }
        });
    }

    /**
     * Visszaadja a kártyát.
     *
     * @return A kártya.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Beállítja a kártyát.
     *
     * @param card Az új kártya.
     */
    public void setCard(Card card) {
        this.card = card;
        updateCardAppearance();
    }

    /**
     * Visszaadja a kártyán levő számot vagy szöveget.
     *
     * @return A kártyán levő szám vagy szöveg.
     */
    public JLabel getNumberLabel() {
        return numberLabel;
    }

    /**
     * Beállítja a kártya színét.
     *
     * @param color Az új szín.
     */
    public void setColor(Color color) {
        this.card.setColor(color);
        updateCardAppearance();
    }

    /**
     * Beállítja a számot jelző címkét.
     *
     * @param numberLabel Az új számot jelző JLabel.
     */
    public void setNumberLabel(JLabel numberLabel) {
        this.numberLabel = numberLabel;
    }

    /**
     * Frissíti a kártya megjelenését.
     */
    private void updateCardAppearance() {
        if (card != null) {
            setBackground(card.getCardColor().getColor());
            numberLabel.setText(String.valueOf(card.getCardText()));
        }
    }

    /**
     * Beállítja az üres JLabelt.
     */
    public void setEmptyLabel() {
        numberLabel.setText("");
    }

}