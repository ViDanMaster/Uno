package frontend;

import backend.Card;
import backend.Deck;
import backend.Player;
import Types.CardType;
import Types.Color;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Az Uno játék fő keretablaka, amely vezérli a játékmenetet és a felhasználói felületét a játéknak.
 */
public class UnoGame extends JFrame {
    private JLabel currentPlayerLabel;
    private JButton drawCardButton;
    private JButton unoButton;
    private ArrayList<PlayerPanel> playerPanels;
    private Stack<Card> discardPile = new Stack<>();
    private CardPanel drawPilePanel;
    private int currentPlayerIndex;
    private Card lastDroppedCard;
    private Deck deck;
    private boolean isReversed = false;

    /**
     * UnoGame osztály konstruktora.
     * @param numberOfPlayers A játékosok száma.
     * @param playerNames A játékosok nevei.
     */
    public UnoGame(int numberOfPlayers, ArrayList<String> playerNames) {
        // Keret beállítása
        setTitle("Uno Játék");
        setSize(1400, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Húzópakli és dobópakli inicializálása
        deck = new Deck();
        currentPlayerIndex = 0;
        lastDroppedCard = deck.drawCard();
        discardPile.push(lastDroppedCard);

        // Játékos panelek létrehozása
        playerPanels = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player;
            if (i < playerNames.size()) {
                player = new Player(playerNames.get(i), deck);
            } else {
                player = new Player("Játékos " + (i + 1), deck);
            }
            PlayerPanel playerPanel = new PlayerPanel(player, this);
            playerPanels.add(playerPanel);
        }

        // Fő panel inicializálása
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Felső panel inicializálása
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Dobópakli panel hozzáadása
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        drawPilePanel = new CardPanel(lastDroppedCard, this);
        topPanel.add(drawPilePanel, gbc);

        // Dobópakli címke hozzáadása
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        JLabel drawLabel = new JLabel("Dobópakli");
        drawLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(drawLabel, gbc);

        // Jelenlegi játékos címke hozzáadása
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        currentPlayerLabel = new JLabel("Jelenlegi játékos: " + playerPanels.get(currentPlayerIndex).getPlayer().getName());
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(currentPlayerLabel, gbc);

        // A felső panel a fő panelhez való adása
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Játékos panelek inicializálása és hozzáadása a fő panelhez
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new GridLayout(numberOfPlayers, 1));
        for (PlayerPanel playerPanel : playerPanels) {
            playersPanel.add(playerPanel);
        }
        mainPanel.add(playersPanel, BorderLayout.CENTER);

        // Gombok panel inicializálása és hozzáadása a fő panelhez
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Húzz lapot és UNO gomb hozzáadása
        drawCardButton = new JButton("Húzz egy lapot");
        unoButton = new JButton("UNO!");

        // Húzz lapot gomb eseménykezelője
        drawCardButton.addActionListener(e -> {
            checkMissedUno();
            Player currentPlayer = playerPanels.get(currentPlayerIndex).getPlayer();
            drawCard(currentPlayer);
            unoButton.setEnabled(false);
            nextPlayer();
            playerPanels.forEach(PlayerPanel::updateHand);
            System.out.println(deck.getDeckSize());
            System.out.println(deck.getCards());
        });

        // UNO gomb eseménykezelője
        unoButton.addActionListener(e -> {
            unoButton.setEnabled(false);
        });

        // Gombok hozzáadása a panelhez
        buttonPanel.add(drawCardButton);
        buttonPanel.add(unoButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Fő panel hozzáadása a keretablakhoz
        add(mainPanel);

        // Játékos panelek inicializálása
        for (PlayerPanel playerPanel : playerPanels) {
            playerPanel.updateHand();
        }

        // Kezdeti kártyahúzás hatásának kezelése
        handleInitialCardEffect();

    }

    /**
     * Dobópakli tetején levő kártya beállítása.
     * @param lastDroppedCard Az utolsó dobott kártya.
     */
    private void setLastDroppedCard(Card lastDroppedCard) {
        this.lastDroppedCard = lastDroppedCard;
    }

    /**
     * Kártya kijátszása eseménykezelő.
     * @param cardPanel A kijátszott kártyát megjelenítő panel.
     */
    public void playCard(CardPanel cardPanel) {
        Card playedCard = cardPanel.getCard();
        Player currentPlayer = playerPanels.get(currentPlayerIndex).getPlayer();

        if (currentPlayer.getHand().getCards().contains(playedCard)) {
            if (canPlayCard(playedCard)) {
                checkMissedUno();
                discardPile.push(playedCard);
                currentPlayer.playCard(playedCard);
                unoButton.setEnabled(currentPlayer.getHand().getCards().size() == 1);

                if (playedCard.isSpecial()) {
                    handlePlayedSpecialCard(playedCard);
                }

                setLastDroppedCard(playedCard);
                drawPilePanel.setCard(playedCard);
                nextPlayer();
                playerPanels.forEach(PlayerPanel::updateHand);
                checkWinner();

            } else {
                System.err.println("Nem lehet ezt a lapot kijátszani!");
            }
        } else {
            System.err.println("A kártya nincs a játékos kezében!");
        }

    }

    /**
     * Ellenőrzi, hogy a kijátszott kártyát ki lehet-e játszani.
     * @param playedCard A kijátszott kártya.
     * @return True, ha az érvényes, egyébként false.
     */
    private boolean canPlayCard(Card playedCard) {
        if (playedCard == null) {
            return false;
        }

        return playedCard.getCardColor() == lastDroppedCard.getCardColor() ||
                playedCard.getCardText().equals(lastDroppedCard.getCardText()) ||
                playedCard.getCardType() == CardType.WILD ||
                playedCard.getCardType() == CardType.WILD_DRAW_FOUR;
    }

    /**
     * Különleges kártyák hatásának kezelése.
     * @param card A kijátszott kártya.
     */
    private void handlePlayedSpecialCard(Card card) {
        Player currentPlayer = playerPanels.get(currentPlayerIndex).getPlayer();
        int increment = isReversed ? -1 : 1;
        int nextPlayerIndex = Math.floorMod(currentPlayerIndex + increment, playerPanels.size());
        Player nextPlayer = playerPanels.get(nextPlayerIndex).getPlayer();

        switch (card.getCardType()) {
            case SKIP:
                nextPlayer();
                break;
            case REVERSE:
                reverseTurn();
                break;
            case DRAW_TWO:
                drawCards(nextPlayer, 2);
                nextPlayer();
                break;
            case WILD:
                selectWildColor(card);
                break;
            case WILD_DRAW_FOUR:
                challengeWildDrawFour(nextPlayer, currentPlayer);
                selectWildColor(card);
                break;
        }
    }

    /**
     * A játékosok közötti irány megfordítása, 2 játékos esetén ez egy kimaradsz-al egyenlő.
     */
    private void reverseTurn() {
        if (playerPanels.size() > 2) {
            isReversed = !isReversed;
        }else{
            nextPlayer();
        }
    }

    /**
     * Vad kártya színének kiválasztása.
     * @param card A vad kártya.
     */
    private void selectWildColor(Card card) {
        JFrame colorSelectionFrame = new JFrame("Válassz színt!");
        colorSelectionFrame.setSize(600, 400);
        colorSelectionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        colorSelectionFrame.setLocationRelativeTo(null);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(2, 2));

        JButton redButton = new JButton("Piros");
        redButton.addActionListener(e -> {
            card.setColor(Color.RED);
            drawPilePanel.setCard(card);
            colorSelectionFrame.dispose();
        });

        JButton blueButton = new JButton("Kék");
        blueButton.addActionListener(e -> {
            card.setColor(Color.LIGHT_BLUE);
            drawPilePanel.setCard(card);
            colorSelectionFrame.dispose();
        });

        JButton greenButton = new JButton("Zöld");
        greenButton.addActionListener(e -> {
            card.setColor(Color.GREEN);
            drawPilePanel.setCard(card);
            colorSelectionFrame.dispose();
        });

        JButton yellowButton = new JButton("Sárga");
        yellowButton.addActionListener(e -> {
            card.setColor(Color.YELLOW);
            drawPilePanel.setCard(card);
            colorSelectionFrame.dispose();
        });

        colorPanel.add(redButton);
        colorPanel.add(blueButton);
        colorPanel.add(greenButton);
        colorPanel.add(yellowButton);

        colorSelectionFrame.add(colorPanel);
        colorSelectionFrame.setVisible(true);
    }

    /**
     * Vad +4 kártya kihívásának implementálása. +4 lapot csak akkor lehet kijátszani, ha nincs olyan színű kártyája, ami a dobópakli tetején van.
     * Ha kihívást elfogadja a soron következő játékos és megnyeri (azaz volt olyan színű kártyája a +4-es lapot kijátszó játékosnak, akkor a +4-est kijátszó játékos húz 4 lapot).
     * Ha a kihívást elfogadja a soron következő játékos, és nem nyeri meg (azaz tényleg nem volt olyan színű kártyája a +4-es lapot kijátszó játékosnak, akkor a soron következő játékos fog húzni 6 lapot).
     * Ha nem fogadja el a kihívást, akkor a soron következő játékos húz 4 lapot.
     * @param player A játékos, aki lerakta a +4-et.
     * @param challengedPlayer A játékos, aki a sorban következik.
     */
    private void challengeWildDrawFour(Player player, Player challengedPlayer) {
        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                challengedPlayer.getName() + " +4 lapot játszott. Szeretnéd kihívni? Ha van olyan színű lapja, mint ami a dobópakli tetején van, akkor ő húz négyet, de ha kihívod és nincs neki olyan lapja, akkor te húzol 6-ot. Ha nem hívod ki, te húzol 4-et.",
                "Kártya kihívása",
                JOptionPane.YES_NO_OPTION
        );

        if (dialogResult == JOptionPane.YES_OPTION) {
            boolean hasMatchingColor = challengedPlayer.getHand().getCards().stream()
                    .anyMatch(card -> card.getCardColor() == lastDroppedCard.getCardColor());
            if (hasMatchingColor) {
                JOptionPane.showMessageDialog(this, challengedPlayer.getName() + "-nak van megfelelő színű lapja. A kihívás sikeres volt.\n" + challengedPlayer.getName() + " 4 lapot húz.");
                drawCards(challengedPlayer, 4);
            } else {
                JOptionPane.showMessageDialog(this, challengedPlayer.getName() + "-nak nincs megfelelő színű lapja. A kihívás sajnos sikertelen volt.\n" + player.getName() + " 6 lapot húz.");
                drawCards(player, 6);
                nextPlayer();
            }
        } else {
            drawCards(player, 4);
            JOptionPane.showMessageDialog(this, player.getName() + " úgy döntött, hogy nem hívja ki. 4 lapot húz.");
            nextPlayer();
        }
    }


    /**
     * Következő játékosra ugrás.
     */
    private void nextPlayer() {
        int increment = isReversed ? -1 : 1;
        currentPlayerIndex = Math.floorMod(currentPlayerIndex + increment, playerPanels.size());
        currentPlayerLabel.setText("Jelenlegi játékos: " + playerPanels.get(currentPlayerIndex).getPlayer().getName());
    }
    /**
     * Aktuális játékos indexének lekérdezése.
     * @return Az aktuális játékos indexe.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Játékos panel lista lekérdezése.
     * @return A játékos panel lista.
     */
    public ArrayList<PlayerPanel> getPlayerPanels() {
        return playerPanels;
    }

    /**
     * Leellenőrzi, hogy valaki megnyerte-e már a játékot. (Természetesen csak akkor tud nyerni, ha bejelentette az UNO-t.)
     */
    private void checkWinner() {
        for (PlayerPanel playerPanel : playerPanels) {
            if (playerPanel.getPlayer().getHand().getCards().isEmpty()) {
                JOptionPane.showMessageDialog(this, playerPanel.getPlayer().getName() + " nyert!");
                dispose();
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            }
        }
    }

    /**
     * Azt ellenőrzi, hogy az a játékos, akinek csak 1 lapja van bejelentette-e az UNO-t.
     */
    private void checkMissedUno() {
        for (PlayerPanel playerPanel : playerPanels) {
            Player player = playerPanel.getPlayer();

            if (player.getHand().getCards().size() == 1 && unoButton.isEnabled()) {
                JOptionPane.showMessageDialog(this, player.getName() + " elfelejtett UNO-t mondani! 2 lapot húz.");
                player.drawCardsFromDeck(2, deck);
                playerPanel.updateHand();
            }
        }
    }

    /**
     * Egy kártya húzása a húzópakliból. Ez a függvény azt is ellenőrzi, hogy tud-e ennyi lapot húzni a játékos és ha nem, akkor a dobópakliban a legfelső kártya kivételével visszakeveri az összes lapot a húzópakliba.
     * @param playerWhoDraws A játékos, aki húz.
     */
    private void drawCard(Player playerWhoDraws) {
        if (deck.getDeckSize() < 1) {
            RefillDrawPile();
        }
        playerWhoDraws.drawCard(deck);
    }

    /**
     * Több lap húzása a húzópakliból. Ez a függvény azt is ellenőrzi, hogy tud-e ennyi lapot húzni a játékos és ha nem, akkor a dobópakliban a legfelső kártya kivételével visszakeveri az összes lapot a húzópakliba.
     * @param playerWhoDraws A játékos, aki húz.
     * @param numCards Húzandó lapok száma.
     */
    public void drawCards(Player playerWhoDraws, int numCards) {
        if (deck.getDeckSize() < numCards) {
            RefillDrawPile();
        }

        playerWhoDraws.drawCardsFromDeck(numCards, deck);
    }

    /**
     * Dobópakli újrakeverése és a húzópakliba való áttevése. A legfelső lap a dobópakli tetején megmarad és az lesz az egyedüli lap a dobópakliban a függvény lefutása után.
     */
    private void RefillDrawPile() {
        Card topDiscard = discardPile.pop();
        deck.getCards().addAll(discardPile);
        discardPile.clear();
        deck.setDeckSize(deck.getCards().size());
        deck.shuffleDeck(deck.getCards());
        discardPile.push(topDiscard);
        drawPilePanel.setCard(topDiscard);
        System.out.println(deck.getDeckSize());
        System.out.println(deck.getCards());
        if(deck.getDeckSize() == 0){
            JOptionPane.showMessageDialog(this, "A dobópakliban csak a legfelső kártya szerepel és a húzópakliban elfogyott a kártya! Nem tudsz húzni lapot.");
        }else{
            JOptionPane.showMessageDialog(this, "Elfogyott a húzópakli, ezért a dobópaklit újrakevertük a húzópakliba!");
        }
    }

    /**
     * Kezdeti kártyahúzás hatásának kezelése a játékszabály szerint.
     */
    private void handleInitialCardEffect() {
        CardType cardType = lastDroppedCard.getCardType();
        System.out.println(lastDroppedCard);

        switch (cardType) {
            case SKIP:
                JOptionPane.showMessageDialog(this, "Az első kártya kimaradsz volt. A kezdő játékos kimarad a körből!");
                nextPlayer();
                break;
            case REVERSE:
                JOptionPane.showMessageDialog(this, "Az első visszafordító volt. Az utolsó játékos kezd és visszafele halad a kör!");
                isReversed = true;
                nextPlayer();
                break;
            case DRAW_TWO:
                JOptionPane.showMessageDialog(this, "Az első kártya +2 volt. A kezdő játékos húz két lapot és kimarad a körből!");
                drawCards(playerPanels.get(currentPlayerIndex).getPlayer(), 2);
                nextPlayer();
                break;
            case WILD:
                JOptionPane.showMessageDialog(this, "Az első kártya vad volt. A kezdő játékos választhat színt!");
                selectWildColor(lastDroppedCard);
                break;
            case WILD_DRAW_FOUR:
                JOptionPane.showMessageDialog(this, "Az első kártya vad +4 volt. A kártyát visszatettük a pakliba és új lapot teszünk a dobópakliba!");
                deck.putBackAndShuffle(lastDroppedCard);
                discardPile.pop();
                lastDroppedCard = deck.drawCard();
                drawPilePanel.setCard(lastDroppedCard);
                discardPile.push(lastDroppedCard);
                handleInitialCardEffect();
                break;
            default:
                break;
        }

        playerPanels.forEach(PlayerPanel::updateHand);
    }


}
