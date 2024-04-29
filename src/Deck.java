import java.util.*; // for ArrayList and Collections
public class Deck
{
    public ArrayList<Card> cards = new ArrayList<Card>();

    public void initFullDeck()
    {
        this.cards.clear();
        String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
        int[] rankValues = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };

        String[] suits = { "Clubs", "Diamonds", "Hearts", "Spades" };

        for (int i = 0; i < ranks.length; i++)
        {
            for (String suit : suits)
            {
                this.cards.add(new Card(ranks[i], suit, rankValues[i]));
            }
        }
    }

    public Card takeCard() // Removes card from top of Deck, ArrayList and returns it
    {
        if (this.cards.isEmpty())
        {
            System.out.println("No more Cards in Deck!");
            System.exit(0);
        }
        Card tempCard = this.cards.getLast();
        this.cards.removeLast();
        return tempCard;
    }

    public void shuffle()
    {
        long seed = System.nanoTime();
        Collections.shuffle(this.cards, new Random(seed)); //  Randomly permute the specified list using the specified source of randomness.
    }

    public int getTotalValue()
    {
        int totalValue = 0;
        for (Card card : this.cards)
        {
            totalValue += card.value;
        }
        return totalValue;
    }

    public int getNumAces()
    {
        int numAces = 0;
        for (Card card : this.cards)
        {
            if (Objects.equals(card.rank, "Ace"))
            {
                numAces++;
            }
        }
        return numAces;
    }

    public int getCount()
    {
        return this.cards.size();
    }

    public void print()
    {
        for (Card card : this.cards)
        {
            card.print();
        }
    }
}
