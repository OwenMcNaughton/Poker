import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Screen extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	static Hand handObj = new Hand();

	JButton button0;
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	JButton button5;
	JButton button6;

	JLabel label0;
	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;
	JLabel label6;
	JLabel label7;
	JLabel label8;
	JLabel label9;
	JLabel label10;
	JLabel label11;

	GridBagConstraints c = new GridBagConstraints();

	ArrayList<String> deck;
	String[][] hands;
	int[][][] splitHand;

	int exchangeCount = 0;

	public static int players = 0;

	JLabel[] reverses = new JLabel[30];
	Image[] miscImages = new Image[10];

	String[] playerNames = new String[6];
	JLabel[] names;
	int[] monies = { 99, 99, 99, 99, 99, 99 };
	JLabel[] money;
	String[][] actions = new String[6][3];
	JLabel[] action = new JLabel[36];
	int[] confidence;
	int bet = 0;
	int[] allBets;
	int pot = 5;
	int highestBet = 0;
	int bettingRound = 0;
	int[][] redraws;
	
	boolean[] inGame;

	int turnIter = 0;
	
	boolean afterRedraw = false;

	public static void main(String[] args) {
		new Screen().setVisible(true);
	}

	private Screen() {
		super("Poker");
		setSize(800, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(new GridBagLayout());

		label0 = new JLabel("How many players?");
		c.insets = new Insets(0, 0, 30, 0);
		c.gridx = 0;
		c.gridy = 1;
		add(label0, c);

		c.insets = new Insets(10, 0, 0, 0);

		button2 = new JButton("Two");
		button2.setActionCommand("Two");
		button2.addActionListener(this);
		c.gridx = 0;
		c.gridy = 5;
		add(button2, c);

		button3 = new JButton("Three");
		button3.setActionCommand("Three");
		button3.addActionListener(this);
		c.gridx = 0;
		c.gridy = 6;
		add(button3, c);

		button4 = new JButton("Four");
		button4.setActionCommand("Four");
		button4.addActionListener(this);
		c.gridx = 0;
		c.gridy = 7;
		add(button4, c);

		button5 = new JButton("Five");
		button5.setActionCommand("Five");
		button5.addActionListener(this);
		c.gridx = 0;
		c.gridy = 8;
		add(button5, c);

		button6 = new JButton("Six");
		button6.setActionCommand("Six");
		button6.addActionListener(this);
		c.gridx = 0;
		c.gridy = 9;
		add(button6, c);

	}

	public void actionPerformed(ActionEvent e) {
		String name = e.getActionCommand();

		if (name.equals("Two")) {
			players = 2;
			setup();
		} else if (name.equals("Three")) {
			players = 3;
			setup();
		} else if (name.equals("Four")) {
			players = 4;
			setup();
		} else if (name.equals("Five")) {
			players = 5;
			setup();
		} else if (name.equals("Six")) {
			players = 6;
			setup();

		} else if (name.equals("next")) {
			setActionConfidence();
			nextBet();
		} else if (name.equals("okay")) {
			nextBet();
		} else if (name.equals("Fold")) {
			actions[turnIter - 1][0] = "folds";
			bet = 0;
			bettingRound++;
			turnIter = players - 1;
			
			inGame[turnIter] = false;
			
			playerBetDisplay();
		} else if (name.equals("Call")) {
			actions[turnIter - 1][0] = "calls";
			bet = highestBet;
			bettingRound++;
			turnIter = players - 1;
			playerBetDisplay();
		} else if (name.equals("Check")) {
			actions[turnIter - 1][0] = "checks";
			bet = 0;
			bettingRound++;
			turnIter = players - 1;
			playerBetDisplay();
		} else if (name.equals("Bet 1$")) {
			actions[turnIter - 1][0] = "bets";
			bet = 1;
			bettingRound++;
			turnIter = players - 1;
			playerBetDisplay();
		} else if (name.equals("Bet 5$")) {
			actions[turnIter - 1][0] = "bets";
			bet = 5;
			bettingRound++;
			turnIter = players - 1;
			playerBetDisplay();
		} else if (name.equals("Raise")) {
			actions[turnIter - 1][0] = "raises to";
			if (highestBet < 5) {
				bet = 5;
			}
			if (highestBet >= 5) {
				bet = highestBet + 1;
			}
			bettingRound++;
			turnIter = players - 1;
			playerBetDisplay();
		} else if (name.equals("redraw")) {
			redraw();
		} else if (name.equals("nextredraw")) {
			displayRedraw();
		} else if (name.equals("change1")) {
			if(exchangeCount != 3) {
				redraws[players-1][0] = 1;
				button2.setVisible(false);
				exchangeCount++;
			}
		} else if (name.equals("change2")) {
			if(exchangeCount != 3) {
				redraws[players-1][1] = 1;
				button3.setVisible(false);
				exchangeCount++;
			}
		} else if (name.equals("change3")) {
			if(exchangeCount != 3) {
				redraws[players-1][2] = 1;
				button4.setVisible(false);
				exchangeCount++;
			}
		} else if (name.equals("change4")) {
			if(exchangeCount != 3) {
				redraws[players-1][3] = 1;
				button5.setVisible(false);
				exchangeCount++;
			}
		} else if (name.equals("change5")) {
			if(exchangeCount != 3) {
				redraws[players-1][4] = 1;
				button6.setVisible(false);
				exchangeCount++;
			}
		} else if (name.equals("exchange")) {
			int[] redrawTotal = new int[players];
			
			redrawTotal[players-1] = redraws[players-1][0] + redraws[players-1][1] + 
					redraws[players-1][2] + redraws[players-1][3] + redraws[players-1][4];
			
			amendHands(players-1, redraws, redrawTotal);
			actions[players-1][1] = redrawTotal[players-1] + " cards";
			showHand();
			button0 = new JButton("Okay");
			button0.setActionCommand("exchangeover");
			button0.addActionListener(this);
			c.insets = new Insets(37, 0, 0, 0);
			c.gridx = 3;
			c.gridy = 25;
			add(button0, c); 
		} else if (name.equals("reset")) {
			exchangeCount = 0;
			redraws[players-1][0] = 0;
			redraws[players-1][1] = 0;
			redraws[players-1][2] = 0;
			redraws[players-1][3] = 0;
			redraws[players-1][4] = 0;
			button2.setVisible(true);
			button3.setVisible(true);
			button4.setVisible(true);
			button5.setVisible(true);
			button6.setVisible(true);
		} else if (name.equals("exchangeover")) {
			deal();
			secondBet();
		}
	}

	private void setup() {
		setPlayerNames();
		confidence = new int[players];
		allBets = new int[players];
		inGame = new boolean[players];
		for(int i = 0; i != players; i++) {
			inGame[i] = true;
		}
 
		deck = Poker.PokerInit1();
		hands = Poker.PokerInit2(deck, players);
		splitHand = Poker.PokerInit3(players, hands);

		setActionDeal();
		deal();
	}

	private void showHand() {
		removeAll();
		Image[][] cardImages = new Image[players][5];
		// Load player's hand images into cardImages[]
		Deck deckObj = new Deck();
		cardImages = deckObj.getCardImages(players, splitHand);

		label0 = new JLabel("This is");
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 1;
		add(label0, c);

		label1 = new JLabel("your hand.");
		c.insets = new Insets(0, 0, 20, 0);
		c.gridx = 2;
		c.gridy = 2;
		add(label1, c);

		// All the card images
		c.insets = new Insets(0, 0, 0, 5);
		c.gridy = 10;
		ImageIcon icon = new ImageIcon();
		
		icon = new ImageIcon(cardImages[players-1][0]);
		label3 = new JLabel(icon);
		c.gridx = 0;
		add(label3, c);
		icon = new ImageIcon(cardImages[players-1][1]);
		label4 = new JLabel(icon);
		c.gridx = 1;
		add(label4, c);
		icon = new ImageIcon(cardImages[players-1][2]);
		label5 = new JLabel(icon);
		c.gridx = 2;
		add(label5, c);
		icon = new ImageIcon(cardImages[players-1][3]);
		label6 = new JLabel(icon);
		c.gridx = 3;
		add(label6, c);
		icon = new ImageIcon(cardImages[players-1][4]);
		label7 = new JLabel(icon);
		c.gridx = 4;
		add(label7, c);
		// All the card images
	}

	private void playerBet() {
		button2 = new JButton("Fold");
		button2.setActionCommand("Fold");
		button2.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 20;
		add(button2, c);

		for (int i = 0; i != players - 1; i++) {
			if (allBets[i] > highestBet) {
				highestBet = allBets[i];
			}
		}

		String b3Name = "Call";
		if (highestBet == 0) {
			b3Name = "Check";
		}

		button3 = new JButton(b3Name);
		button3.setActionCommand(b3Name);
		button3.addActionListener(this);
		c.gridx = 2;
		add(button3, c);

		String b4Name = "Raise";
		if (highestBet == 0) {
			b4Name = "Bet 1$";
		}

		button5 = new JButton(b4Name);
		button5.setActionCommand(b4Name);
		button5.addActionListener(this);
		c.gridx = 3;
		add(button5, c);

		for (int i = 0; i != players - 1; i++) {
			pot += allBets[i];
		}

		label8 = new JLabel("Pot: $" + pot);
		c.insets = new Insets(20, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 25;
		add(label8, c);

		if (highestBet != 0) {
			label9 = new JLabel("Call: $" + highestBet);
			c.gridx = 2;
			add(label9, c);
		}

		int raise = highestBet + 1;

		if (highestBet < 5) {
			raise = 5;
		}

		if (highestBet == 0) {
			label10 = new JLabel("Bet 1$");
			c.gridx = 3;
			add(label10, c);
			label11 = new JLabel("Bet 5$");
			c.gridx = 4;
			add(label11, c);
			button4 = new JButton("Bet 5$");
			button4.setActionCommand("Bet 5$");
			button4.addActionListener(this);
			c.insets = new Insets(5, 0, 0, 0);
			c.gridy = 20;
			c.gridx = 4;
			add(button4, c);
		} else {
			label10 = new JLabel("Raise to: $" + raise);
			c.gridx = 3;
			add(label10, c);
		}
	}

	private void deal() {
		removeAll();

		String dir = "res\\B.jpg";
		File sourceImage = new File(dir);
		try {
			miscImages[0] = ImageIO.read(sourceImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon back = new ImageIcon(miscImages[0]);

		for (int i = 0; i != reverses.length; i++) {
			reverses[i] = new JLabel(back);
		}

		names = new JLabel[players];

		int topInset = 0;
		int leftInset = 0;
		int count = -1;
		c.gridy = 0;
		for (int j = 0; j != 5; j++) {
			for (int i = 0; i != players; i++) {
				count++;
				c.gridx = i;
				c.insets = new Insets(0, 5, topInset, leftInset);
				add(reverses[count], c);
			}
			topInset += 7;
			leftInset += 7;
		}

		c.insets = new Insets(0, 0, 0, 0);
		for (int i = 0; i != players; i++) {
			names[i] = new JLabel(playerNames[i]);
			c.gridx = i;
			c.gridy = 10;
			add(names[i], c);

		}

		for (int i = 0; i != players; i++) {
			action[i] = new JLabel(actions[i][0]);
			c.gridx = i;
			c.gridy = 12;
			add(action[i], c);
		}

		for (int i = players; i != players + players; i++) {
			action[i] = new JLabel(actions[i - players][1]);
			c.gridx = i - players;
			c.gridy = 13;
			add(action[i], c);
		}

		money();

		button0 = new JButton("Next turn");
		button0.setActionCommand("next");
		button0.addActionListener(this);
		c.gridx = players - 1;
		c.gridy = 20;
		c.insets = new Insets(20, 0, 0, 0);
		add(button0, c);

	}

	public void removeAll() {
		if (button0 != null) {
			if (button0.isVisible()) {
				button0.setVisible(false);
			}
		}
		if (button1 != null) {
			if (button1.isVisible()) {
				button1.setVisible(false);
			}
		}
		if (button2 != null) {
			if (button2.isVisible()) {
				button2.setVisible(false);
			}
		}
		if (button3 != null) {
			if (button3.isVisible()) {
				button3.setVisible(false);
			}
		}
		if (button4 != null) {
			if (button4.isVisible()) {
				button4.setVisible(false);
			}
		}
		if (button5 != null) {
			if (button5.isVisible()) {
				button5.setVisible(false);
			}
		}
		if (button6 != null) {
			if (button6.isVisible()) {
				button6.setVisible(false);
			}
		}

		for (int i = 0; i != 30; i++) {
			if (reverses[i] != null) {
				if (reverses[i].isVisible()) {
					reverses[i].setVisible(false);
				}
			}
		}

		for (int i = 0; i != players; i++) {
			if (names != null) {
				if (names[i].isVisible()) {
					names[i].setVisible(false);
				}
			}
		}

		for (int i = 0; i != players; i++) {
			if (money != null) {
				if (money[i].isVisible()) {
					money[i].setVisible(false);
				}
			}
		}

		for (int i = 0; i != 18; i++) {
			if (action[i] != null) {
				if (action[i].isVisible()) {
					action[i].setVisible(false);
				}
			}
		}

		if (label0 != null) {
			while (label0.isVisible()) {
				label0.setVisible(false);
			}
		}
		if (label1 != null) {
			if (label1.isVisible()) {
				label1.setVisible(false);
			}
		}
		if (label2 != null) {
			if (label2.isVisible()) {
				label2.setVisible(false);
			}
		}
		if (label3 != null) {
			if (label3.isVisible()) {
				label3.setVisible(false);
			}
		}
		if (label4 != null) {
			if (label4.isVisible()) {
				label4.setVisible(false);
			}
		}
		if (label5 != null) {
			if (label5.isVisible()) {
				label5.setVisible(false);
			}
		}
		if (label6 != null) {
			if (label6.isVisible()) {
				label6.setVisible(false);
			}
		}
		if (label7 != null) {
			if (label7.isVisible()) {
				label7.setVisible(false);
			}
		}
		if (label8 != null) {
			if (label8.isVisible()) {
				label8.setVisible(false);
			}
		}
		if (label9 != null) {
			if (label9.isVisible()) {
				label9.setVisible(false);
			}
		}
		if (label10 != null) {
			if (label10.isVisible()) {
				label10.setVisible(false);
			}
		}
		if (label11 != null) {
			if (label11.isVisible()) {
				label11.setVisible(false);
			}
		}
	}

	private void setPlayerNames() {
		List<String> names = Arrays.asList("Abel", "Abby", "Adam", "Alex",
				"Dink", "Drew", "Dana", "Edna", "Fred", "Gene", "Gwen", "Ezra",
				"Lyle", "Matt", "Nina", "Otis", "Rita", "Walt", "Sara", "Ruth",
				"Mona", "Mary", "Neil", "Link", "Kurt", "Jeff");

		playerNames[players - 1] = "User";

		Collections.shuffle(names);

		for (int i = players - 2; i != -1; i--) {
			playerNames[i] = names.get(i);
		}
	}

	private void money() {
		money = new JLabel[players];
		c.gridy = 15;
		c.insets = new Insets(20, 0, 0, 0);
		for (int i = 0; i != players; i++) {
			money[i] = new JLabel("$" + monies[i]);
			c.gridx = i;
			add(money[i], c);
		}
	}

	private void setActionDeal() {
		for (int i = 0; i != players; i++) {
			actions[i][0] = "was dealt";
		}
		for (int i = 0; i != players; i++) {
			actions[i][1] = "5 cards";
		}
	}

	private void setActionConfidence() {
		for (int i = 0; i != players; i++) {
			confidence[i] = handObj.getConfidence(i, splitHand);
		}

		button0.setVisible(false);

		button0 = new JButton("Okay");
		button0.setActionCommand("okay");
		button0.addActionListener(this);
		c.gridx = players - 1;
		c.gridy = 20;
		c.insets = new Insets(20, 0, 0, 0);
		add(button0, c);
	}

	private void nextBet() {
		action[turnIter].setVisible(false);
		action[turnIter + players].setVisible(false);

		if (!actions[turnIter][0].equals("folds")) {

			if (turnIter == 0 && bettingRound == 0) {
				bet = assessRisk();
				allBets[0] = bet;
				bet = 0;
			} else if (turnIter < players - 1) {
				bet = otherBets();
				allBets[turnIter] = bet;
				bet = 0;
			}
			
			if(actions[turnIter].equals("folds")) {
				inGame[turnIter] = false;
			}

			if (actions[turnIter][0].equals("bets")
					|| actions[turnIter][0].equals("raises to")
					|| actions[turnIter][0].equals("calls")) {
				actions[turnIter][1] = allBets[turnIter] + "$";
				if (turnIter != players - 1) {
					monies[turnIter] -= allBets[turnIter];
				}
				money[turnIter].setVisible(false);
				c.gridy = 15;
				c.insets = new Insets(20, 0, 0, 0);
				money[turnIter] = new JLabel("$" + monies[turnIter]);
				c.gridx = turnIter;
				add(money[turnIter], c);
			} else {
				actions[turnIter][1] = "";
			}

			c.insets = new Insets(0, 0, 0, 0);
			action[turnIter] = new JLabel(actions[turnIter][0]);
			c.gridx = turnIter;
			c.gridy = 12;
			add(action[turnIter], c);

			action[turnIter + players] = new JLabel(actions[turnIter][1]);
			c.gridx = turnIter;
			c.gridy = 13;
			add(action[turnIter + players], c);

		}

		checkBetOver();

		turnIter++;

		if (turnIter == players) {
			if(inGame[players-1]) {
				showHand();
				playerBet();
			} else {
				bettingRound++;
				turnIter = players - 1;
				playerBetDisplay();
			}
		}
	}

	private int assessRisk() {
		Random gen = new Random();
		int r = gen.nextInt(10);

		actions[turnIter][0] = "checks";
		if (confidence[turnIter] > 20) { // If he has higher than a three of a
											// kind
			actions[turnIter][0] = "bets";
			bet = 5; // Usually bet big
			if (r == 9) { // 1 in 10 chance of bluffing nothing
				actions[turnIter][0] = "checks";
				bet = 0;
			}
			if (r > 6) { // 1 in 5 chance of bluffing small
				bet = 1;
			}

		}
		if (confidence[turnIter] > 10) { // If he has higher than a pair
			actions[turnIter][0] = "bets";
			bet = 1; // Usually bet small
			if (r == 9) { // 1 in 10 chance of bluffing big
				bet = 5;
			}
			if (r > 6) { // 1 in 5 chance of bluffing nothing
				actions[turnIter][0] = "checks";
				bet = 0;
			}

		}
		if (confidence[turnIter] > 5) { // If he has a pair
			actions[turnIter][0] = "bets";
			bet = 1; // Usually bet small
			if (r == 9) { // 1 in 10 chance of bluffing big
				actions[turnIter][0] = "bets";
				bet = 5;
			}
			if (r > 6) { // 1 in 5 chance of checking
				actions[turnIter][0] = "checks";
				bet = 0;
			}

			if (confidence[turnIter] > 2) {
				actions[turnIter][0] = "checks"; // Usually check
				bet = 0;
				if (r == 9) { // 1 in 10 chance of betting big
					actions[turnIter][0] = "bets";
					bet = 1;
				}
				if (r == 8) { // 1 in 10 chance of betting big
					actions[turnIter][0] = "bets";
					bet = 5;
				}
			}

		}
		if (r == 9 && confidence[turnIter] < 6) { // 1 in 10 chance of bluffing
													// small
			actions[turnIter][0] = "bets";
			bet = 1;
		}
		if (r == 8 && confidence[turnIter] < 6) { // 1 in 10 chance of bluffing
													// big
			actions[turnIter][0] = "bets";
			bet = 5;
		}

		return bet;
	}

	private int otherBets() {

		for (int i = 0; i != players - 1; i++) {
			if (allBets[i] > highestBet) {
				highestBet = allBets[i];
			}
		}

		bet = assessRisk();

		allBets[turnIter] = bet;

		Random gen = new Random();
		int r = gen.nextInt(10);

		if (actions[turnIter][0].equals("checks")) { // If you have nothing
			if (highestBet >= 5) { // And there's a high bet
				actions[turnIter][0] = "folds"; // Usually Fold
				if (r == 9) {
					actions[turnIter][0] = "raises to"; // Or 1 in 10 raise
					bet = highestBet + 1;
				}
				if (r > 6) {
					actions[turnIter][0] = "calls"; // Or 1 in 5 call
					bet = highestBet;
				}
			}
			if (highestBet >= 1 && highestBet < 5) { // And there's a low bet
				actions[turnIter][0] = "folds"; // Sometimes fold
				if (r == 9) {
					actions[turnIter][0] = "raises to"; // Or 1 in 10 raise
					bet = 5;
				}
				if (r > 6) {
					actions[turnIter][0] = "calls"; // Or 1 in 2 call
					bet = highestBet;
				}
			}
			if (highestBet == 0) { // And there's no bet
									// Usually check
				if (r == 9) {
					actions[turnIter][0] = "bets"; // Or 1 in 10 bet high
					bet = 5;
				}
				if (r > 6) {
					actions[turnIter][0] = "bets"; // Or 1 in 5 bet low
					bet = 1;
				}
			}
			return bet;
		}

		if (actions[turnIter][0].equals("bets") && allBets[turnIter] == 5) { // If
																				// you're
																				// betting
																				// high
			if (highestBet >= 5) { // And there's a high bet
				actions[turnIter][0] = "calls"; // Usually call
				bet = highestBet;
				if (r == 9) {
					actions[turnIter][0] = "raises to"; // Or 1 in 10 raise
					bet = highestBet + 1;
				}
			}
			if (highestBet >= 1 && highestBet < 5) { // And there's a low bet
				actions[turnIter][0] = "raises to"; // Usually raise
				bet = 5;
				if (r == 9) {
					actions[turnIter][0] = "checks"; // Or 1 in 10 check
					bet = highestBet;
				}
			}
			if (highestBet == 0) { // And there's no bet
				// 1 in 2 bet high
				if (r > 4) { // Or 1 in 2 bet low
					bet = 1;
				}
			}
			return bet;
		}

		if (actions[turnIter][0].equals("bets") && allBets[turnIter] == 1) { // If
																				// you're
																				// betting
																				// low
			if (highestBet >= 5) { // And there's a high bet
				actions[turnIter][0] = "calls"; // Usually call
				bet = highestBet;
				if (r == 9) { // Or 1 in 10 fold
					actions[turnIter][0] = "folds";
					bet = 0;
				}
				if (r > 6) { // Or 1 in 5 raise
					actions[turnIter][0] = "raises to";
					bet = highestBet + 1;
				}
			}
			if (highestBet >= 1 && highestBet < 5) { // And there's a low bet
				actions[turnIter][0] = "calls"; // Usually call
				bet = highestBet;
				if (r > 7) { // Or 1 in 5 raise
					actions[turnIter][0] = "raises to";
					bet = 5;
				}
			}
			if (highestBet == 0) { // And there's no bet
				// Usually bet small
				if (r > 7) {
					bet = 5; // Or 1 in 5 bet big
				}
			}
			return bet;
		}

		return bet;
	}

	private void checkBetOver() {
		if (bettingRound != 0) {
			if (actions[turnIter][0].equals("calls")
					|| actions[turnIter][0].equals("checks")
					|| actions[turnIter][0].equals("folds")) {
			}
		}
		return;
	}

	private void playerBetDisplay() {
		removeAll();

		allBets[players - 1] = bet;

		String dir = "res\\B.jpg";
		File sourceImage = new File(dir);
		try {
			miscImages[0] = ImageIO.read(sourceImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageIcon back = new ImageIcon(miscImages[0]);

		for (int i = 0; i != reverses.length; i++) {
			reverses[i] = new JLabel(back);
		}

		names = new JLabel[players];

		int topInset = 0;
		int leftInset = 0;
		int count = -1;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		for (int j = 0; j != 5; j++) {
			for (int i = 0; i != players; i++) {
				count++;
				c.gridx = i;
				c.insets = new Insets(0, 5, topInset, leftInset);
				add(reverses[count], c);
			}
			topInset += 7;
			leftInset += 7;
		}

		c.insets = new Insets(0, 0, 0, 0);
		for (int i = 0; i != players; i++) {
			names[i] = new JLabel(playerNames[i]);
			c.gridx = i;
			c.gridy = 10;
			add(names[i], c);

		}

		for (int i = 0; i != players; i++) {
			action[i] = new JLabel(actions[i][0]);
			c.gridx = i;
			c.gridy = 12;
			add(action[i], c);
		}

		money();
		if (actions[turnIter][0].equals("bets")
				|| actions[turnIter][0].equals("raises to")
				|| actions[turnIter][0].equals("calls")) {
			actions[turnIter][1] = allBets[turnIter] + "$";
			monies[turnIter] -= allBets[turnIter];
			money[turnIter].setVisible(false);
			c.gridy = 15;
			c.insets = new Insets(0, 0, 0, 0);
			money[turnIter] = new JLabel("$" + monies[turnIter]);
			c.gridx = turnIter;
			add(money[turnIter], c);
		}

		for (int i = players; i != players + players; i++) {
			action[i] = new JLabel(actions[i - players][1]);
			c.gridx = i - players;
			c.gridy = 13;
			add(action[i], c);
		}

		if (bet <= highestBet) {
			if(!afterRedraw) {
				for (int i = 1; i != players; i++) {
					if (allBets[i] > highestBet) {
						highestBet = allBets[i];
					}
				}
				button0 = new JButton("Redraw");
				button0.setActionCommand("redraw");
				button0.addActionListener(this);
				c.gridx = players - 1;
				c.gridy = 20;
				c.insets = new Insets(20, 0, 0, 0);
				add(button0, c);
			}
			if(afterRedraw) {
				findWinner();
			}
			
		} else {
			for (int i = 1; i != players; i++) {
				if (allBets[i] > highestBet) {
					highestBet = allBets[i];
				}
			}
			turnIter = 0;
			button0 = new JButton("Next turn");
			button0.setActionCommand("okay");
			button0.addActionListener(this);
			c.gridx = players - 1;
			c.gridy = 20;
			c.insets = new Insets(20, 0, 0, 0);
			add(button0, c);

		}
	}

	private void findWinner() {
		int winner = 0;
		int[] winners = new int[players];
		
		int[][] scores = new int[players][6];
		
		for(int i = 0; i != players; i++) {
			if(inGame[i]) {
				scores[i] = handObj.scores(splitHand, i);
			}
		
		}
		
		int j = 0;
		for(int i = 0; i != players-1; i++) {
			if(j != 6) {
				if(Arrays.equals(scores[winner], scores[i+1])) {
					winners[winner] = 1;
					winners[i+1] = 1;
				}
				if(scores[winner][j] > scores[i+1][j]) {
					j = 0;
				} else if(scores[winner][j] < scores[i+1][j]) {
					winner = i+1;
					j = 0;
				} else if(scores[winner][j] == scores[i+1][j]) {
					j++;
					i--;
				}
			} else {
				j = 0;
			}
		}
		
		boolean tie = false;
		
		for(int i = 0; i != players; i++) {
			if(winners[i] == 1) {
				tie = true;
			}
		}
		
		if(tie) {
			displayTie(winners);
		} else {
			displayWinner(winner);
		}
	}

	private void displayWinner(int winner) {
		removeAll();
		
		Image[][] cardImages = new Image[players][5];
		// Load player's hand images into cardImages[]
		Deck deckObj = new Deck();
		cardImages = deckObj.getCardImages(players, splitHand);
		
		label0 = new JLabel(playerNames[winner] + " won " + pot + " $");
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 30, 0);
		add(label0, c);
		
		c.gridy = c.gridy + 10;
		showEveryHand(winner, cardImages);
		c.gridy = c.gridy + 10;
		if(winner != players-1) {
			showEveryHand(players-1, cardImages);
		}
		
	}

	private void displayTie(int[] winners) {
		removeAll();
		
	}

	public void redraw() {
		redraws = new int[players][5];
		
		for(int i = 0; i != players-1; i++) {
			if(!actions[i][0].equals("folds")) {
				redraws[i] = handObj.getRedraw(splitHand, i);
			}
			
		}
		
		int[] redrawTotal = new int[players];
		
		for(int i = 0; i != players; i++) {
			redrawTotal[i] = redraws[i][0] + redraws[i][1] + redraws[i][2] 
					+ redraws[i][3] + redraws[i][4];
		}
		
		for(int i = 0; i != players; i++) {
			amendHands(i, redraws, redrawTotal);
		}
		
		for (int i = 0; i != players; i++) {
			if(!actions[i][0].equals("folds")) {
				actions[i][0] = "Redrew";
			}
		}
		for (int i = 0; i != players; i++) {
			if(!actions[i][0].equals("folds")) {
				actions[i][1] = redrawTotal[i] + " cards";
			}
		}
		
		button0.setVisible(false);
		
		turnIter = 0;
		
		button0 = new JButton("Next");
		button0.setActionCommand("nextredraw");
		button0.addActionListener(this);
		c.gridx = players - 1;
		c.gridy = 20;
		c.insets = new Insets(20, 0, 0, 0);
		add(button0, c);
		
	}
	
	public void displayRedraw() {
		action[turnIter].setVisible(false);
		action[turnIter + players].setVisible(false);
		
		c.insets = new Insets(0, 0, 0, 0);
		action[turnIter] = new JLabel(actions[turnIter][0]);
		c.gridx = turnIter;
		c.gridy = 12;
		add(action[turnIter], c);

		action[turnIter + players] = new JLabel(actions[turnIter][1]);
		c.gridx = turnIter;
		c.gridy = 13;
		add(action[turnIter + players], c);
		
		turnIter++;
		
		if(turnIter == players) {
			if(inGame[players-1]) {
				showHand();
				playerRedraw();
			}
			turnIter = 0;
			if(!inGame[players-1]) {
				deal();
				secondBet();
			}
		}
	}
	
	private void playerRedraw() {
		button0 = new JButton("Okay");
		button0.setActionCommand("exchange");
		button0.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 3;
		c.gridy = 25;
		add(button0, c); 
		
		button1 = new JButton("Reset");
		button1.setActionCommand("reset");
		button1.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 25;
		add(button1, c); 
		
		button2 = new JButton("Change");
		button2.setActionCommand("change1");
		button2.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 20;
		add(button2, c);
		
		button3 = new JButton("Change");
		button3.setActionCommand("change2");
		button3.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 20;
		add(button3, c);
		
		button4 = new JButton("Change");
		button4.setActionCommand("change3");
		button4.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 20;
		add(button4, c);
		
		button5 = new JButton("Change");
		button5.setActionCommand("change4");
		button5.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 3;
		c.gridy = 20;
		add(button5, c);
		
		button6 = new JButton("Change");
		button6.setActionCommand("change5");
		button6.addActionListener(this);
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 4;
		c.gridy = 20;
		add(button6, c);
	}

	public void amendHands(int player, int[][] redraw2, int[] redrawTotal2) {
		
		int[] redraw = new int[5];
		System.arraycopy(redraw2[player], 0, redraw, 0, redraw2[player].length);
		
		int redrawTotal = redrawTotal2[player];
		
		for(; redrawTotal != 0; redrawTotal--) {

			String card = deck.get(0);
			deck.remove(0);
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
			
			int picker = 0;
			
			for(int i = 0; i != 5; i++) {
				if(redraw[i] == 1) {
					redraw[i] = 0;
					picker = i;
					break;
				}
			}
			
			splitHand[player][0][picker] = value;
			splitHand[player][1][picker] = suit;

		}
	}

	public void secondBet() {
		button0.setVisible(false);
		
		turnIter = 0;
		
		bet = 0;
		for(int i = 0; i != players; i++) {
			allBets[i] = 0;
		}
		bettingRound = 0;
		highestBet = 0;
		
		button0 = new JButton("Next bet");
		button0.setActionCommand("next");
		button0.addActionListener(this);
		c.gridx = players - 1;
		c.gridy = 20;
		c.insets = new Insets(20, 0, 0, 0);
		add(button0, c);
		
		afterRedraw = true;
		
	}

	public void showEveryHand(int player, Image[][] cardImages) {
		
		label2 = new JLabel(playerNames[player] + "'s hand:");
		c.insets = new Insets(20, 0, 10, 5);
		c.gridx = 2;
		add(label2, c);
		
		c.insets = new Insets(0, 0, 10, 5);
		ImageIcon icon = new ImageIcon();
		c.gridy = c.gridy + 1;
		
		icon = new ImageIcon(cardImages[player][0]);
		label3 = new JLabel(icon);
		c.gridx = 0;
		add(label3, c);
		icon = new ImageIcon(cardImages[player][1]);
		label4 = new JLabel(icon);
		c.gridx = 1;
		add(label4, c);
		icon = new ImageIcon(cardImages[player][2]);
		label5 = new JLabel(icon);
		c.gridx = 2;
		add(label5, c);
		icon = new ImageIcon(cardImages[player][3]);
		label6 = new JLabel(icon);
		c.gridx = 3;
		add(label6, c);
		icon = new ImageIcon(cardImages[player][4]);
		label7 = new JLabel(icon);
		c.gridx = 4;
		add(label7, c);
	}
}

