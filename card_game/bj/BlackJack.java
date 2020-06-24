
package card_game.bj;

import card_game.general.*;
import java.util.*;
import java.io.*;
import cust.*;

enum Status {WIN, LOSE, BE}
enum Opts   {Ok, Hit, Stay, Double_Down, Split, Next, Exit, Yes, No}

public class BlackJack{

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
    String MONEYFILE = ".money";
    
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
    public BlackJack() throws Exception{
        m = 100;
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
    
    
    public void playGame() throws WTF, OverdrawnException, SplitException, DeckNumException{    
        if(splash()==Opts.Exit) System.exit(0);
        
        deck = new Deck(deckNum());
        timeToPlay();
        
        do{
            hands = new LinkedList<>();
            initDeal(manyHands());
            betting();
            playHands();
            dealerBet();
        }while(finalFrame() != Opts.Exit);
    }
    
    
    
    private void playHands() throws OverdrawnException, SplitException{
        Opts o;
        for(int i = 0; i<hands.size(); i++){
            do{
                o = (Opts) Jop.capture(output(i), null, optionsOn(i));
                Hand2 h = hands.get(i).doThis(o, deck, this);
                if(o==Opts.Split) hands.add(h);
            }while(o!=Opts.Stay);
        }
    }

    private void dealerBet() throws OverdrawnException{
        do{
            Jop.capture(output(), null, OPTIONS[2]);
        }while(dealer.dealerHit(deck));
    }
    
    private Opts finalFrame(){
        Jop.capture(finalOut(), null, OPTIONS[7]);
        hands.forEach((h) -> {
            resolveHand(h);
        });
        save();
        return (Opts) Jop.capture("Money: "+m, MONEYFILE, OPTIONS[0]);
    }
    
    private Opts splash(){
        return (Opts) Jop.capture("Play BlackJack", null, OPTIONS[0]);
    }
    private int deckNum(){
        return (int) Jop.dropDownNums("How many decks would you like to play with?", null, 1, 4, 0);
    }
    private void timeToPlay(){
        Jop.capture("Ok let's get started!", null, OPTIONS[7]);
    }
    private int manyHands(){
        return (int) Jop.dropDownNums("How many hands this round?", null, 1, 5, 0);
    }
    private void betting(){
        for(int i = 0; i<hands.size(); i++){
            bet(i, (int) Jop.dropDownNums("Money: "+m+"\n\nBet for hand" + (i+1)+"\n", null, 0, m, 0));
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
    
    private void resolveHand(Hand2 h, Status s){
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
    private void resolveHand(int i, Status s){
        resolveHand(hands.get(i), s);
    }
    private void resolveHand(Hand2 h){
        resolveHand(h, statusHand(h));
    }
    
    private Status statusHand(Hand2 h){
        return h.vs(dealer);
    }
    private Status statusHand(int i){
        return statusHand(hands.get(i));
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
    
    protected void bet(Hand2 h, int n){
        h.bet(n);
        m-=n;
    }
    private void bet(int i, int n){
        bet(hands.get(i), n);
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
                h + statusHand(h).toString()+"\n"
        ).reduce(o, String::concat);
        return o+"\n\n"+dealer;
    }
    
    
    
    public void test() throws WTF, OverdrawnException, SplitException, Exception{

        System.out.println(manyHands());
        
    }
    
}


















