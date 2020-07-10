/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.general;
import javax.swing.*;

/**
 *
 * @author samt
 */
public abstract class GameUIAbstract extends javax.swing.JFrame{
    Game game;
    public final JPanel outputPane;
    public final JPanel buttonPanel;
    
    
    
    public final String PREFERENCES = "prefs";
    public final String ADDACCOUNT = "addacc";
    
    
    public GameUIAbstract(Game g){
        super();
        game = g;
        outputPane = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        
        game.ui=this;
        game.uiInit();
        
    }
    
    
    
    public void updateOutput(){
        updateOutput(game.output());
    }
    
    public void updateOutput(JComponent c){
        outputPane.removeAll();
        outputPane.add(c);
        validate();
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
            java.util.logging.Logger.getLogger(GameUIAbstract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    
    }
    
    
    
    
    
}
