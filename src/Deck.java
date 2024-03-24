import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int deckSize = 108;


    public Deck(){
        cards = new ArrayList<>();
        initializeDeck();
        shuffleDeck(cards);
    }

    public int getDeckSize() {
        return deckSize;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    private void initializeDeck() {
        for (Color color : Color.values()) {
            if (color != Color.BLACK) {
                for (int i = 0; i <= 9; i++) {
                    cards.add(new Card(i, color, CardType.NUMBER, false));
                    if (i != 0) {
                        cards.add(new Card(i, color, CardType.NUMBER, false));
                    }
                }
                for (int i = 0; i < 2; i++) {
                    cards.add(new Card(15, color, CardType.DRAW_TWO, true));
                    cards.add(new Card(16, color, CardType.SKIP, true));
                    cards.add(new Card(17, color, CardType.REVERSE, true));
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(20, Color.BLACK, CardType.WILD, true));
            cards.add(new Card(21, Color.BLACK, CardType.WILD_DRAW_FOUR, true));
        }
    }

    private void shuffleDeck(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) (Math.random() * cards.size());
            Card temp = cards.get(i);
            cards.set(i, cards.get(randomIndex));
            cards.set(randomIndex, temp);
        }
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("Deck is empty");
            return null;
        }
        this.deckSize--;
        return cards.remove(0);
    }

    public void putBackAndShuffle(Card card){
        cards.add(card);
        shuffleDeck(cards);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deck contents:\n");
        for (Card card : cards) {
            sb.append(card.toString()).append("\n");
        }
        sb.append("Deck size: ").append(deckSize);
        return sb.toString();
    }

}