import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class Deck {
	private String[] suits = { "C", "D", "H", "S" };
	private String[] values = { "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"J", "Q", "K", "A" };

	// Fills an arrayList "deck" with every card type
	public ArrayList<String> getDeck(boolean jokers, boolean shuffled) {
		ArrayList<String> Deck = new ArrayList<String>();

		for (int valuesIter = 0; valuesIter != 13; valuesIter++) {
			for (int suitsIter = 0; suitsIter != 4; suitsIter++) {
				String card = values[valuesIter] + suits[suitsIter];
				Deck.add(card);
			}
		}

		if (!jokers) {
			Deck.add("OB");
			Deck.add("OR");
		}

		if (!shuffled) {
			Collections.shuffle(Deck);
		}

		return Deck;
	}

	// Removes cards from deck and sorts them into 2D array "hands"
	public String[][] getHands(ArrayList<String> deck, int players) {
		String[][] hands = new String[players][5];

		for (int cards = 0; cards != 5; cards++) {
			for (int player = 0; player != players; player++) {
				hands[player][cards] = deck.get(0);
				deck.remove(0);
			}
		}

		return hands;
	}
	
	//Gets 
	public Image[][] getCardImages(int players, int[][][] splitHand)
	{
		Image[][] cardImages = new Image[players][5];
		
		String[][] directories = new String[players][5];
		
		for(int player = 0; player != players; player++) {
			for(int i = 0; i < 5; i++)
			{
				int num = splitHand[player][0][i];
				int suit = splitHand[player][1][i];
			
				String cardNum = Integer.toString(num);
				String cardSuit = Integer.toString(suit);
			
				String name = cardNum + cardSuit;
				String dir = "res\\" + name + ".png";
				directories[player][i] = dir;
			
				File sourceImage = new File(dir);
			
				try {
					cardImages[player][i] = ImageIO.read(sourceImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(int player = 0; player != players; player++) {
			for(int i = 0; i != 5; i++) {
				String checker = directories[player][i];
				directories[player][i] = "0";
				
				for(int j = 0; j != 5; j++) {
					String checker2 = directories[player][j];
					
					if(checker.equals(checker2)) {
						getCardImages(players, splitHand);
					}
				}
			}
		}
		
		return cardImages;
	}

	
}
