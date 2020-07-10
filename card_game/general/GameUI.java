/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.general;
import java.awt.*;
import javax.swing.*;

import test.TestGame;

/**
 *
 * @author samt
 */
public class GameUI extends GameUIAbstract{
    
    
    public GameUI(Game g){
        super(g);
        init();
    }    
    
    
    private void init(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(game.title);
        
        var box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        
        var scroll = new JScrollPane(outputPane);
        
        box.add(scroll);
        box.add(Box.createVerticalStrut(5));
        
        buttonPanel.setLayout(new GridLayout());
        for(javax.swing.JButton b : game.buttons()){
            buttonPanel.add(b);
        }
        
        box.add(buttonPanel);
        
        getContentPane().add(box);
        pack();
        buttonPanel.setMaximumSize(buttonPanel.getSize());
        
    }
    
    public void viz(){
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    

    
    public static void main(String[] a){
        laf();
        java.awt.EventQueue.invokeLater(() -> {
            var g = new TestGame();
            var x = new GameUI(g);
            x.viz();
        });
        
    }
    
}
