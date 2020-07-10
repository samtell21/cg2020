/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.general;
import java.awt.GridLayout;
import javax.swing.*;
import java.util.LinkedList;

/**
 *
 * @author samt
 */
public class GameMenuBar extends JMenuBar{
    class AccountCheckBox extends JCheckBoxMenuItem{
    
        public final Account account;

        public AccountCheckBox(Account a){
            super();
            account = a;
            setText(a.toString());
            setActionCommand("account");
        }
        
        
    }
    
    
    private final String[] menuBar = new String[]{"File", "Edit", "Accounts"};
    public static final String PREFERENCES = "prfrncs";
    public static final String ADDACCOUNT = "addacc";
    
    private JMenu accMen;
    public JMenu getAccMen(){
        return accMen;
    }
    
    public final JFrame deckOpts;
    
    public GameMenuBar(LinkedList<Account> acc){
        super();
        
        
        for(int i = 0; i< menuBar.length; i++){
            var b = new JMenu();
            b.setText(menuBar[i]);
            switch(i){
                case 0:
                    var o = new JMenuItem();
                    o.setText("Preferences");
                    o.setActionCommand(PREFERENCES);
                    
                    b.add(o);
                    break;
                case 1:
                    break;
                case 2:
                    accMen = b;
                    
                    var amen = new JMenuItem();
                    amen.setText("Add Account");
                    amen.setActionCommand(ADDACCOUNT);
                    
                    acc.forEach((a) -> {
                        addAccount(a);
                    });
                    
                    
                    b.add(amen);
                    break;
                default:
            }
            add(b);
        }
        
        deckOpts = new JFrame();
        
        var dc = deckOpts.getContentPane();
        dc.setLayout(new GridLayout(0,1));
        
        var dnum = new JPanel();
        dnum.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        dnum.add(new JLabel("Number of Decks:"));
        dc.add(dnum);
        //TODO
        
       
    }
    
    public void addAccount(Account a){
        var cb = new AccountCheckBox(a);
        cb.setSelected(true);
        accMen.add(cb);
        validate();
    }
    
    public void accountValidate(){
        for(var a : accMen.getMenuComponents()){
            try{
                var acb = (AccountCheckBox) a;
                acb.setText(acb.account.toString());
            }catch(ClassCastException e){}
        }
    }
    
    
}
