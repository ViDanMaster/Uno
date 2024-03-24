import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards;

    public Hand(Deck deck) {
        this.cards = new ArrayList<>();
        initializeHand(deck);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    private void initializeHand(Deck deck) {
        for (int i = 0; i < 7; i++) {
            Card randomCard = deck.drawCard();
            if(randomCard != null){
                cards.add(randomCard);
            }else{
                System.out.println("Deck is empty");
                break;
            }
        }
    }

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
