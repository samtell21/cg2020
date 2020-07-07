
package card_game.bj;

import card_game.general.*;
import java.util.*;
import java.io.*;
import cust.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

enum Status {WIN, LOSE, BE}


public class BlackJack extends Game{

    
    private Hand2 dealer;
    private Deck deck;
    
    BufferedReader br;
    BufferedWriter bw;
    
    final int MAXHANDS = 5;

    

            
        
    //TODO deck num exception (ctrl-f when refactoring)
    public BlackJack(){
        try{
            var p = new JSONParser();
            br = new BufferedReader(new FileReader(MONEYFILE));
            accountsString = (JSONObject) p.parse(br);
            br.close();
        }
        catch(IOException | ParseException e){
            System.out.println(e.getMessage()+"\n\nError reading accounts");
            accountsString = new JSONObject();
        }
        
    }
    
    
    public void playGame(){
        try{
            if(splash()==Opts.Exit) return;
            deck = new Deck(deckNum());
            timeToPlay();
        }catch(Cancel c){
            return;
        }catch(DeckNumException d){
            throw new WTF("deck created with negative number");
        }

        do{
            
            while(true){
                try{
                    hands = new LinkedList<>();
                    initDeal(manyHands());
                    try{
                        while(true){
                            try{
                                betting();
                                break;
                            }catch(FundsException f){
                                Jop.message(f.getMessage(), null, OPTIONS[7]);
                            }
                        }
                        break;
                    }catch(Cancel c){}
                }catch(Cancel c){
                    return;
                }
            }
            try{
                while(true){
                    try{
                        playHands();
                        break;
                    }catch(FundsException f){
                        Jop.message(f.getMessage(), null, OPTIONS[7]);
                    }catch(Cancel c){
                        if(abandon()) return;
                    }
                }
                while(true){
                    try{
                        dealerBet();
                        break;
                    }catch(Cancel c){
                        if(abandon()) return;
                    }
                }
            }catch(OverdrawnException o){
                overDrawn(o.getMessage());
                break;
            }
            finalFrame();

        }while(recap() != Opts.Exit);
    }

    

    
    private Boolean abandon(){
        try{
            Opts o = (Opts) Jop.capture("Are you sure you want to abandon this hand?  Your wager will not be returned.", null, OPTIONS[1]);
            if(o==Opts.Yes) save();
            return o==Opts.Yes;
            
        }
        catch(Cancel c){return false;}
    }
    
    private void overDrawn(String s){
        Jop.message("Deck is overdrawn: "+s+"\n\nYour wager will be returned",null,OPTIONS[7]);
    }
    private void playHands() throws OverdrawnException, Cancel, WTF, FundsException{
        Opts o;
        for(int i = 0; i<hands.size(); i++){
            do{
                o = (Opts) Jop.capture(output(i), null, optionsOn(i));
                try{
                    Hand2 h = hands.get(i).doThis(o, deck, this);
                    if(o==Opts.Split) hands.add(h);
                }catch(SplitException s){
                    throw new WTF(s.getMessage());
                }
                
            }while(o!=Opts.Stay);
        }
    }

    private void dealerBet() throws OverdrawnException, Cancel{
        do{
            Jop.capture(output(), null, OPTIONS[2]);
        }while(dealer.dealerHit(deck));
    }
    
    private void finalFrame(){
        Jop.message(finalOut(), null, OPTIONS[7]);
        hands.forEach((h) -> {
            resolveHand(h);
        });
        save();
        
    }
    
    private Opts recap() {
        try{
            return (Opts) Jop.capture("Money: "+m, null, OPTIONS[0]);
        }catch(Cancel c){
            return Opts.Ok;
        }
    }
  
    
    
    
    

    
    private void resolveHand(Hand2 h, Status s){
        int b = h.getBet();
        switch(s){
            case WIN:
                if(h.blackJack()) b *= 3;
                else b *= 2;
                
                break;
            case LOSE:
                b = 0;
                break;
        }
        m += b;
    }
    private void resolveHand(Hand2 h){
        resolveHand(h, statusHand(h));
    }
    
    private Status statusHand(Hand2 h){
        return h.vs(dealer);
    }
    
    
    private Object[] optionsOn(int i){
        Hand2 h = hands.get(i);
        if(h.bust() || h.tot()==21 || h.getDD()) return OPTIONS[6];
        if(h.isSplit()) return OPTIONS[5];
        if(h.size()==2) return OPTIONS[4];
        return OPTIONS[3];
    }
    
    private String bets(){
        String o = Integer.toString(hands.get(0).getBet());
        for(int i = 1;i<hands.size();i++){
            o+=", "+hands.get(i).getBet();
        }
        return o;
    }

    
    private String plural(){
        return (hands.size()==1) ? "" : "s";
    }
    private String outHeader(){
        String a = "Bet";
        a+= plural()+": ";
        return "Money: "+m+"\n"+a+bets()+"\n\n";
    }
    
    private String output(int i, boolean playing){
        String b = "Hand";
        b+= plural()+": ";
        return playing ?
                outHeader()+b+"\n"+Schmutils.selectString(hands,i)+"\n\nDealer Hand: "+dealer.dealerString()+"\n":
                outHeader()+b+"\n"+hands+"\n\nDealer Hand: "+dealer+"\n";
    }
    private String output(int i){
        return output(i, true);
    }
    
    private String output(){
        return output(0, false);
    }
    
    private String finalOut(){
        String o = outHeader();
        o = hands.stream().map((h) -> 
                h + " " + statusString(statusHand(h))+"\n"
        ).reduce(o, String::concat);
        return o+"\n\n"+dealer;
    }
    
    private String statusString(Status s){
        switch(s){
            case WIN:   return "Win";
            case LOSE:  return "Lose";
            case BE:    return "Break Even";
            default:    return null;
        }
        
    }
    

    
}


















