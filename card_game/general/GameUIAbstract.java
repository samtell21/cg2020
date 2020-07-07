/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.general;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author samt
 */
public abstract class GameUIAbstract extends javax.swing.JFrame implements ActionListener{
    Game game;
    public final javax.swing.JPanel outputPane;
    public final javax.swing.JPanel buttonPanel;
    private JMenuBar menu;
    private JMenu accountMenu;
    private String[] menuBar;
    
    
    
    public final String DECKOPTIONS = "dckopts";
    public final String ADDACCOUNT = "addacc";
    
    
    private JFrame deckOpts;
    
    public GameUIAbstract(Game g){
        super();
        game = g;
        outputPane = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        
        game.ui=this;
        game.uiInit();
        
        menuInit();
    }
    
    
    
    public void updateOutput(){
        outputPane.removeAll();
        outputPane.add(game.output());
        outputPane.validate();
    }
    
    public static void laf(){
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
        */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "exit": case "Exit" : case "EXIT" :
                dispose();
                System.exit(0);
            case DECKOPTIONS:
                deckOpts.setLocationRelativeTo(this);
                deckOpts.pack();
                deckOpts.setVisible(true);
                
                break;
            case ADDACCOUNT:
                String n = JOptionPane.showInputDialog(this, "Enter Your Name");
                if(n==null || n.equals("")) break;
                var cb = new AccountCheckBox(new Account(n, game.defaultMon));
                cb.setSelected(true);
                accountMenu.add(cb);
                game.accounts.add(cb);
                
                break;
            default: 
        }
        
        
    }
    
    private void menuInit(){
        menuBar = new String[]{"File", "Edit", "Deck Options", "Accounts"};
        
        menu = new JMenuBar();
        
        
        for(int i = 0; i< menuBar.length; i++){
            var b = new JMenu();
            b.setText(menuBar[i]);
            switch(i){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    var o = new JMenuItem();
                    o.setText("Preferences");
                    o.setActionCommand(DECKOPTIONS);
                    o.addActionListener(this);
                    
                    b.add(o);
                    break;
                case 3:
                    var a = new JMenuItem();
                    a.setText("Add Account");
                    a.setActionCommand(ADDACCOUNT);
                    a.addActionListener(this);
                    
                    b.add(a);
                    
                    accountMenu = b;
                    break;
                default:
            }
            menu.add(b);
        }
        
        setJMenuBar(menu);
        
        deckOpts = new JFrame();
        
        var dc = deckOpts.getContentPane();
        dc.setLayout(new GridLayout(0,1));
        
        var dnum = new JPanel();
        dnum.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        dnum.add(new JLabel("Number of Decks:"));
        dc.add(dnum);
    }
    
}
