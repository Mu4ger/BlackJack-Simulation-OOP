public class Card
{
    // Stores necessary information for the cards. (Ranks / Suits / Value)
    public String rank, suit;
    public int value;
    Card(String r, String s, int v) // Constructor
    {
        this.rank = r;
        this.suit = s;
        this.value = v;
    }

    public void print() // Debug - print information
    {
        System.out.printf("%s of %s, value %d\n", this.rank, this.suit, this.value);
    }

    public String getFileName() // Get the file name of the image of this card
    {
        if (value == 0) // If this is the dealer's turned over card (value 0)
            return "cardImages/backCover.png";
        return String.format("cardImages/%s/%s.png", this.suit, this.rank); // Return file name
    }
}
