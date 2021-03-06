package card_game.general;

import java.util.LinkedList;
import java.util.Random;

public class Deck{

    protected LinkedList<Card> deck;
    protected LinkedList<Card> table;
    protected LinkedList<Card> burn;
    
    public Deck(int n) throws DeckNumException, WTF{
        init(n);
    }
    public Deck() throws DeckNumException, WTF{
        init(1);
    }
    
    
    private void init(int n) throws DeckNumException, WTF{
        if(n<1) throw new DeckNumException("number of decks must be greater than 0");
        deck = new LinkedList<>();
        table = new LinkedList<>();
        burn = new LinkedList<>();
        for(int k = 0; k<n; k++)
            for(int i = 0; i<13; i++)
                for(int j = 0; j<4; j++){
                    try{
                        deck.add(new Card(i,j));
                    }
                    catch(Exception e){
                        throw new WTF("bad card");
                    }
                }
    }
    
    public Card drawRand() throws OverdrawnException{
        Random r = new Random();
        
        if(deck.isEmpty()){
            swap();
            if(deck.isEmpty())
                throw new OverdrawnException("All the cards are on the table");
        }
        int i = r.nextInt(deck.size());
        Card c = deck.remove(i);
        table.add(c);
        return c;
    }
    
    /*
    TODO OverdrawnException
    private Card draw(int i){
        Card c = deck.remove(i);
        table.add(c);
        return c;
    }
    
    TODO OverdrawnException
    public boolean draw(Card r) {
        return deck.remove(r);
        
    }
    
    */
    
    protected void swap(){
        LinkedList<Card> t = deck;
        deck = burn;
        burn = t;
    }
    
    public void burn(){
        while(!table.isEmpty())
            burn.add(table.pop());
    }
    
    /**
     *
     * @return
     */
    @Override
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
