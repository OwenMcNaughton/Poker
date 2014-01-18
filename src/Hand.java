import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hand {
	
	String fold = "folds";
	String call = "calls";
	String bet = "bets";

	// Splits each player's hand into two int arrays,
	// splitHand[0] for values and splitHand[1] for suits.
	public int[][][] getSplitHand(String[][] hands, int players) {
		int[][][] splitHand = new int[players][2][5];

		for(int player = 0; player != players; player++) {
			for (int count = 0; count != 5; count++) {

				String card = hands[player][count];
				String[] split = card.split("");

				int value = 0;

				try {
					Scanner scan = new Scanner(split[1]);
					value = scan.nextInt();
					scan.close();
				} catch (Exception inputMismatchException) {
					if (split[1].equals("J")) {
					value = 11;
					}
					if (split[1].equals("Q")) {
					value = 12;
					}
					if (split[1].equals("K")) {
					value = 13;
					}
					if (split[1].equals("A")) {
					value = 14;
					}
				}

				int suitPicker = 2;
				int suit = 0;

				if (value == 1) {
					value = 10;
					suitPicker++;
				}
				
				if (split[suitPicker].equals("C")) {
					suit = 1;
				}
				if (split[suitPicker].equals("D")) {
					suit = 2;
				}
				if (split[suitPicker].equals("H")) {
					suit = 3;
				}
				if (split[suitPicker].equals("S")) {
					suit = 4;
				}

				splitHand[player][0][count] = value;
				splitHand[player][1][count] = suit;

			}
		}
		return splitHand;
	}

	public boolean getHasFlush(int[][] splitHand) {
		boolean hasFlush = false;
		int count = 0;

		if (splitHand[1][count++] == splitHand[1][count]) {
			if (splitHand[1][count++] == splitHand[1][count]) {
				if (splitHand[1][count++] == splitHand[1][count]) {
					if (splitHand[1][count++] == splitHand[1][count]) {
						hasFlush = true;
					}
				}
			}
		}

		return hasFlush;
	}
	
	public boolean getAlmostFlush(int[][] splitHand) {
		boolean almostFlush = false;
		int c = 0;
		int d = 0;
		int h = 0;
		int s = 0;
		
		for(int i = 0; i != 5; i++) {
			int suit = splitHand[1][i];
			switch(suit) {
				case 1: c++; break;
				case 2: d++; break;
				case 3: h++; break;
				case 4: s++; break;
			}
		}
		
		if(c==4 || d==4 || h==4 || s==4) {
			almostFlush = true;
		}
		
		return almostFlush;
	}
	
	public boolean getHasStraight(int[][] splitHand) {
		boolean hasStraight = false;
		int count = 0;
		
		int[] numbers = new int[5];
		
		System.arraycopy(splitHand[0], 0, numbers, 0, splitHand[0].length);
		
		Arrays.sort(numbers);
		
		if(numbers[count++] == ((numbers[count]) - 1)) {
			if(numbers[count++] == ((numbers[count]) - 1)) {
				if(numbers[count++] == ((numbers[count]) - 1)) {
					if(numbers[count++] == ((numbers[count]) - 1)) {
						hasStraight = true;
					}
				}
			}
		}
		
		return hasStraight;
	}

	public boolean getAlmostStraight(int[][] splitHand) {
		boolean almostStraight = false;
		int count = 0;
		
		int[] numbers = new int[5];
		
		System.arraycopy(splitHand[0], 0, numbers, 0, splitHand[0].length);
		
		Arrays.sort(numbers);
		
		if(numbers[count++] == ((numbers[count]) - 1)) {
			if(numbers[count++] == ((numbers[count]) - 1)) {
				if(numbers[count++] == ((numbers[count]) - 1)) {
					almostStraight = true;
				}
			}
		}
		
		count = 1;
		
		if(numbers[count++] == ((numbers[count]) - 1)) {
			if(numbers[count++] == ((numbers[count]) - 1)) {
				if(numbers[count++] == ((numbers[count]) - 1)) {
					almostStraight = true;
				}
			}
		}
		
		return almostStraight;
	}
	
	public int[] getMatches(int[][] splitHand) {
		int[] matches = new int[5];
		//element 0 is for type of match: 1 is pair, 2 is 2pair, 3 is triple, 4 is house, 5 is quad
		//element 1 is for value of match
		//element 2 is for either value of lesser pair (2pair), or value of highest non pair card (pair)
		//element 3 is for either value of remaining card (2pair), or value of middle non pair card (pair)
		//element 4 is for lowest value non pair card (pair)
		
		int[] values = new int[5];
		
		for(int i = 0; i != 5; i++) {
			values[i] = splitHand[0][i];
		}
		
		Arrays.sort(values);
		
		label: {
			if(values[0] == values[1]) {
				if(values[1] == values[2]) {
					if(values[2] == values[3]) {
						matches[0] = 5;
						matches[1] = values[3];		//Found a four of a kind
						break label;
					}
					matches[0] = 3;
					matches[1] = values[2];			//Found a three of a kind
					if(values[3] == values[4]) {
						matches[0] = 4;				//Found a full house
						break label;
					}
					break label;
				}
				matches[0] = 1;
				matches[1] = values[1];				//Found a pair
				if(values[2] == values[3]) {
					if(values[3] == values[4]) {
						matches[0] = 4;				//Found a full house
						matches[1] = values[4];
						break label;
					}
					matches[0] = 2;					//Found a 2pair
					matches[1] = values[2];
					matches[2] = values[1];
					matches[3] = values[4];
					break label;
				}
				if(values[3] == values[4]) {
					matches[0] = 2;					//Found a 2pair
					matches[1] = values[3];		
					matches[2] = values[1];
					matches[3] = values[2];
					break label;
				}
				matches[2] = values[4];
				matches[3] = values[3];
				matches[4] = values[2];
				break label;
			}
			if(values[1] == values[2]) {
				if(values[2] == values[3]) {
					if(values[3] == values[4]) {
						matches[0] = 5;
						matches[1] = values[4];		//Found a four of a kind
						break label;
					}
					matches[0] = 3;
					matches[1] = values[3];			//Found a three of a kind
					break label;
				}
				matches[0] = 1;
				matches[1] = values[2];
				if(values[3] == values[4]) {
					matches[0] = 2;					//Found a 2pair
					matches[1] = values[3];
					matches[2] = values[2];
					matches[3] = values[0];
					break label;
				}
				matches[2] = values[4];
				matches[3] = values[3];
				matches[4] = values[0];
				break label;
			}
			if(values[2] == values[3]) {
				if(values[3] == values[4]) {
					matches[0] = 3;
					matches[1] = values[4];			//Found a three of a kind
					break label;
				}
				matches[0] = 1;
				matches[1] = values[2];				//Found a pair
				matches[2] = values[4];
				matches[3] = values[1];
				matches[4] = values[0];
				break label;
			}
			if(values[3] == values[4]) {
				matches[0] = 1;
				matches[1] = values[3];				//Found a pair
				matches[2] = values[2];
				matches[3] = values[1];
				matches[4] = values[0];
			}
		}
		
		return matches;
	}

	public int getHigh(int[][] splitHand) {
		
		int[] numbers = new int[5];
		
		System.arraycopy(splitHand[0], 0, numbers, 0, splitHand[0].length);
		
		Arrays.sort(numbers);
		
		int high = numbers[4];
		
		return high;
	}
	
	public int getConfidence(int player, int[][][] split) {
		int[][] splitHand = new int[2][5];
		
		for(int i = 0; i != 5; i++) {
			splitHand[0][i] = split[player][0][i];
			splitHand[1][i] = split[player][1][i];
		}
	
		boolean hasStraight = getHasStraight(splitHand);
		boolean hasFlush = getHasFlush(splitHand);
		if(hasStraight && hasFlush) {
			return 50;
		}
		if(hasFlush) {
			return 35;
		}
		if(hasStraight) {
			return 30;
		}
		
		boolean almostFlush = getAlmostFlush(splitHand);
		if(almostFlush) {
			return 28;
		}
		boolean almostStraight = getAlmostStraight(splitHand);
		if(almostStraight) {
			return 25;
		}
		
		int[] matches = getMatches(splitHand);
		if(matches[0] ==  5) {
			return 45;
		}
		
		if(matches[0] ==  4) {
			return 40;
		}
		if(matches[0] ==  3) {
			return 20;
		}
		
		
		if(matches[0] == 2 && matches[1] > 10) {
			return 18;
		}
		if(matches[0] == 2 && matches[1] > 5) {
			return 15;
		}
		if(matches[0] == 2) {
			return 12;
		}
		
		if(matches[0] == 1 && matches[1] > 10) {
			return 10;
		}
		if(matches[0] == 1 && matches[1] > 5) {
			return 9;
		}
		if(matches[0] == 1) {
			return 8;
		}
		
		int high = getHigh(splitHand);
		if(high > 12) {
			return 5;
		}
		if(high > 10) {
			return 3;
		}
		if(high > 5) {
			return 1;
		}
		
		return 0;
	}
	
	public int[] getRedraw(int split[][][], int player) {
		int[][] splitHand = split[player];
		
		int[] redraw = new int[5];
		
		boolean hasFlush = getHasFlush(splitHand);
		if(hasFlush) {
			return redraw;
		}
	
		boolean hasStraight = getHasStraight(splitHand);
		if(hasStraight) {
			return redraw;
		}
		
		boolean almostFlush = getAlmostFlush(splitHand);
		if(almostFlush) {
			int i = redrawAlmostFlush(splitHand);
			redraw[i] = 1;
			return redraw;
		}
		
		boolean almostStraight = getAlmostStraight(splitHand);
		if(almostStraight) {
			int i = redrawAlmostStraight(splitHand);
			redraw[i] = 1;
			return redraw;
		}
		
		int[] matches = getMatches(splitHand);
		
		if(matches[0] > 3) {
			return redraw;
		}
		if(matches[0] == 1) {
			redraw = redrawPair(matches, splitHand);
			return redraw;
		}
		if(matches[0] == 2) {
			redraw = redraw2Pair(matches, splitHand);
			return redraw;
		}
		if(matches[0] == 3) {
			redraw = redrawTriple(matches, splitHand);
			return redraw;
		}
		
		if(matches[0] == 0) {
			int high = getHigh(splitHand);
			int high2 = getHigh2(splitHand);
			
			int[] highs = {high, high2};
			
			Random gen = new Random();
			
			int r = gen.nextInt(3);
			int r2 = gen.nextInt(4);
			int total = 3;
			if(r2 == 0) {
				total = r;
			}
			
			int i = 0;
			int j = 0;
			while(i != 5 && j != total) {
				
				if(splitHand[0][i] != highs[0] && splitHand[0][i] != highs[1]) {
					redraw[i] = 1;
					j++;
				}
				
				i++;
			}
			return redraw;
		}
		
		return null;
	}
	
	private int getHigh2(int[][] splitHand) {
		int[] numbers = new int[5];
		
		System.arraycopy(splitHand[0], 0, numbers, 0, splitHand[0].length);
		
		Arrays.sort(numbers);
		
		int high2 = numbers[3];
		
		return high2;
	}

	public int redrawAlmostFlush(int[][] splitHand) {
		int c = 0;
		int d = 0;
		int h = 0;
		int s = 0;
		
		for(int i = 0; i != 5; i++) {
			int suit = splitHand[1][i];
			switch(suit) {
				case 1: c++; break;
				case 2: d++; break;
				case 3: h++; break;
				case 4: s++; break;
			}
		}
		
		int findNotThis = 0;
		
		if(c==4) findNotThis = 1;
		if(d==4) findNotThis = 2;
		if(h==4) findNotThis = 3;
		if(s==4) findNotThis = 4;
		
		int thisOne = 0;
		
		for(int i = 0; i != 5; i++) {
			if(splitHand[1][i] != findNotThis){
				thisOne = i;
			}
			
		}
		
		return thisOne;
	}

	public int redrawAlmostStraight(int[][] splitHand) {
		int count = 0;
		int[] copy = new int[5];
		System.arraycopy(splitHand[0], 0, copy, 0, splitHand[0].length);
		Arrays.sort(copy);
		int thisCard = 0;
		if(copy[count++] == ((copy[count]) - 1)) {
			if(copy[count++] == ((copy[count]) - 1)) {
				if(copy[count++] == ((copy[count]) - 1)) {
					thisCard = copy[4];
				}
			}
		}
		
		count = 1;
		
		if(copy[count++] == ((copy[count]) - 1)) {
			if(copy[count++] == ((copy[count]) - 1)) {
				if(copy[count++] == ((copy[count]) - 1)) {
					thisCard = copy[0];
				}
			}
		}
		
		int returnThis = 0;
		
		for(int i = 0; i != 5; i++) {
			if(splitHand[0][i] == thisCard) {
				returnThis = i;
			}
		}
		
		return returnThis;
	}
	
	public int[] redrawPair(int[] matches, int[][] splitHand) {
		int[] redrawPair = {1, 1, 1, 1, 1};
		
		int[] keep = new int[5];
		
		keep[0] = matches[1];
		
		Random gen = new Random();
		
		int r = gen.nextInt(4);
		
		if(r != 3) {
			if(matches[2] > 10) {
				keep[1] = matches[2];
			}
			if(matches[3] > 10 && keep[1] == 0) {
				keep[2] = matches[3];
			}
		} 
		
		
		for(int i = 0; i != 5; i++) {
			for(int j = 0; j != 5; j++) {
				if(splitHand[0][i] == keep[j]){
					redrawPair[i] = 0;
				}
			}	
		}
		
		return redrawPair;
	}
	
	public int[] redraw2Pair(int[] matches, int[][] splitHand) {
		int[] redraw2Pair = {1, 1, 1, 1, 1};
		
		int[] keep = new int[5];
		
		keep[0] = matches[1];
		keep[1] = matches[2];
		if(matches[3] > 8) {
			keep[2] = matches[3];
		}
		
		for(int i = 0; i != 5; i++) {
			for(int j = 0; j != 5; j++) {
				if(splitHand[0][i] == keep[j]){
					redraw2Pair[i] = 0;
				}
			}	
		}
		
		return redraw2Pair;
	
	}
	
	public int[] redrawTriple(int[] matches, int[][] splitHand) {
		int[] redrawTriple = {1, 1, 1, 1, 1};
		
		int[] keep = new int[5];
		
		keep[0] = matches[1];
		
		for(int i = 0; i != 5; i++) {
			for(int j = 0; j != 5; j++) {
				if(splitHand[0][i] == keep[j]){
					redrawTriple[i] = 0;
				}
			}	
		}
		
		return redrawTriple;
	}
	
	public int[] scores(int[][][] split, int player) {
		int[][] splitHand = new int[2][5];
		
		for(int i = 0; i != 5; i++) {
			splitHand[0][i] = split[player][0][i];
			splitHand[1][i] = split[player][1][i];
		}
		
		int[] scores = new int[6];
		
		boolean hasStraight = getHasStraight(splitHand);
		boolean hasFlush = getHasFlush(splitHand);
		if(hasStraight && hasFlush) {
			scores[0] = 10;
			Arrays.sort(splitHand[0]);
			scores[1] = splitHand[0][4];
		}
		if(hasFlush) {
			scores[0] = 7;
			Arrays.sort(splitHand[0]);
			scores[1] = splitHand[0][4];
			scores[2] = splitHand[0][3];
			scores[3] = splitHand[0][2];
			scores[4] = splitHand[0][1];
			scores[5] = splitHand[0][0];
		}
		if(hasStraight) {
			scores[0] = 6;
			Arrays.sort(splitHand[0]);
			scores[1] = splitHand[0][0];
		}
		
		int[] matches = getMatches(splitHand);
		if(matches[0] > 0) {
			System.arraycopy(matches, 0, scores, 0, 5);
			if(matches[0] == 5) {
				scores[0] = 9;
			}
			if(matches[0] == 4) {
				scores[0] = 8;
			}
		}
		
		if(matches[0] == 0) {
			Arrays.sort(splitHand[0]);
			scores[0] = 0;
			scores[1] = splitHand[0][4];
			scores[2] = splitHand[0][3];
			scores[3] = splitHand[0][2];
			scores[4] = splitHand[0][1];
			scores[5] = splitHand[0][0];
		}
		
		return scores;
	}
}



