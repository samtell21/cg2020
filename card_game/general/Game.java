
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
import cust.*;



public abstract class Game {
    LinkedList<Account> accounts;
    LinkedList<Hand> hands;
    int defaultMon;
    JSONParser p;
    String acc;
    
    public Game(String accfile, int defm) throws Cancel, FileException{
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
        catch(IOException e){
            System.out.println("Accounts file could not be opened");
        }
        catch(ParseException | ClassCastException e){
            var conf = Jop.capture(e.getMessage()+"\n\nError reading accounts in "+acc+"\nis this the right file??", null, new Object[]{"Yes", "No"}).equals("Yes");
            if(conf){
                accounts = new LinkedList<>();
                newAccount();
            }
            else throw new FileException("Bad accounts file");
        }
    }
    
    public abstract void playGame();
    
    protected final void newAccount() throws Cancel{
        String n = Jop.input("Enter your name");
        accounts.add(new Account(n, defaultMon));
        Jop.show("Name: "+n+"\nMoney: "+defaultMon);
    }
    
    protected Account chooseAccount(String s) throws Cancel{
        return (Account) Jop.dropDown(s, null, accounts.toArray(), 0);
    }
    
    protected Boolean abandon() throws IOException{
        try{
            Boolean o = Jop.capture("Are you sure you want to abandon this hand?  Your wager will not be returned.", null, new Object[]{"Yes", "No"}).equals("Yes");
            if(o) save();
            return o;
            
        }
        catch(Cancel c){
            return false;
        }
    }
    
    protected void save() throws IOException{
        try(var bw = new BufferedWriter(new FileWriter(acc))){
            var j = new JSONObject();
            accounts.forEach(a->{
                j.put(a.getName(), a.getMoney());
            });
            bw.write(j.toString());
        }
    }
    
    private Account chooseAccount() throws Cancel{
        //drop down with a new account button
        //learn more about joptionpane
    }
    
    
}
