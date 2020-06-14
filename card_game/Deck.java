package card_game;

import java.util.LinkedList;
import java.util.Random;

public class Deck{

    protected LinkedList<Card> deck;
    protected LinkedList<Card> table;
    protected LinkedList<Card> burn;
    
    public Deck(int n) throws Exception{
        init(n);
    }
    public Deck() throws Exception{
        init(1);
    }
    
    private void init(int n) throws Exception{
        if(n<1) throw new Exception("number of decks must be greater than 0");
        deck = new LinkedList<>();
        table = new LinkedList<>();
        burn = new LinkedList<>();
        for(int k = 0; k<n; k++)
            for(int i = 0; i<13; i++)
                for(int j = 0; j<4; j++)
                    deck.add(new Card(i,j));
    }
    
    public Card drawRand() throws OverdrawnException{
        Random r = new Random();
        int i;
        
        try{
            i = r.nextInt(deck.size());
        }
        catch(IllegalArgumentException e){
            swap();
            if(deck.isEmpty())
                throw new OverdrawnException("All the cards are on the table");
            return drawRand();
        }
        
        Card c = deck.remove(i);
        table.add(c);
        return c;
    }
    
    protected void swap(){
        LinkedList<Card> t = deck;
        deck = burn;
        burn = t;
    }
    
    public void burn(){
        while(!table.isEmpty())
            burn.add(table.pop());
    }
    
    public String toString(){
        return "Deck: "+deck+"\nTable: "+table+"\nBurn: "+burn;
    }
    
    public LinkedList<?> getDeck(){
        return deck;
    }
    public LinkedList<?> getTable(){
        return table;
    }

}
