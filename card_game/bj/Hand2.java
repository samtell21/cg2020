package card_game.bj;

import card_game.general.*;
import java.util.LinkedList;

public class Hand2 extends Hand{
    
    private boolean dd = false;
    public boolean getDD(){
        return dd;
    }
    
    //TODO rework all of these to use a common init
    //      public Hand2(Card[] l, Deck d) and public Hand2(Card[] l)
    
    public Hand2(Account a, Deck d) throws OverdrawnException{
        super(a, d);
        bet = 0;
        hit(deck); hit(deck);
    }
    private Hand2 (Card c, Hand2 h) throws OverdrawnException{
        super(h.player, h.deck);
        bet=0;
        l.add(c);
        hit(deck);
    }
 
    
    @Override
    public final void hit(Deck d) throws OverdrawnException{
        l.add(d.drawRand());
    }

    public Hand2 split(Deck d) throws OverdrawnException, SplitException, FundsException{
        if(!isSplit())
            throw new SplitException("Hand not splittable");
        hit(d);
        Hand2 h = new Hand2(l.remove(1), this);
        h.betEx(bet);
        return h;
    }
    
    
    
    public void doubledown(Deck deck) throws OverdrawnException, FundsException, DDException{
        if(size()!=2) throw new DDException("Cannot double down hands unless size==0");
        betEx(bet);
        dd= true;
        hit(deck);
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

