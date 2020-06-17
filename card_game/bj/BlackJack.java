
package card_game.bj;

import card_game.general.*;
import java.util.*;
import java.io.*;
import cust.*;

enum Status {WIN, LOSE, BE}
enum Opts   {Ok, Hit, Stay, Double_Down, Split, Next, Exit, Yes, No}

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
    
    private final Object[][] OPTIONS =
        {
                {Opts.Ok, Opts.Exit},                               //0: OK,Exit
                {Opts.Yes,Opts.No},                                 //1: Yes,No
                {Opts.Next},                                        //2: Next
                {Opts.Hit,Opts.Stay},                               //3: Hit,Stay
                {Opts.Hit,Opts.Stay,Opts.Double_Down},              //4: Hit,Stay,Double Down
                {Opts.Hit,Opts.Stay,Opts.Double_Down,Opts.Split},   //5: Hit,Stay,Double Down, Split
                {Opts.Stay},                                        //6: Stay
                {Opts.Ok}                                           //7: OK
        };
            
        
    //TODO deck num exception (ctrl-f when refactoring)
    private void init(int n) throws Exception{
    
        j = new Jop();
        hands = new LinkedList<>();
        deck = new Deck(n);
        m = 100;
        //TODO create file if it doesn't exist...
        try{
            br = new BufferedReader(new FileReader(MONEYFILE));
            m = Integer.parseInt(br.readLine());
            br.close();
        }
        catch(IOException e){
            System.out.println("File not Found, money set to 100");
            save();
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
    
    
    
    private void playHands() throws OverdrawnException, SplitException{
        Opts o;
        for(int i = 0; i<hands.size(); i++){
            do{
                o = (Opts) j.capture(output(i), null, optionsOn(i));
                Hand2 h = hands.get(i).doThis(o, deck, this);
                if(o==Opts.Split) hands.add(h);
            }while(o!=Opts.Stay);
        }
    }

    
    
    
    
    
    
    
    private void save(){
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
    private void initDeal() throws WTF{
        initDeal(1);
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
                b *= 0;
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
    
    protected void bet(int i, int n){
        hands.get(i).bet(n);
        m-=n;
    }
    protected void bet(Hand2 h, int n){
        h.bet(n);
        m-=n;
    }
    
    
    
    private String output(int i){
        String a = "Bet";
        String b = "Hand";
        String c = (hands.size()==1) ? "" : "s";
        a+= c+": ";
        b+= c+": ";
        return "Money: "+m+"\n"+a+bets()+"\n\nPlayer "+b+"\n"+Schmutils.selectString(hands,i)+"\n\nDealer Hand: "+dealer.dealerString()+"\n";
    }
    
    public String output(){
        return output(0);
    }
    
   
    
    
    
    public void test() throws WTF, OverdrawnException, SplitException{
        initDeal();
        bet(0, 10);
        playHands();
        System.out.println(dealer.toString()+"\n"+statusHand(0));
        
        
    }
    
}


















