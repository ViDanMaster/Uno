package backend;

/**
 * Játékos osztály, mely egy UNO játékost reprezentál.
 */
public class Player {
    private String name;
    private Hand hand;

    /**
     * Játékos osztály konstruktora.
     *
     * @param name A játékos neve.
     * @param deck A játékos kezdeti kártyáit tartalmazó pakli.
     */
    public Player(String name, Deck deck) {
        this.name = name;
        this.hand = new Hand(deck);
    }

    /**
     * Visszaadja a játékos nevét.
     *
     * @return A játékos neve.
     */
    public String getName() {
        return name;
    }

    /**
     * Visszaadja a játékos kezében lévő kártyákat.
     *
     * @return A játékos kezében lévő kártyák.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Egy játékos húz egy kártyát a húzópakliból és a kezébe veszi (Hand-jéhez adja).
     *
     * @param deck A húzópakli referenciája, amiből húz egy kártyát a játékos.
     */
    public void drawCard(Deck deck) {
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            hand.addCard(drawnCard);
        } else {
            System.err.println("Deck is empty. Cannot draw a card.");
        }
    }

    /**
     * Egy játékos húz valamennyi kártyát a húzópakliból és a kezébe veszi (Hand-jéhez adja).
     *
     * @param numberOfCards Amennyi kártyát húz a játékos.
     * @param deck Húzópakli referenciája, amiből húzza a kártyát/kártyákat a játékos.
     */
    public void drawCardsFromDeck(int numberOfCards, Deck deck) {
        for (int i = 0; i < numberOfCards; i++) {
            Card drawnCard = deck.drawCard();
            if (drawnCard != null) {
                hand.addCard(drawnCard);
            } else {
                System.err.println("Deck is empty. Cannot draw a card.");
            }
        }
    }

    /**
     * Kijátszik egy kártyát a játékos a kezéből. Csak akkor játssza ki a kártyát, ha biztos van olyan kártya a kezében.
     *
     * @param card A kártya, amit kijátszik a játékos.
     */
    public void playCard(Card card) {
        while (!hand.getCards().isEmpty()){
            if (hand.getCards().contains(card)){
                hand.removeCard(card);
                break;
            } else {
                System.err.println("Card not found in hand. Play another card.");
            }
        }
    }


    /**
     * Visszaadja a játékos nevét és kezében levő lapokat.
     *
     * @return Játékos neve és kezében lévő lapok szöveges formában.
     */
    @Override
    public String toString() {
        return name + "'s hand:\n" + hand;
    }

}
