public class Player {
    private String name;
    private Hand hand;

    public Player(String name, Deck deck) {
        this.name = name;
        this.hand = new Hand(deck);
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public void drawCard(Deck deck) {
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            hand.addCard(drawnCard);
            System.out.println(name + " drew a card: " + drawnCard);
        } else {
            System.err.println("Deck is empty. Cannot draw a card.");
        }
    }

    public void playCard(Card card) {
        while (!hand.getCards().isEmpty()){
            if (hand.getCards().contains(card)){
                hand.removeCard(card);
                System.out.println(name + " played a card: " + card);
                break;
            } else {
                System.err.println("Card not found in hand. Play another card.");
            }
        }
    }


    public boolean hasWon() {
        return hand.getCards().isEmpty();
    }

    @Override
    public String toString() {
        return name + "'s hand:\n" + hand;
    }

}
