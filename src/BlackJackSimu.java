import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlackJackSimu
{

    // GUI elements
    private static final JFrame frame = new MainFrame();
    private static JTextField tfBalance;
    private static JLabel lblInitialBalance;
    private static JButton btnNewGame;
    private static JButton btnEndGame;
    private static JTextField tfBetAmount;
    private static JLabel lblEnterBet;
    private static JButton btnDeal;
    private static JLabel lblCurrentBalance;
    private static JLabel lblBalanceAmount;
    private static JLabel lblDealer;
    private static JLabel lblPlayer;
    private static JButton btnHit;
    private static JButton btnStand;
    private static JLabel lblBetAmount;
    private static JLabel lblBetAmountDesc;
    private static JLabel lblInfo;
    private static JButton btnContinue;
    private static JLabel lblShuffleInfo = null;

    // Game logic variables
    private static Deck deck, dealerCards, playerCards;
    private static DeckPanel dealerCardPanel = null, playerCardPanel = null;
    private static Card dealerHiddenCard;
    private static double balance = 0.0;
    private static int betAmount = 0, roundCount = 0;

    public static boolean isValidAmount(String s)
    {
        try
        {
            // Ensure amount entered is > 0
            return Integer.parseInt(s) > 0;
        }
            catch (NumberFormatException e)
            {
                // If not a valid integer
                return false;
            }
    }

    // Initialize GUI objects for entering initial balance and starting/stopping the game
    public static void initGuiObjects()
    {
        btnNewGame = new JButton("Start");
        btnNewGame.addActionListener(e -> newGame());
        btnNewGame.setBounds(20, 610, 99, 50);
        frame.getContentPane().add(btnNewGame);

        btnEndGame = new JButton("Stop");
        btnEndGame.setEnabled(false);
        btnEndGame.setBounds(121, 610, 99, 50);
        btnEndGame.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.repaint();
            initGuiObjects();
        });
        frame.getContentPane().add(btnEndGame);

        tfBalance = new JTextField();
        tfBalance.setText("100");
        tfBalance.setBounds(131, 580, 89, 28);
        frame.getContentPane().add(tfBalance);
        tfBalance.setColumns(10);

        lblInitialBalance = new JLabel("Bankroll:");
        lblInitialBalance.setFont(new Font("Helvetica", Font.BOLD, 13));
        lblInitialBalance.setForeground(Color.WHITE);
        lblInitialBalance.setBounds(30, 586, 100, 16);
        frame.getContentPane().add(lblInitialBalance);
    }

    public static void showBetGui()
    {
        btnEndGame.setEnabled(true);

        lblCurrentBalance = new JLabel("Current Balance:");
        lblCurrentBalance.setHorizontalAlignment(SwingConstants.CENTER);
        lblCurrentBalance.setFont(new Font("Helvetica", Font.BOLD, 16));
        lblCurrentBalance.setForeground(Color.WHITE);
        lblCurrentBalance.setBounds(315, 578, 272, 22);
        frame.getContentPane().add(lblCurrentBalance);

        lblBalanceAmount = new JLabel();
        lblBalanceAmount.setText(String.format("$%.2f", balance));
        lblBalanceAmount.setForeground(Color.ORANGE);
        lblBalanceAmount.setFont(new Font("Arial", Font.BOLD, 40));
        lblBalanceAmount.setHorizontalAlignment(SwingConstants.CENTER);
        lblBalanceAmount.setBounds(315, 600, 272, 50);
        frame.getContentPane().add(lblBalanceAmount);

        lblInfo = new JLabel("Enter a bet and click Deal");
        lblInfo.setBackground(Color.ORANGE);
        lblInfo.setOpaque(false);
        lblInfo.setForeground(Color.ORANGE);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setBounds(290, 482, 320, 28);
        frame.getContentPane().add(lblInfo);

        tfBetAmount = new JTextField();
        tfBetAmount.setText("10");
        tfBetAmount.setBounds(790, 580, 89, 28);
        frame.getContentPane().add(tfBetAmount);

        lblEnterBet = new JLabel("Enter Bet:");
        lblEnterBet.setFont(new Font("Arial", Font.BOLD, 14));
        lblEnterBet.setForeground(Color.WHITE);
        lblEnterBet.setBounds(689, 586, 100, 16);
        frame.getContentPane().add(lblEnterBet);

        btnDeal = new JButton("Deal");
        btnDeal.setBounds(679, 610, 200, 50);
        btnDeal.addActionListener(e -> deal());
        frame.getContentPane().add(btnDeal);
        btnDeal.requestFocus();

        frame.repaint();
    }

    public static void deal()
    {
        if (lblShuffleInfo != null)
            frame.getContentPane().remove(lblShuffleInfo);

        dealerCards = new Deck();
        playerCards = new Deck();

        if (isValidAmount(tfBetAmount.getText()))
        {
            betAmount = Integer.parseInt(tfBetAmount.getText());
        }
            else
        {
            lblInfo.setText("Error:Must be a Valid Bet!");
            tfBetAmount.requestFocus();
            return;
        }

        if (betAmount > balance)
        {
            lblInfo.setText("Error: Not Enough in Balance!");
            tfBetAmount.requestFocus();
            return;
        }
        balance -= betAmount;

        lblBalanceAmount.setText(String.format("$%.2f", balance));

        tfBetAmount.setEnabled(false);
        btnDeal.setEnabled(false);

        lblInfo.setText("Please Hit or Stand");

        lblDealer = new JLabel("Dealer");
        lblDealer.setForeground(Color.BLACK);
        lblDealer.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblDealer.setBounds(415, 158, 82, 28);
        frame.getContentPane().add(lblDealer);

        lblPlayer = new JLabel("Player");
        lblPlayer.setForeground(Color.BLACK);
        lblPlayer.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblPlayer.setBounds(415, 266, 82, 28);
        frame.getContentPane().add(lblPlayer);

        btnHit = new JButton("Hit");
        btnHit.setBounds(290, 515, 140, 35);
        btnHit.addActionListener(e -> hit());
        frame.getContentPane().add(btnHit);
        btnHit.requestFocus();

        btnStand = new JButton("Stand");
        btnStand.setBounds(470, 515, 140, 35);
        btnStand.addActionListener(e -> stand());
        frame.getContentPane().add(btnStand);

        btnContinue = new JButton("Continue");
        btnContinue.setEnabled(false);
        btnContinue.setVisible(false);
        btnContinue.setBounds(290, 444, 320, 35);
        btnContinue.addActionListener(e -> acceptOutcome());
        frame.getContentPane().add(btnContinue);

        lblBetAmount = new JLabel();
        lblBetAmount.setText("$" + betAmount);
        lblBetAmount.setHorizontalAlignment(SwingConstants.CENTER);
        lblBetAmount.setForeground(Color.green);
        lblBetAmount.setFont(new Font("Arial", Font.BOLD, 40));
        lblBetAmount.setBounds(679, 488, 200, 50);
        frame.getContentPane().add(lblBetAmount);

        lblBetAmountDesc = new JLabel("Bet Amount:");
        lblBetAmountDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblBetAmountDesc.setForeground(Color.WHITE);
        lblBetAmountDesc.setFont(new Font("Arial", Font.BOLD, 16));
        lblBetAmountDesc.setBounds(689, 465, 190, 22);
        frame.getContentPane().add(lblBetAmountDesc);

        frame.repaint();

        dealerHiddenCard = deck.takeCard();
        dealerCards.cards.add(new Card("", "", 0));
        dealerCards.cards.add(deck.takeCard());

        playerCards.cards.add(deck.takeCard());
        playerCards.cards.add(deck.takeCard());

        updateCardPanels();

        simpleOutcomes();
    }

    public static void hit()
    {
        playerCards.cards.add(deck.takeCard());
        updateCardPanels();

        simpleOutcomes();
    }

    public static boolean simpleOutcomes()
    {
        boolean outcomeHasHappened = false;
        int playerScore = playerCards.getTotalValue();
        if (playerScore > 21 && playerCards.getNumAces() > 0)
            playerScore -= 10;

        if (playerScore == 21)
        {
            dealerCards.cards.set(0, dealerHiddenCard);
            updateCardPanels();
                if (dealerCards.getTotalValue() == 21)
                {
                    lblInfo.setText("Push!");
                    balance += betAmount;
                }
                    else
                    {
                        lblInfo.setText(String.format("Player gets Blackjack! Profit: $%.2f", 1.5f * betAmount));
                         balance += 2.5f * betAmount;
                    }
                    lblBalanceAmount.setText(String.format("$%.2f", balance));
                    outcomeHasHappened = true;
                    outcomeHappened();
                }
                    else if (playerScore > 21)
                {
                    lblInfo.setText("Player goes Bust! Loss: $" + betAmount);
                    dealerCards.cards.set(0, dealerHiddenCard);
                    updateCardPanels();
                    outcomeHasHappened = true;
                    outcomeHappened();
                }
        return outcomeHasHappened;
    }

    public static void stand()
    {
        if (simpleOutcomes())
            return;

        int playerScore = playerCards.getTotalValue();
        if (playerScore > 21 && playerCards.getNumAces() > 0)
            playerScore -= 10;

        dealerCards.cards.set(0, dealerHiddenCard);

        int dealerScore = dealerCards.getTotalValue();

        while (dealerScore < 16)
        {
            dealerCards.cards.add(deck.takeCard());
            dealerScore = dealerCards.getTotalValue();
            if (dealerScore > 21 && dealerCards.getNumAces() > 0)
                dealerScore -= 10;
        }
        updateCardPanels();

        if (playerScore > dealerScore)
        {
            lblInfo.setText("Player wins! Profit: $" + betAmount);
            balance += betAmount * 2;
            lblBalanceAmount.setText(String.format("$%.2f", balance));
        }
            else if (dealerScore == 21)
        {
            lblInfo.setText("Dealer gets Blackjack! Loss: $" + betAmount);
        }
            else if (dealerScore > 21)
        {
            lblInfo.setText("Dealer goes Bust! Profit: $" + betAmount);
            balance += betAmount * 2;
            lblBalanceAmount.setText(String.format("$%.2f", balance));
        }
            else if (playerScore == dealerScore)
        {
            lblInfo.setText("Push!");
            balance += betAmount;
            lblBalanceAmount.setText(String.format("$%.2f", balance));
        }
            else
        {
            lblInfo.setText("Dealer Wins! Loss: $" + betAmount);
        }
        outcomeHappened();
    }

    public static void outcomeHappened()
    {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);

        lblInfo.setOpaque(true);
        lblInfo.setForeground(Color.RED);
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run() {
                btnContinue.setEnabled(true);
                btnContinue.setVisible(true);
                btnContinue.requestFocus();
            }
        }, 500);
    }

    public static void acceptOutcome()
    {
        lblInfo.setOpaque(false);
        lblInfo.setForeground(Color.ORANGE);

        frame.getContentPane().remove(lblDealer);
        frame.getContentPane().remove(lblPlayer);
        frame.getContentPane().remove(btnHit);
        frame.getContentPane().remove(btnStand);
        frame.getContentPane().remove(lblBetAmount);
        frame.getContentPane().remove(lblBetAmountDesc);
        frame.getContentPane().remove(btnContinue);
        frame.getContentPane().remove(dealerCardPanel);
        frame.getContentPane().remove(playerCardPanel);
        lblInfo.setText("Please enter a bet and click Deal");
        tfBetAmount.setEnabled(true);
        btnDeal.setEnabled(true);
        btnDeal.requestFocus();
        frame.repaint();

        if (balance <= 0)
        {
            int choice = JOptionPane.showOptionDialog(null, "You have run out of funds. Press Yes to add $100, or No to end the current game.", "Out of funds", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (choice == JOptionPane.YES_OPTION)
            {
                balance += 100;
                lblBalanceAmount.setText(String.format("$%.2f", balance));
            }
                else
            {
                frame.getContentPane().removeAll();
                frame.repaint();
                initGuiObjects();
                return;
            }
        }

        roundCount++;
        if (roundCount >= 5)
        {
            deck.initFullDeck();
            deck.shuffle();

            lblShuffleInfo = new JLabel("Deck has been replenished and reshuffled!");
            lblShuffleInfo.setForeground(Color.ORANGE);
            lblShuffleInfo.setFont(new Font("Arial", Font.BOLD, 20));
            lblShuffleInfo.setHorizontalAlignment(SwingConstants.CENTER);
            lblShuffleInfo.setBounds(235, 307, 430, 42);
            frame.getContentPane().add(lblShuffleInfo);

            roundCount = 0;
        }
    }

    public static void newGame()
    {
        if (isValidAmount(tfBalance.getText()))
        {
            balance = Integer.parseInt(tfBalance.getText());
        }
            else
        {
            JOptionPane.showMessageDialog(frame, "Invalid balance! Please ensure it is a natural number.", "Error", JOptionPane.ERROR_MESSAGE);
            tfBalance.requestFocus();
            return;
        }

        btnNewGame.setEnabled(false);
        tfBalance.setEnabled(false);

        showBetGui();

        roundCount = 0;

        deck = new Deck();
        deck.initFullDeck();
        deck.shuffle();
    }

    public static void updateCardPanels()
    {
        if (dealerCardPanel != null)
        {
            frame.getContentPane().remove(dealerCardPanel);
            frame.getContentPane().remove(playerCardPanel);
        }

        dealerCardPanel = new DeckPanel(dealerCards, 420 - (dealerCards.getCount() * 40), 50, 70, 104, 10);
        frame.getContentPane().add(dealerCardPanel);
        playerCardPanel = new DeckPanel(playerCards, 420 - (playerCards.getCount() * 40), 300, 70, 104, 10);
        frame.getContentPane().add(playerCardPanel);
        frame.repaint();
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        initGuiObjects();
        frame.setVisible(true);
    }

    public static int heightFromWidth(int width)
    {
        return (int) (1f * width * (380f / 255f));
    }
}
