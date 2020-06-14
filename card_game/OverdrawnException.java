package card_game;

public class OverdrawnException extends Exception{
	private static final long serialVersionUID = 1L;

	public OverdrawnException(String s){
		super(s);
	}
}
