package backend;

import Types.CardType;
import Types.Color;

import java.util.ArrayList;

/**
 * Húzópakli osztály, mely UNO kártyákkal van tele.
 */
public class Deck {
    private ArrayList<Card> cards;
    private int deckSize = 108;


    /**
     * Kártya pakli osztály konstruktora.
     */
    public Deck(){
        cards = new ArrayList<>();
        initializeDeck();
        shuffleDeck(cards);
    }

    /**
     * Visszaadja a pakli méretét.
     *
     * @return A pakli mérete.
     */
    public int getDeckSize() {
        return deckSize;
    }

    /**
     * Beállítja a pakli méretét.
     *
     * @param deckSize A pakli új mérete.
     */
    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    /**
     * Visszaadja a pakli kártyáit.
     *
     * @return A pakli kártyái.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Inicializálja a húzópakliban alapjáraton levő 108 kártyát.
     */
    private void initializeDeck() {
        for (Color color : Color.values()) {
            if (color != Color.BLACK) {
                for (int i = 0; i <= 9; i++) {
                    cards.add(new Card(String.valueOf(i), color, CardType.NUMBER, false));
                    if (i != 0) {
                        cards.add(new Card(String.valueOf(i), color, CardType.NUMBER, false));
                    }
                }
                for (int i = 0; i < 2; i++) {
                    cards.add(new Card("+2", color, CardType.DRAW_TWO, true));
                    cards.add(new Card("Kimaradsz", color, CardType.SKIP, true));
                    cards.add(new Card("Fordító", color, CardType.REVERSE, true));
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cards.add(new Card("Vad", Color.BLACK, CardType.WILD, true));
            cards.add(new Card("Vad +4", Color.BLACK, CardType.WILD_DRAW_FOUR, true));
        }
    }

    /**
     * A húzópakliban levő maradék kártyákat összekeveri.
     */
    public void shuffleDeck(ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) (Math.random() * cards.size());
            Card temp = cards.get(i);
            cards.set(i, cards.get(randomIndex));
            cards.set(randomIndex, temp);
        }
    }

    /**
     * Húz egy kártyát a húzópakliból.
     *
     * @return a kártyát, amit húztál.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("Deck is empty");
            return null;
        }
        this.deckSize--;
        return cards.remove(0);
    }

    /**
     * Visszatesz egy kártyát és összekeveri a húzópaklit.
     *
     * @param card A kártya amit visszateszel.
     */
    public void putBackAndShuffle(Card card){
        cards.add(card);
        shuffleDeck(cards);
    }


    /**
     * Visszadja a pakliban lévő lapokat és a pakli méretét.
     *
     * @return Pakliban lévő lapok és a pakli mérete szöveges formában.
     */
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