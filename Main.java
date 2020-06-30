import card_game.bj.BlackJack;
import card_game.general.WTF;

public class Main {
    public static void main(String[] a){
        
        BlackJack bj = new BlackJack();
        try{
            bj.playGame();
        }catch(WTF w){
            System.out.println(w.getMessage());
            for(StackTraceElement e : w.getStackTrace()){
                System.out.println(e);
            }
        }
    }
}
