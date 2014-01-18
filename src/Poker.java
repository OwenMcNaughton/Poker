import java.util.ArrayList;

public class Poker {
	private static boolean jokers;
	private static boolean shuffled;

	public static ArrayList<String> PokerInit1() {
		ArrayList<String> deck = new ArrayList<>();

		Deck deckObj = new Deck();					// Makes deck with or without jokers,
		deck = deckObj.getDeck(!jokers, shuffled); 	// and shuffled or not shuffled.
		
		return deck;
	}
	
	public static String[][] PokerInit2(ArrayList<String> deck, int players) {
		
		Deck deckObj = new Deck();
		String[][] hands = new String[players][5]; 	// Draws a hand for each player.
		hands = deckObj.getHands(deck, players); 
		
		return hands;
	}
	
	public static int[][][] PokerInit3(int players, String[][] hands) {
		int[][][] splitHand = new int[players][2][5];
		Hand handObj = new Hand();
		
		splitHand = handObj.getSplitHand(hands, players);	 // Splits each player's hand.
		
		return splitHand;
	}
	

}
