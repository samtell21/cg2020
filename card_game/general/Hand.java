
package card_game.general;
import java.util.LinkedList;

public class Hand {
    protected final LinkedList<Card> l = new LinkedList<>();
    protected int bet;
    public int getBet(){
        return bet;
    }
    
    public final Account player;
    public final Deck deck;
    public Hand(Account a, Deck d){
        player = a;
        deck = d;
    }
    
    public void hit(Deck d) throws OverdrawnException{
        l.add(d.drawRand());
    }
    
    public void betEx(int n) throws FundsException{
        if(player.bet(n)) bet+=n;
        else throw new FundsException("Insufficient Funds");
    }
    
    
    public boolean betBo(int n){
        boolean b = player.bet(n);
        if(b) bet+=n;
        return b;
    }
    
    @Override
    public String toString(){
        return l.toString();
    }
    
    
    public int size(){
        //System.out.println(l.size());
        return l.size();
    }
    
}
