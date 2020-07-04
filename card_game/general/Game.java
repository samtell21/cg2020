
package card_game.general;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.event.ActionListener;
import javax.swing.JButton;


public abstract class Game implements ActionListener{
    protected LinkedList<Account> accounts;
    protected LinkedList<Hand> hands;
    protected int defaultMon;
    private JSONParser p;
    protected String acc;
    public final String title;
    
    public abstract JButton[] buttons();
    
    public Game(String t, String accfile, int defm){
        title = t;
        acc = accfile;
        defaultMon = defm;
        accounts = new LinkedList<>();
        p = new JSONParser();
        
        
        try(var br = new BufferedReader(new FileReader(acc))){
            var accountsJSON = (JSONObject) p.parse(br);
            accountsJSON.forEach((n,m) -> {
                var name = (String)n;
                var money = (int)m;
                accounts.add(new Account(name, money));
                
            });
        }
        catch(IOException | ParseException | ClassCastException e){
            //TODO
        }
    }
    
    private String getClassDir(){
        return "TODO";
    }
    
    public abstract void playGame();
    
    
  
    
    protected void save() throws IOException{
        try(var bw = new BufferedWriter(new FileWriter(acc))){
            var j = new JSONObject();
            accounts.forEach(a->{
                j.put(a.getName(), a.getMoney());
            });
            bw.write(j.toString());
        }
    }
    
    
    
    
}
