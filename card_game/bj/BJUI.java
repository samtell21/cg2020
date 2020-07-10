/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.bj;
import card_game.general.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.LinkedList;
import java.awt.GridLayout;

/**
 *
 * @author samt
 */
public class BJUI extends Game{
    class HandPanel extends JPanel{
        public LinkedList<? extends Account> accounts;
        JComboBox <Account> dropdown;
        
        public HandPanel(LinkedList<? extends Account> acc){
            super();
            accounts = acc;
            dropdown = new javax.swing.JComboBox<>();
            
            setBorder(BorderFactory.createTitledBorder("Hand Box"));
            dropdown.addItem(null);
            
            accounts.forEach((a) -> {
                dropdown.addItem(a);
            });
            
            dropdown.setSelectedIndex(1);
            
            add(dropdown);
            
            
            setBorder(BorderFactory.createEtchedBorder());
            
        }
    }
    
    protected int state;
    protected JComponent out;
    protected JComponent players;
    protected JComponent dealer;
    
    
    public final String START = "start";
    
    private final String[] buttons = new String[]{
        "debug",
        "exit"
    };
    
    public BJUI(){
        super("Black Jack", ".accounts", 100);
        state = 0;
    }
    
    @Override
    public void elseActionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "debug":
                debug();
                return;
            case START:
                state = 1;
                break;
        }
        
        playGame();
    }
    
    @Override
    public void debug(){
        bet(accounts(true).get(1), 5);
    }
    
    public void bet(Account a, int m){
        a.bet(m);
        menu.accountValidate();
    }
    
    @Override
    public void deal(Account a){
        //TODO
    }

    @Override
    public JButton[] buttons() {
        var o = new JButton[buttons.length];
        for(int i = 0; i<o.length; i++){
           var b = new JButton();
           b.setText(buttons[i]);
           b.setActionCommand(buttons[i]);
           b.addActionListener(this);
           
           o[i] = b;
        }
        return o;
    }
    
    @Override
    public void playGame() {
        switch(state){
            case 0: 
                updateOutput();
                break;
            case 1:
                updateOutput(setUpPlayers());
            
        }
    }
    
    private JComponent setUpPlayers(){
        var accs = accounts(true);
        
        if(accs.isEmpty()){
            if(accounts(false).isEmpty()){
                var na = new JButton();
                na.setActionCommand(GameMenuBar.ADDACCOUNT);
                na.addActionListener(this);
                na.doClick();
                return setUpPlayers();
            }
            else{
                JOptionPane.showMessageDialog(ui, "Please activate an account", null, JOptionPane.ERROR_MESSAGE);
                return output();
            }
        }
        
        menu.addAccount(new Account("test", 100));
        accs = accounts(true);
        
        out = new JPanel();
        
        players = new JPanel();
        players.setLayout(new GridLayout());
        
        for(int i = 0; i<5; i++){
            var x = new HandPanel(accs);
            players.add(x);
        }
        out.add(players);
        
        
        return out;
    }
    
    
    @Override
    public JComponent output() {
        out = new JPanel();

        var b = new JButton();
        var l = new JLabel();

        var img = new ImageIcon(System.getProperty("user.home")+"/cg2020/card_game/bj/resources/bjcrds.jpeg", "Play Blackjack");
        l.setIcon(img);
        l.setBorder(BorderFactory.createLineBorder(Color.black, 5));
        l.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        
        b.setText("Play Blackjack");
        b.setFont(new Font("Bitstream Vera Sans", Font.PLAIN, 40));
        b.setActionCommand(START);
        b.addActionListener(this);
        b.setBounds(l.getWidth()/2 - 150, l.getHeight()/2, 314, 60);
        
        var layered = new JLayeredPane();
        layered.setPreferredSize(l.getSize());
        
        layered.add(l,0);
        layered.add(b,0);
       
        return layered;
    }
    
    public static void main(String[] a) {
        
        card_game.general.GameUI.laf();
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            var bj = new BJUI();
            var x = new GameUI(bj);
            x.viz();
            bj.playGame();
        });
        
    }
}
