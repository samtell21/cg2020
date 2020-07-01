package card_game.bj;

import card_game.general.*;
import java.util.LinkedList;

public class Hand2{
    private final LinkedList<Card> l = new LinkedList<>();
    private int b;
    public int getBet(){
        return b;
    }
    private boolean dd = false;
    public boolean getDD(){
        return dd;
    }
    public final Account player;
    public final Deck deck;
    
    //TODO rework all of these to use a common init
    //      public Hand2(Card[] l, Deck d) and public Hand2(Card[] l)
    
    public Hand2(Deck d, Account a) throws OverdrawnException{
        player = a;
        deck = d;
        b = 0;
        hit(d); hit(d);
    }
    private Hand2 (Card c, Hand2 h) throws OverdrawnException{
        player = h.player;
        deck = h.deck;
        b=0;
        l.add(c);
        hit(deck);
    }
    
/*
    //TODO deck num exception (ctrl-f when refactoring)
    protected Hand2(Card c, Card d) throws Exception, OverdrawnException{
        b = 0;
        l.add(c);
        l.add(d);
        
        defaultDeck = new Deck();
        if(!(defaultDeck.draw(c) && defaultDeck.draw(d))) throw new OverdrawnException("cannot create... gonna refactor anyway, who cares");
    }

    
    //TODO deck num exception (ctrl-f when refactoring)
    private Hand2() throws Exception{
        defaultDeck = new Deck();
        
        b = 0;
        hit(); hit();
    }
*/
    
    public final void hit(Deck d) throws OverdrawnException{
        l.add(d.drawRand());
    }
/*  
    private void hit() throws OverdrawnException{
        hit(defaultDeck);
    }
*/
    

    public Hand2 split(Deck d) throws OverdrawnException, SplitException, FundsException{
        if(!isSplit())
            throw new SplitException("Hand not splittable");
        hit(d);
        Hand2 h = new Hand2(l.remove(1), this);
        h.bet(b);
        return h;
    }
    

    
    void bet(int n) throws FundsException{
        if(player.bet(n)) b+=n;
        else throw new FundsException("Insufficient Funds");
    }
    
    
    
    public void doubledown(Deck deck) throws OverdrawnException, FundsException, DDException{
        if(size()!=2) throw new DDException("Cannot double down hands unless size==0");
        bet(b);
        dd= true;
        hit(deck);
    }

    
    @Override
    public String toString(){
        return l.toString();
    }
    public String dealerString(){
        return "[XXXX, "+ l.get(1) +"]";
    }
    
    public boolean dealerHit(Deck d) throws OverdrawnException{
        if(tot()<17 && !bust()){
            hit(d);
            return true;
        }
        else{
            return false;
        }
    }

    
    private int highNutBust(LinkedList<Integer> l){
        LinkedList<Integer> m = new LinkedList<>();
        while(!l.isEmpty()){
            int x = l.pop();
            if(x<=21)
                m.add(x);
        }
        if(m.isEmpty())
            return 0;
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
        return highNutBust(x);
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
    
    Status vs(Hand2 d){
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
    

    Hand2 doThis(Opts o, Deck d, BlackJack bj) throws OverdrawnException, SplitException, FundsException, DDException{
        Hand2 r = this;
        switch(o){
            case Hit:           hit(d);          break;
            case Double_Down:   doubledown(d);   break;
            case Split:         r = split(d); 
        }
        return r;
    }
    
    public Boolean blackJack(){
        return tot()==21 && size() == 2;
    }
    
}

