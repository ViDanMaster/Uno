package backend;

import Types.CardType;
import Types.Color;

/**
 * Kártya osztály, mely egy UNO kártyát reprezentál.
 */
public class Card {
    private String cardText;
    private Color cardColor;
    private CardType cardType;
    private boolean isSpecial;

    /**
     * Kártya osztály konstruktora.
     *
     * @param cardText A kártyán szereplő szám vagy szöveg.
     * @param cardColor A kártya színe.
     * @param cardType A kártya típusa.
     * @param isSpecial Igaz, ha a kártya speciális, hamis egyébként.
     */
    public Card(String cardText, Color cardColor, CardType cardType, boolean isSpecial) {
        this.cardText = cardText;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.isSpecial = isSpecial;
    }

    /**
     * A kártyán szereplő szám vagy jel lekérdezése.
     *
     * @return A kártyán szereplő szám vagy jel.
     */
    public String getCardText() {
        return cardText;
    }

    /**
     * A kártya színének lekérdezése.
     *
     * @return A kártya színe.
     */
    public Color getCardColor() {
        return cardColor;
    }

    /**
     * A kártya színének beállítása.
     *
     * @param color A kártya új színe.
     */
    public void setColor(Color color) {
        this.cardColor = color;
    }

    /**
     * A kártya típusának lekérdezése.
     *
     * @return A kártya típusa.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Megadja, hogy a kártya speciális-e.
     *
     * @return Igaz, ha a kártya speciális, hamis egyébként.
     */
    public boolean isSpecial() {
        return isSpecial;
    }

    /**
     * Visszaadja a kártya színét, típusát és rajta szereplő szám vagy szöveg.
     *
     * @return A kártya színe, típusa és rajta szereplő szám vagy szöveg szöveges formában.
     */
    @Override
    public String toString() {
        return "[" + cardColor + " " + cardType + " " + cardText + "]";
    }
}
