package card_game.bj;

import card_game.general.*;
import java.util.LinkedList;

public class Hand2{
    private final LinkedList<Card> l = new LinkedList<>();
    private int b;
    
    private Deck defaultDeck;
    
    //TODO rework all of these to use a common init
    //      public Hand2(Card[] l, Deck d) and public Hand2(Card[] l)
    
    public Hand2(Deck d) throws OverdrawnException{
        b = 0;
        hit(d); hit(d);
        
        defaultDeck = d;
    }
    protected Hand2 (Card c, Deck d) throws OverdrawnException{
        b=0;
        l.add(c);
        hit(d);
        
        defaultDeck = d;
    }
    
    //TODO deck num exception (ctrl-f when refactoring)
    public Hand2(Card c, Card d) throws Exception, OverdrawnException{
        l.add(c);
        l.add(d);
        
        defaultDeck = new Deck();
        if(!(defaultDeck.draw(c) && defaultDeck.draw(d))) throw new OverdrawnException("cannot create... gonna refactor anyway, who cares");
    }
    
    //TODO deck num exception (ctrl-f when refactoring)
    public Hand2() throws Exception{
        defaultDeck = new Deck();
        
        b = 0;
        hit(defaultDeck); hit(defaultDeck);
    }
    
    public void hit(Deck d) throws OverdrawnException{
        l.add(d.drawRand());
    }
    
    public void hit() throws OverdrawnException{
        hit(defaultDeck);
    }
    
    public Hand2 split(Deck d) throws Exception{
        //hits on this instance, removes one of the twins, and returns a new Hand2 instance with that one and Deck d
        //utilizes the protected constructor, which does not set a b value.
        //TODO after reviewing BlackJack.java, consider setting bet on split instance
        if(!isSplit())
            //TODO this needs a better message...
            throw new Exception("Hand not splittable");
        hit(d);
        return new Hand2(l.remove(1),d);
    }
    
    public Hand2 split() throws Exception{
        return split(defaultDeck);
    }
    
    public void bet(int n){
        b+=n;
    }
    public int getBet(){
        return b;
    }
    
    public void doubledown(){
        b *= 2;
    }
    
    public String toString(){
        return l.toString();
    }
    public String dealerString(){
        return "[XXXX, "+ l.get(1) +"]";
    }
    
    public boolean dealerHit(Deck d) throws OverdrawnException{
        if(tot()<17){
            hit(d);
            return true;
        }
        else{
            return false;
        }
    }
    public boolean dealerHit() throws OverdrawnException{
        return dealerHit(defaultDeck);
    }
    
    int highNutBust(LinkedList<Integer> l) throws BustException{
        LinkedList<Integer> m = new LinkedList<>();
        while(!l.isEmpty()){
            int x = l.pop();
            if(x<=21)
                m.add(x);
        }
        if(m.isEmpty())
            throw new BustException("All are bust!");
        int x = m.get(0);
        for(int i=1;i<m.size();i++)
            if(m.get(i)>x)
                x = m.get(i);
        return x;
    }
    
    public int tot(){
        LinkedList<Integer> x = new LinkedList<>();
        x.add(0);
        for(Card c:l){
            {
                switch(c.getNum()){
                
                    case "A":
                        int t = x.size();
                        for(int i=0;i<t;i++){
                            x.add(x.get(i)+11);
                            x.set(i, x.get(i)+1);
                        }
                        break;
                        
                    case "J":case"Q":case"K":
                        for(int i=0;i<x.size();i++)
                            x.set(i, x.get(i)+10);
                        break;
                        
                    default:
                        for(int i=0;i<x.size();i++)
                            x.set(i, x.get(i)+Integer.parseInt(c.getNum()));
                }
            }
        }
        try{
            return highNutBust(x);
        }
        catch(BustException e){
            return 0;
        }
    }
    
    public boolean bust(){
        return tot()==0;
    }
    
    public int size(){
        //System.out.println(l.size());
        return l.size();
    }
    
    public boolean isSplit(){
        return l.get(0).getNum().equals(l.get(1).getNum()) && size() == 2;
    }
    
    public Status vs(Hand2 d){
        int x = Integer.compare(tot(), d.tot());
        Status o;
        switch(x){
            case(-1):
                o = Status.LOSE;
                break;
            case(0):
                o = Status.BE;
                break;
            default:
                o = Status.WIN;
        }
        return o;
    }
}

