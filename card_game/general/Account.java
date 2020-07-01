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
public class Account {
    private final String name;
    public String getName(){
        return name;
    }
    private int money;
    public int getMoney(){
        return money;
    }
    public Account(String n, int m){
        name = n;
        money = m;
    }
    
    public boolean bet(int n){
        if(n > money) return false;
        money-=n;
        return true;
    }
}
