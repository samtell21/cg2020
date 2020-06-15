package card_game.general;

public class Card{
    private String[] nums = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    private String[] suits = {"Spades","Clubs","Hearts","Diamonds"};
    private String num;
    private String suit;
    
    public Card(int a, int b) throws Exception{
        try{
            num = nums[a];
            suit = suits[b];
        }
        catch(Exception e){
            throw new Exception("Cannot create card with ("+a+", "+b+")");
        }
    }
    
    public String getNum(){
        return num;
    }
    public String getSuit(){
        return suit;
    }
    
    public String toString(){
        return num+" of "+suit;
    }
    
    public boolean equals(Object c){
        return this.toString().equals(c.toString());
    }
}
