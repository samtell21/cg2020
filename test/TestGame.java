/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import card_game.general.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author samt
 */

public class TestGame extends Game{
    
    
    private String out;
    private final Box box;
    private final JButton[] buttons;
    
    public TestGame() {
        super("Test Game", "test.txt", 100);
        
        box = Box.createVerticalBox();
        
        String[] x = new String[]{"button 1", "button 2", "button 3","button 4", "button 5","button 6", "button 7", "hi","there","these","are","buttons","exit", "debug"};
        var j = new JButton[x.length];
        for(int i = 0; i< j.length; i++){
            j[i] = new JButton(x[i]);
            j[i].addActionListener(this);
            j[i].setActionCommand(x[i]);
        }
        buttons = j;
        out = "this is a test. \n\n\n I am running tests...\n\n\nbuttons: ";
    }
    

    
    @Override
    public void playGame(){
        
    }
    
    @Override
    public JComponent output(){
        box.add(new JLabel(out));
        return box;
    }
    
    @Override
    public JButton[] buttons(){
        return buttons;
    }
    
    @Override
    public void elseActionPerformed(ActionEvent a){
        
        if(a.getActionCommand().equals("debug")){
            debug();
            return;
        }
        
        var s = a.getSource();
        try{
            var b = (JButton) s;       
            //b.setEnabled(false);
        } catch(ClassCastException e){}
        out = a.getActionCommand() +"\n";
        ui.updateOutput();
    }
    
    public static void main(String[] a) {
        
        card_game.general.GameUI.laf();
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            var x = new GameUI(new TestGame());
            x.viz();
        });
        
    }
    
   
    
    @Override
    public void deal(Account a){
        System.out.println(a.toString());
    }
    
    @Override
    public void debug(){
        dealAll();
    }
    
}


