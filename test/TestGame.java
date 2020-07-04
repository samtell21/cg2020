/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;
import card_game.general.*;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

/**
 *
 * @author samt
 */

public class TestGame extends Game{
    
    public enum opts {Option1, Option2, Option3, Option4}
    
    private final JButton[] buttons;
    
    public TestGame(){
        super("Test Game", "test.txt", 100);
        String[] x = new String[]{"button 1", "button 2", "button 3","button 4", "button 5","button 6", "button 7", "hi","there","these","are","buttons","exit"};
        var j = new JButton[x.length];
        for(int i = 0; i< j.length; i++){
            j[i] = new JButton(x[i]);
            j[i].addActionListener(this);
            j[i].setActionCommand(x[i]);
        }
        buttons = j;
    }
    
    @Override
    public void playGame(){
        
    }
    
    @Override
    public JButton[] buttons(){
        return buttons;
    }
    
    @Override
    public void actionPerformed(ActionEvent a){
        var b = (JButton) a.getSource();
        b.setEnabled(false);
        System.out.println(a.getActionCommand());
    }
    
    public static void main(String[] a) {
        
        card_game.general.GameUI.laf();
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            var x = new GameUI(new TestGame());
            x.pack();
            x.setLocationRelativeTo(null);
            x.setVisible(true);
        });
        
    }
    
}
