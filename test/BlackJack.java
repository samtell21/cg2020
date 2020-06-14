package test;
import card_game.*;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import cust.Jop;


//this is the old versiion of BlackJack and needs a lot of improvement
public class BlackJack 
{
	public static void main(String[] args)
	{
        Jop j = new Jop();
        Object[] dexnums = {1,2,3,4,5};
        int dex = (Integer) dexnums[j.capturedMessageDialog("how many decks????????????????", null, dexnums)];
        System.out.println(dex);
		try{
			BlackJack bj= new BlackJack(dex);
			String a;
			int xx;
			boolean b = false;
			while(true){
				a = b? "Next Hand":"Play BlackJack";
				Object[] o = {a, bj.ops[6]};
				try{
					xx = bj.j.capturedMessageDialog("Money: "+bj.getM(), null, o);
					if(xx<-1||xx>1)
						throw bj.fuckedOps;
					if(xx!=0)
						System.exit(0);
					bj.game();
					b=true;
				}
				catch(Cancel t){
					bj.next(true);
				}
				catch(Exception e){
					a = e.getClass().equals(OverdrawnException.class)? "Error: "+e.getMessage()+"\n(Hand will be abandoned. Wager will be returned.)": e.getMessage();
					bj.next(true);
					JOptionPane.showMessageDialog(null, a);
					if(e.getClass().equals(WTF.class))
						System.exit(0);
				}
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error Creating Deck\n"+e.getMessage());
		}
	}
	
	Jop j = new Jop();
	private int m;
	public int getM(){
		return m;
	}
	private LinkedList<Hand2> hh;
	private Hand2 d;
	private Deck deck;
	int maxHands;
	BufferedReader br;
	BufferedWriter bw;
	String name = "Money.txt";
	
	private final Object[] ops = {"OK", "Hit","Stay","Double Down","Split", "Next","Exit","Yes","No"};
	private Object[][] options =
		{
				{ops[0],ops[6]},				//0: OK,Exit
				{ops[7],ops[8]},				//1: Yes,No
				{ops[5]},						//2: Next
				{ops[1],ops[2]},				//3: Hit,Stay
				{ops[1],ops[2],ops[3]},			//4: Hit,Stay,Double Down
				{ops[1],ops[2],ops[3],ops[4]},	//5: Hit,Stay,Double Down, Split
				{ops[2]},						//6: Stay
				{ops[0]}						//7: OK
		};
	/*
	public BlackJack() throws Exception{
		hh = new LinkedList<>();
		deck = new Deck();
		try{
			br = new BufferedReader(new FileReader(name));
			m = Integer.parseInt(br.readLine());
			br.close();
		}
		catch(IOException e){
			System.out.println("File not Found");
		}
		catch(NumberFormatException er){
			System.out.println("Number Format Exception");
		}
	}
	/**/
	public BlackJack(int n) throws Exception{
		init(n);
	}
	
	public BlackJack() throws Exception{
        init(1);
	}
	/**/
	
	private void init(int n) throws Exception{
        hh = new LinkedList<>();
		deck = new Deck(n);
		try{
			br = new BufferedReader(new FileReader(name));
			m = Integer.parseInt(br.readLine());
			br.close();
		}
		catch(IOException e){
			System.out.println("File not Found");
		}
		catch(NumberFormatException er){
			System.out.println("Number Format Exception");
		}
	}
	
	public void game() throws Exception,Cancel{
		game(5);
	}
	
	WTF fuckedOps = new WTF("Someone fucked with the options...");
	public void game(int q) throws Exception,Cancel
	{
		int xx=0;
		String a;
		if(q<=0||q>5)
			throw new WTF("Nope, maxHands has to be between 1 and 5, inclusive");
		maxHands = q;
		try{
			xx = (int)j.dropDownNums("Money: "+m+"\n\nHow man hands?", null, 0, q, 1);
		}
		catch(Exception e){
			throw new Cancel();
		}
		initialDeal(xx);
		for(int i=0;i<hh.size();i++)
		{
			a = hh.size()==1? "": " for hand "+(i+1);
			String s = "Money: "+m+"\n\nPlace bet"+a;
			try{
				xx = (int)j.dropDownNums(s, null, 0, m, 0);
			}
			catch(Exception e){
				throw new Cancel();
			}
			bet(i,xx);
		}
		int x =-1;
		while(true){
			Hand2 h;
			try{
				h = hh.get(++x);
			}
			catch(Exception e){
				break;
			}
			Object[] use;
			SHABADOO:
			while(true){
				if(h.tot()>=21)
					use = options[6];
				else if(h.size()==2)
					if(h.isSplit())
						use = options[5];
					else
						use = options[4];
				else
					use = options[3];
				do{
					xx = j.capturedMessageDialog(output(false,x), null, use);
					if(xx==-1)
						abandon();
				}while(xx==-1);
				switch((String)use[xx]){
					case "Hit": 
						h.hit(deck);
						break;
					case "Stay":
						break SHABADOO;
					case "Double Down":
						if(h.getBet()>m){
							JOptionPane.showMessageDialog(null, "Insuficient Funds");
							break;
						}
						bet(x, h.getBet());
						h.hit(deck);
						break SHABADOO;
					case "Split":
						if(h.getBet()>m){
							JOptionPane.showMessageDialog(null, "Insuficient Funds");
							break;
						}
						hh.add(x+1,h.split(deck));
						bet(x+1, h.getBet());
						break;
					default: throw fuckedOps;
				}
			}
		}
		do{
			do{
				xx = j.capturedMessageDialog(output(true,0), null, options[2]);
				if(xx!=0&&xx!=-1)
					throw fuckedOps;
				if(xx==-1)
					abandon();
			}while(xx==-1);
		}while(d.dealerHit(deck));
		xx = j.capturedMessageDialog(finalOut(), null, options[7]);
		if(xx!=0&&xx!=-1)
			throw fuckedOps;
		if(xx==-1)
			System.exit(0);
	}
	
	private String finalOut(){
		String o ="";
		int x;
		for(Hand2 h:hh){
			o+=h+": ";
			if(d.bust())
				if(h.bust()){
					o+="Break Even";
					x=2;
				}
				else{
					o+= "Winner";
					x=0;
				}
			else{
				if(h.bust()){
					o+= "Loser";
					x=1;
				}
				else{
					if(h.tot()>d.tot()){
						o+= "Winner";
						x=0;
					}
					else if(h.tot()==d.tot()){
						o+= "Break Even";
						x=2;
					}
					else{
						o+= "Loser";
						x=1;
					}
				}
			}
			switch(x){
				case 0:
					m+=h.getBet()*2;
					if(!(h.size()==2&&h.tot()==21))
						break;
				case 2:
					m+=h.getBet();
			}
			o+="\n\n";
		}
		next(false);
		o+="Dealer Hand: "+d+"\n\n";
		return "Money: "+m+"\n\n"+o;
	}
	
	public void initialDeal(int n) throws Exception{
		if(n==0)
			throw new Exception("You have chosen to sit this round out.");
		for(int i=0;i<n;i++){
			Hand2 h = new Hand2(deck);
			hh.add(h);
		}
		d = new Hand2(deck);
	}
	
	public String output(Boolean e, int i){
		String a,b;
		if(hh.size()==1){
			a = "Bet: ";
			b = "Hand: ";
		}
		else{
			a = "Bets: ";
			b = "Hands: ";
		}
		return e?
				"Money: "+m+"\n"+a+bets()+"\n\nPlayer "+b+"\n"+hh+"\n\nDealer Hand: "+d+"\n":
				"Money: "+m+"\n"+a+bets()+"\n\nPlayer "+b+"\n"+selectString(hh,i)+"\n\nDealer Hand: "+d.dealerString()+"\n";
	}
	
	String bets(){
		String o = Integer.toString(hh.get(0).getBet());
		for(int i = 1;i<hh.size();i++){
			o+=", "+hh.get(i).getBet();
		}
		return o;
	}
	
	String selectString(LinkedList<Hand2> l, int i){
		String o = "[";
		o+= i==0?
				"*"+l.get(0)+"*":
					l.get(0);
		for(int j=1;j<l.size();j++){
			
			o+= i==j?
					", *"+l.get(j)+"*":
						", "+ l.get(j);
		}
		return o+"]";
	}
	
	private void abandon(){
		int sure = j.capturedMessageDialog("Are sure you want to leave the table in the middle of a round?\n(Your wager will not be returned)",null,options[1]);
		if(sure == 0){
			save();
			System.exit(0);
		}
	}
	
	public void next(boolean r)
	{
		if(r)
			for(Hand2 h:hh)
				m+=h.getBet();
		hh.clear();
		deck.burn();
		save();
	}
	
	private void save(){
		try{
			bw = new BufferedWriter(new FileWriter(name));
			bw.write(Integer.toString(m));
			bw.close();
		}
		catch(IOException e){
			System.out.println("card_games.blackjack line 312");
		}
	}
	
	private void bet(int i, int n){
		hh.get(i).bet(n);
		m-=n;
	}
}
