
package card_game.bj;

import card_game.general.*;
import java.util.*;
import java.io.*;
import cust.Jop;

enum Status {WIN, LOSE, BE}

public class BlackJack{
    public static void main(String[] a) throws Exception{
        BlackJack bj = new BlackJack();
        bj.test();
    }
    
    
    
    Jop j;
    private int m;
    public int getM(){
        return m;
    }
    private LinkedList<Hand2> hands;
    private Hand2 dealer;
    private Deck deck;
    
    BufferedReader br;
    BufferedWriter bw;
    
    final int MAXHANDS = 5;
    final String MONEYFILE = ".money";
    
    private final Object[] OPS = {"OK", "Hit","Stay","Double Down","Split", "Next","Exit","Yes","No"};
    private Object[][] OPTIONS =
        {
                {OPS[0],OPS[6]},                //0: OK,Exit
                {OPS[7],OPS[8]},                //1: Yes,No
                {OPS[5]},                       //2: Next
                {OPS[1],OPS[2]},                //3: Hit,Stay
                {OPS[1],OPS[2],OPS[3]},         //4: Hit,Stay,Double Down
                {OPS[1],OPS[2],OPS[3],OPS[4]},  //5: Hit,Stay,Double Down, Split
                {OPS[2]},                       //6: Stay
                {OPS[0]}                        //7: OK
        };
            
        
    //TODO deck num exception (ctrl-f when refactoring)
    private void init(int n) throws Exception{
    
        j = new Jop();
        hands = new LinkedList<>();
        deck = new Deck(n);
        
        //TODO create file if it doesn't exist...
        try{
            br = new BufferedReader(new FileReader(MONEYFILE));
            m = Integer.parseInt(br.readLine());
            br.close();
        }
        catch(IOException e){
            System.out.println("File not Found");
        }
        catch(NumberFormatException er){
            System.out.println("Number Format Exception");
        }
        
    }
    public BlackJack(int n) throws Exception{
        init(n);
    }
    public BlackJack() throws Exception{
        init(1);
    }
    
    
    public void playRound(){
                
    }
    
    
    
    
    
    
    
    
    
    
    
    private void save(){
        //TODO better exception handling
        try{
            bw = new BufferedWriter(new FileWriter(MONEYFILE));
            bw.write(Integer.toString(m));
            bw.close();
        }
        catch(IOException e){
            System.out.println("File not Found");
        }
    }
    

    private void initDeal(int n) throws WTF{
        try{
            for(int i = 0; i<n; i++){
                hands.add(new Hand2(deck));
            }
            dealer = new Hand2(deck);
        }
        catch(OverdrawnException e){
            throw new WTF("not enough cards in the deck to deal...");
        }
    }
    
    private void betHand(int i, int b){
        hands.get(i).bet(b);
        m -= b;
    }
    
    
    private void resolveHand(int i, Status s){
        Hand2 h = hands.get(i);
        int b = h.getBet();
        switch(s){
            case WIN:
                b *= 2;
                break;
            case LOSE:
                b *= -1;
                break;
        }
        m += b;
    }
    private void resolveHand(int i){
        resolveHand(i, statusHand(i));
    }
    private Status statusHand(int i){
        return hands.get(i).vs(dealer);
    }
    
    private Object[] optionsOn(int i){
        Hand2 h = hands.get(i);
        if(h.isSplit()) return OPTIONS[5];
        if(h.size()==2) return OPTIONS[4];
        if(h.bust() || h.tot()==21) return OPTIONS[6];
        return OPTIONS[3];
    }
    
    
    
    public void test() throws WTF, OverdrawnException{
        initDeal(1);
        j.capturedMessageDialog(hands.get(0).toString(), null, optionsOn(0));
        hands.get(0).hit(deck);
        j.capturedMessageDialog(hands.get(0).toString(), null, optionsOn(0));
        hands.get(0).hit(deck);
        j.capturedMessageDialog(hands.get(0).toString(), null, optionsOn(0));
        hands.get(0).hit(deck);
        j.capturedMessageDialog(hands.get(0).toString(), null, optionsOn(0));
        hands.get(0).hit(deck);
        
    }
    
}


















