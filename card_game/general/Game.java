
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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;


public abstract class Game implements ActionListener{

    /**
     *
     */
    public LinkedList<AccountCheckBox> accounts;
    protected JButton[] buttons;
    protected LinkedList<Hand> hands;
    public final int defaultMon;
    private JSONParser p;
    protected String acc;
    public final String title;
    public GameUIInterface ui;
    
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
                var cb = new AccountCheckBox(new Account(name, money));
                cb.addActionListener(this);
                accounts.add(cb);
                
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
    public abstract String output();
    
    
  
    
    protected void save() throws IOException{
        try(var bw = new BufferedWriter(new FileWriter(acc))){
            var j = new JSONObject();
            accounts.forEach(a->{
                j.put(a.account.getName(), a.account.getMoney());
            });
            bw.write(j.toString());
        }
    }
}


class AccountCheckBox extends JCheckBoxMenuItem{
    
    public final Account account;
    
    public AccountCheckBox(Account a){
        super();
        account = a;
        setText(a.toString());
        setActionCommand("account");
    }
}