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
public class GameUInoform extends GameUIAbstract{
    private Dimension buttonSize;
    
    
    public GameUInoform(Game g){
        super(g);
        init();
    }    
    
    
    private void init(){
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(game.title);
        
        var box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        
        
        box.add(outputPane);
        box.add(Box.createVerticalStrut(5));
        
        buttonPanel.setLayout(new GridLayout());
        for(javax.swing.JButton b : game.buttons()){
            buttonPanel.add(b);
            b.addActionListener(this);
        }
        
        box.add(buttonPanel);
        
        getContentPane().add(box);
        pack();
        buttonSize = buttonPanel.getSize();
        buttonPanel.setMaximumSize(buttonSize);
        
        
        
        
    }
    
    public void viz(){
        setSize(buttonSize.width+100, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    

    
    public static void main(String[] a){
        laf();
        java.awt.EventQueue.invokeLater(() -> {
            var g = new TestGame();
            var x = new GameUInoform(g);
            x.viz();
        });
        
    }
    
}
