import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameManager {
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private Deck deck;
    private Card lastDroppedCard;
    private Scanner scanner = new Scanner(System.in);
    private boolean firstRound = true;

    public GameManager(int numberOfPlayers) {
        players = new ArrayList<>();
        deck = new Deck();
        for (int i = 1; i <= numberOfPlayers; i++) {
            createPlayer(i);
        }
        currentPlayerIndex = 0;
        drawFirstCard();
        Game();
    }

    public void createPlayer(int playerNumber) {
        players.add(new Player("Player " + playerNumber, deck));
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getNextPlayer() {
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card getLastDroppedCard() {
        return lastDroppedCard;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void Game() {
        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn.");
            System.out.println("Last dropped card: " + lastDroppedCard);
            System.out.println(currentPlayer);
            playTurn(currentPlayer);
            if (currentPlayer.hasWon()) {
                System.out.println(currentPlayer.getName() + " has won!");
                break;
            }
            nextPlayer();
        }
    }

    private void drawFirstCard() {
        lastDroppedCard = deck.drawCard();
        handlePlayedSpecialCard(lastDroppedCard, players.get(currentPlayerIndex), players.get(currentPlayerIndex));
        firstRound = false;
    }

    private void playTurn(Player player) {
        while (true) {
            System.out.print("Enter the index of the card you want to play (or -1 to draw a card): ");
            if (scanner.hasNextInt()) {
                int cardIndex = scanner.nextInt();
                if (cardIndex == -1) {
                    player.drawCard(deck);
                    break;
                }
                if (cardIndex >= 0 && cardIndex < player.getHand().getCards().size()) {
                    Card selectedCard = player.getHand().getCards().get(cardIndex);
                    if (selectedCard.getCardColor() == lastDroppedCard.getCardColor() || selectedCard.getCardNumber() == lastDroppedCard.getCardNumber() || selectedCard.getCardColor() == Color.BLACK) {
                        player.playCard(selectedCard);
                        handlePlayedSpecialCard(selectedCard, player, getNextPlayer());
                        lastDroppedCard = selectedCard;
                        break;
                    } else {
                        System.out.println("Invalid card selection. The selected card cannot be played.");
                    }
                } else {
                    System.out.println("Invalid card index. Please enter a valid index.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }

    public void drawCardsFromDeck(int numberOfCards, Player playerWhoDraws) {
        for (int i = 0; i < numberOfCards; i++) {
            Card drawnCard = deck.drawCard();
            if (drawnCard != null) {
                playerWhoDraws.getHand().addCard(drawnCard);
            } else {
                System.err.println("Deck is empty. Cannot draw a card.");
                break;
            }
        }
    }

    private void handlePlayedSpecialCard(Card card, Player player, Player nextPlayer) {
        switch (card.getCardType()) {
            case SKIP:
                skipTurn(player);
                break;
            case REVERSE:
                reverseTurn(player);
                break;
            case DRAW_TWO:
                drawCardsFromDeck(2, nextPlayer);
                skipTurn(player);
                break;
            case WILD:
                selectWildColor(card, player);
                break;
            case WILD_DRAW_FOUR:
                if(!firstRound){
                    selectWildColor(card, player);
                    challengeWildDrawFour(getNextPlayer(), player);
                }else{
                    System.out.println("Cannot play a Wild Draw Four card in the first round.");
                    deck.putBackAndShuffle(card);
                    drawFirstCard();
                }
                break;
            default:
                System.out.println("Regular card played.");
        }
    }

    private void skipTurn(Player player) {
        System.out.println(player.getName() + " played a Skip card. Next player's turn is skipped.");
        nextPlayer();
    }

    private void reverseTurn(Player player) {
        if(players.size() == 2) {
            System.out.println("Reverse card played. Skipped " + player.getName()+ "'s turn.");
            nextPlayer();
            return;
        }

        ArrayList<Player> players = this.getPlayers();
        Collections.reverse(players);
        if(firstRound){
            System.out.println("Reverse card played in the first round. The "+ players.get(0).getName() + " will start the game.");
        }else{
            this.setCurrentPlayerIndex(players.size() - currentPlayerIndex - 1);
            System.out.println(player.getName() + " played a Reverse card. Turn order has been reversed.");
        }
    }

    private void selectWildColor(Card card, Player player) {
        System.out.println("Select a color for the Wild card:");
        while (true) {
            String selectedColor = scanner.next().toUpperCase();
            if (selectedColor.equals("RED") || selectedColor.equals("BLUE") || selectedColor.equals("GREEN") || selectedColor.equals("YELLOW")) {
                Color color = Color.valueOf(selectedColor);
                card.setColor(color);
                System.out.println(player.getName() + " played a Wild card and chose " + color + " color.");
                break;
            } else {
                System.out.println("Invalid color selection. Please select a valid color.");
            }
        }
    }

    private void challengeWildDrawFour(Player player, Player challengedPlayer) {
        System.out.println("Do you want to challenge the Wild Draw Four card? (y/n)");
        String input = scanner.next();
        if(input.equals("y")) {
            System.out.println(challengedPlayer.getName() + " has been challenged for playing a Wild Draw Four card.");
            if (challengedPlayer.getHand().getCards().stream().anyMatch(card -> card.getCardColor() == lastDroppedCard.getCardColor())) {
                System.out.println(challengedPlayer.getName() + " has a card with the same color. Challenge succeeded.");
                System.out.println(challengedPlayer.getName() + " draws 4 cards.");
                drawCardsFromDeck(4, challengedPlayer);
            } else {
                System.out.println(challengedPlayer.getName() + " does not have a card with the same color. Challenge failed.");
                System.out.println(player.getName() + " draws 6 cards.");
                drawCardsFromDeck(6, player);
            }
        }else{
            drawCardsFromDeck(4, player);
            System.out.println(player.getName() + " decided not to challenge. Draw 4 cards.");
        }
    }

}
