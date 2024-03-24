public class Card {
    private int cardNumber;
    private Color cardColor;
    private CardType cardType;
    private boolean isSpecial;

    public Card(int cardNumber, Color cardColor, CardType cardType, boolean isSpecial) {
        this.cardNumber = cardNumber;
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.isSpecial = isSpecial;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public Color getCardColor() {
        return cardColor;
    }

    public void setColor(Color color) {
        this.cardColor = color;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    @Override
    public String toString() {
        return "[" + cardColor + " " + cardType + " " + cardNumber + "]";
    }
}
