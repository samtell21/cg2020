
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
import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;



public abstract class Game implements ActionListener{

    /**
     *
     */
    public final int defaultMon;
    private JSONParser p;
    protected String acc;
    public final String title;
    public GameUIAbstract ui;
    private int decknum;
    protected boolean deckValid;
    protected Deck deck;
    public GameMenuBar menu;
    
    public abstract JButton[] buttons();
    
    public Game(String t, String accfile, int defm){
        title = t;
        acc = accfile;
        defaultMon = defm;
        
        p = new JSONParser();
        setDecknum(4);
        
        var accounts = new LinkedList<Account>();
        try(var br = new BufferedReader(new FileReader(acc))){
            var accountsJSON = (JSONObject) p.parse(br);
            accountsJSON.forEach((n,m) -> {
                var name = (String)n;
                var money = (int)m;
                var a = new Account(name, money);
                accounts.add(a);
            });
        }
        catch(IOException | ParseException | ClassCastException e){
            //TODO
        }
        menu = new GameMenuBar(accounts);
        for(Component com1 : menu.getComponents()){
            var jmen = (JMenu) com1;
            for(Component com2 : jmen.getMenuComponents()){
                var x = (JMenuItem)com2;
                x.addActionListener(this);
            }
                
        }
        
        
        
        try{
            validate();
        }catch(DeckNumException d){
            //TODO log it in the journal
        }
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
            accounts(false).forEach((a) -> {
                j.put(a.getName(), a.getMoney());
            });
            bw.write(j.toString());
        }
    }
    
    public LinkedList<? extends Account> accounts(boolean activeOnly){
        var o = new LinkedList<Account>();
        for(java.awt.Component c : menu.getAccMen().getMenuComponents()){
            try{
                var a = (GameMenuBar.AccountCheckBox) c;
                if(a.getState() || !activeOnly) o.add(a.account);
            } catch(ClassCastException e){}
        }
        return o;
    }
    
    public void dealAll(){
        accounts(true).forEach((a)->{
            deal(a);
        });
    }
    
    
    
    public abstract void deal(Account a);
    
    public void debug(){
        //System.out.println(decknum);
    }
    
    public void uiInit(){
        ui.setJMenuBar(menu);
    }
    
    
    @Override
    public final void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "exit": case "Exit" : case "EXIT" :
                ui.dispose();
                System.exit(0);
            case GameMenuBar.PREFERENCES:
                menu.deckOpts.setLocationRelativeTo(ui);
                menu.deckOpts.pack();
                menu.deckOpts.setVisible(true);
                
                break;
            case GameMenuBar.ADDACCOUNT:
                String n = JOptionPane.showInputDialog(ui, "Enter Your Name");
                if(n==null || n.equals("")) break;
                menu.addAccount(new Account(n, defaultMon));
                
                break;
            default: elseActionPerformed(e);
        }
    }
    
    protected abstract void elseActionPerformed(ActionEvent e);
    
    protected void updateOutput(){
        ui.updateOutput();
    }
    
    protected void updateOutput(JComponent c){
        ui.updateOutput(c);
    }
    
}


