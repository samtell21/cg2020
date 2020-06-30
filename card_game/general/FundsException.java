/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card_game.general;

/**
 *
 * @author samt
 */
public class FundsException extends Exception{
    private static final long serialVersionUID = 1L;

    public FundsException(String s){
        super(s);
    }
    public FundsException(){
        super();
    }
}
