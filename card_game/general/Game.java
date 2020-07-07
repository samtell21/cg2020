
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
import javax.swing.JComponent;



public abstract class Game implements ActionListener{

    /**
     *
     */
    LinkedList<AccountCheckBox> accounts;
    protected JButton[] buttons;
    protected LinkedList<Hand> hands;
    public final int defaultMon;
    private JSONParser p;
    protected String acc;
    public final String title;
    public GameUIAbstract ui;
    private int decknum;
    protected boolean deckValid;
    protected Deck deck;
    
    public abstract JButton[] buttons();
    
    public Game(String t, String accfile, int defm){
        title = t;
        acc = accfile;
        defaultMon = defm;
        accounts = new LinkedList<>();
        p = new JSONParser();
        setDecknum(4);
        
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
        
        try{
            validate();
        }catch(DeckNumException d){
            //TODO log it in the journal
        }
    }
    
   
    
    private String getClassDir(){
        return "TODO";
    }
    
    public abstract void playGame();
    public abstract JComponent output();
    
    public final void setDecknum(int n){
        decknum=n;
        deckValid = false;
    }
    
    public final void validate() throws DeckNumException{
        if(!deckValid) deck = new Deck(decknum);
    }
  
    
    protected void save() throws IOException{
        try(var bw = new BufferedWriter(new FileWriter(acc))){
            var j = new JSONObject();
            accounts.forEach(a->{
                j.put(a.account.getName(), a.account.getMoney());
            });
            bw.write(j.toString());
        }
    }
    
    public void dealAll(){
        accounts.forEach((a)->{
            if(a.getState()) deal(a.account);
        });
    }
    
    
    
    public abstract void deal(Account a);
    
    public void debug(){
        //System.out.println(decknum);
    }
    
    public abstract void uiInit();
    
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