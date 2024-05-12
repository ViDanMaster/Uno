package backend;

import java.util.ArrayList;

/**
 * Kéz osztály, mely egy UNO játékos kezét reprezentálja.
 */
public class Hand {
    private ArrayList<Card> cards;

    /**
     * Kéz osztály konstruktora, minden kézben 7 kártya van az inicializást követően.
     *
     * @param deck Az alapvető pakli, amiből a kezet inicializálja.
     */
    public Hand(Deck deck) {
        this.cards = new ArrayList<>();
        initializeHand(deck);
    }

    /**
     * Visszaadja a kézben lévő kártyákat.
     *
     * @return A kézben lévő kártyák listája.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Hozzáad egy kártyát a kézhez.
     *
     * @param card A hozzáadandó kártya.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Eltávolít egy kártyát a kézből.
     *
     * @param card Az eltávolítandó kártya.
     */
    public void removeCard(Card card) {
        cards.remove(card);
    }

    /**
     * Inicializálja a kéz kártyáit a pakli segítségével.
     *
     * @param deck A pakli, amiből a kéz kártyáit inicializálja.
     */
    private void initializeHand(Deck deck) {
        for (int i = 0; i < 7; i++) {
            Card randomCard = deck.drawCard();
            if (randomCard != null) {
                cards.add(randomCard);
            } else {
                System.out.println("Deck is empty");
                break;
            }
        }
    }

    /**
     * Visszaadja a kéz tartalmát szöveges formában.
     *
     * @return A kéz tartalma szöveges formában.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hand contents:\n");
        for(int i = 0; i < cards.size(); i++){
            sb.append(i).append(": ").append(cards.get(i)).append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }
}
