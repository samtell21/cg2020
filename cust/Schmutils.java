package cust;
import card_game.bj.Hand2;
import java.util.LinkedList;

public class Schmutils{
    
    public static String selectString(LinkedList<Hand2> l, int i){
        String o = "[";
        o+= i==0?
                "*"+l.get(0)+"*":
                    l.get(0);
        for(int j=1;j<l.size();j++){
            
            o+= i==j?
                    ", *"+l.get(j)+"*":
                        ", "+ l.get(j);
        }
        return o+"]";
    }
    
}
