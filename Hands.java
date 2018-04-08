package projects.cyclesInWar;

import java.util.ArrayList;

public class Hands {
	private Quene<Cards> handOne;
	private Quene<Cards> handTwo;

	public Hands() {
		handOne = (Quene<Cards>) new Quene<Cards>();
		handTwo = (Quene<Cards>) new Quene<Cards>();
	}
	public static Hands createHands(int numCards) {
		Hands h = new Hands();
		h = createRandomHands(numCards);
		return h;
	}
	
	// === Creates all permutations and checks how many are infinite games ===
	/***
	 * Runs multiple helper methods together in order to find the number of infinite games given a number of Cards.
	 * createDeckString 		- Based on the number of cards, create a String of numbers separated by commas.
	 * getNumsString 		- Separate those numbers into individual elements for an ArrayList String.
	 * getPermsString 		- From the ArrayList String of numbers, find and return all possible permutations as ArrayList String.
	 * infiniteWarPermCards	- Given every permutation of Cards, create every possible set of hands and determine how many are infinite games.
	 * @param numCards, the number of Cards in the deck.
	 * @return
	 */
	public static int infiniteWarPermCards(int numCards) {
		ArrayList<String> permsString = getPermsString(getNumsString(createDeckString(numCards)));		
		return infiniteWarPermCards(permsString);
	}
	/***
	 * Find the number of infinite games given every permutation of Cards.
	 * Splits every permutation of Cards into two separate Hands, thus giving us every possible set of Hands.
	 * Determines if each set is an infinite game, if so add one to the current count.
	 * @param str, ArrayList String output from getPermsString.
	 * @return counter, the number of infinite games given the permutations of Card Values.
	 */
	private static int infiniteWarPermCards(ArrayList<String> str) {
		int counter = 0;
		int infiniteGames = 0;
		double percent = 0.10;
		
		System.out.println(" +++++++++++++ Started +++++++++++++ ");
		
		for(int s = 0; s < str.size(); s++) {
			if(s >= str.size() * percent) {
				counter++;
				percent = counter * 0.01;
				System.out.println("====\tPercent Complete: " + (int)(percent*100) + "%\t====");
			}
			
			if(splitCards(str.get(s)).infiniteWarGame()) 
				infiniteGames++;
		}
		
		System.out.println(" ++++++++++++ Finished ++++++++++++ ");
		
		return infiniteGames;
	}
	
	// === Checks if Current Hands would result in an Infinite War ===
	/***
	 * Checks to see if the current set of Hands would result in an infinite game.
	 * Runs the wasPreviousHands method to check if the current set of Hands result in an infinite game.
	 * Runs "Updates and Checks values for initial Hand" on the initial Hand to find Letter Values for the initial Hand.
	 * @return false if the current game is not infinite.
	 * @return true otherwise.
	 */
	public boolean infiniteWarGame() {
		Quene<Hands> previousHands = new Quene<Hands>();
		Hands initialHand = cloneHand(this);
		int status = 0;
		
		while(handOne.get(0).getLetterValue() != 0 && handTwo.get(0).getLetterValue() != 0) {
			playWar(status); // playWar until we find the largest card, only card with a known value
		}

		while(handOne.size() > 0 && handTwo.size() > 0 && !wasPreviousHands(previousHands)) {
			previousHands.push(cloneHand(this));
			playWar(status);
		}
		
		if(handOne.size() == 0 || handTwo.size() == 0)
			return false;
		
		status++;
				
		if(duplicateLetters(initialHand))
			initialHand = updateDuplicateLetterValues(initialHand);
		
		while(unknownValues(initialHand)) {
			previousHands = new Quene<Hands>();
			while(!wasPreviousHands(previousHands)) {
				previousHands.push(cloneHand(this));
				playWar(status);
				initialHand = updateLetterValues(initialHand);
			}
		}

		if(duplicateLetters(initialHand))
			initialHand = updateDuplicateLetterValues(initialHand);
		
//		System.out.println(initialHand.toCardValue());		
//		System.out.println(initialHand.toLetter());

		return true;
	}
	
	// === Checks for infinite games ===
	/***
	 * Given all previous sets of Hands, check if the current set has appeared previously.
	 * @param previousHands, all previous sets of Hands.
	 * @return true if it has appeared before.
	 * @return false otherwise.
	 */
	private boolean wasPreviousHands(Quene<Hands> previousHands) {
		for(int i = 0; i < previousHands.size(); i++) {
			if(this.toCardValue().equals(previousHands.get(i).toCardValue()))
				return true;
		}
		return false;
	}

	
	// === Updates and Checks values for initial Hand ===
	/***
	 * Updates the Letter Values in the initial Hand based on the Letter Values of the current Hand.
	 * @param initial, the initial Hand to update.
	 * @return initial Hand with updated Letter Values.
	 */
	private Hands updateLetterValues(Hands initial) {
		Quene<Cards> stack = new Quene<Cards>();
		
		// gathering all Cards from the current Hands into stack
		for(int i = 0; i < this.handOne.size(); i++) {stack.push(this.handOne.get(i));}
		for(int i = 0; i < this.handTwo.size(); i++) {stack.push(this.handTwo.get(i));}
		
		while(stack.size() > 0) {
			// take a single Card from the stack
			Cards c = stack.pop();
			boolean updated = false;
			int initialCardValue;
			int stackCardValue = c.getCardValue();
			int stackLetterValue = c.getLetterValue();
			
			// find the location of the Card from the stack in the initial Hands
			// update the letter value from the stack into initial Hands
			for(int i = 0; i < initial.handOne.size(); i++) {
				initialCardValue = initial.handOne.get(i).getCardValue();
				
				if(initialCardValue == stackCardValue) {
					initial.handOne.get(i).setLetterValue(stackLetterValue);
					updated = true;
					break;
				}
			}
			if(updated == false) { // done to increase efficiency slightly by not checking 2nd hand if already updated
				for(int i = 0; i < initial.handTwo.size(); i++) {
					initialCardValue = initial.handTwo.get(i).getCardValue();
				
					if(initialCardValue == stackCardValue) {
						initial.handTwo.get(i).setLetterValue(stackLetterValue);
						break;
					}
				}
			}
		}
		
		return initial;
		
 	}
	/***
	 * Checks if there are any duplicate Letter Values within both Hands at the same card location.
	 * @param initial, the initial Hand to be checked.
	 * @return true if there are any duplicate Letters in the initialHand.
	 * @return false otherwise.
	 */
	private boolean duplicateLetters(Hands initial) {
		int size = Math.min(initial.handOne.size(), initial.handTwo.size());
		
		for(int i = 0; i < size; i++) {
			int letterValueOne = initial.handOne.get(i).getLetterValue();
			int letterValueTwo = initial.handTwo.get(i).getLetterValue();
			
			if(letterValueOne == letterValueTwo)
				return true;
		}
		
		return false;
	}
	/***
	 * Updates the duplicate Letter Values in the initial Hands based on the Card Value.
	 * @param initial, the initial Hand to be updated.
	 * @return an updated initial Hand with no duplicate Letter Values.
	 */
	private Hands updateDuplicateLetterValues(Hands initial) {
		int size = Math.min(initial.handOne.size(), initial.handTwo.size());
		
		for(int i = 0; i < size; i++) {
			int letterValueOne = initial.handOne.get(i).getLetterValue();
			int letterValueTwo = initial.handTwo.get(i).getLetterValue();
			
			if(letterValueOne == letterValueTwo && letterValueOne != -1) {
				int cardValueOne = initial.handOne.get(i).getCardValue();
				int cardValueTwo = initial.handTwo.get(i).getCardValue();
				
				if(cardValueOne < cardValueTwo) initial.handOne.get(i).setLetterValue(letterValueOne + 1); 
				else initial.handTwo.get(i).setLetterValue(letterValueTwo + 1);
					
			}
		}
		
		return initial;
	}
	/***
	 * Checks if there are any unknown Letter Values in both Hands.
	 * @param h, the set of Hands to be checked.
	 * @return true if there are any unknown Letter Values.
	 * @return false otherwise.
	 */
	private boolean unknownValues(Hands h) {
		for(int i = 0; i < h.handOne.size(); i++) {
			int letterValue = h.handOne.get(i).getLetterValue();
			
			if(letterValue == -1)
				return true;
		}
		for(int i = 0; i < h.handTwo.size(); i++) {
			int letterValue = h.handTwo.get(i).getLetterValue();
		
			if(letterValue == -1)
				return true;
		}
		
		return false;
	}
	
	/***
	 * Goes through one iteration of war and updates the game according to the status of the game.
	 * The value of status is recorded within infiniteWarGame as that is where we want to keep track of where the game is.
	 * Status can have only 2 values.
	 * 	0 - the beginning of infiniteWarGame, do not update the game as we are looking for the only known value first.
	 * 	1 - after finding the largest value, only update when we are certain that it is an infiniteGame
	 * @param status, value stored in infiniteGame in order to keep track of the current status of the game.
	 */
	private void playWar(int status) {
		Cards cardOne = handOne.pop();
		Cards cardTwo = handTwo.pop();
		
		updateGame(cardOne, cardTwo, status);
	}	
	/***
	 * Compares two Cards and updates the game according to which Card is bigger + the status of the game.
	 * @param cardOne, the Card from Hand One.
	 * @param cardTwo, the Card from Hand Two.
	 * @param status, the current status of the Game as described in playWar.
	 */
	private void updateGame(Cards cardOne, Cards cardTwo, int status) {
		int oneIsBigger = cardOne.compareTo(cardTwo); // bigger if positive, smaller if negative
		int letterOne = cardOne.getLetterValue();
		int letterTwo = cardTwo.getLetterValue();
		final int unknown = -1;
		
		if(oneIsBigger > 0) {
			if(status == 1) {
				if(letterOne != unknown && letterTwo != unknown && letterOne < letterTwo) cardTwo.setLetterValue(letterOne + 1);
				else if(letterOne != unknown && letterTwo == unknown) cardTwo.setLetterValue(letterOne + 1);
				else if(letterOne == unknown && letterTwo != unknown) cardOne.setLetterValue(letterTwo - 1);
			}
			
			handOne.push(cardTwo);
			handOne.push(cardOne);
		}
		else{
			if(status == 1) {
				if(letterOne != unknown && letterTwo != unknown && letterOne > letterTwo) cardOne.setLetterValue(letterTwo + 1);
				else if(letterOne != unknown && letterTwo == unknown) cardTwo.setLetterValue(letterOne - 1);
				else if(letterOne == unknown && letterTwo != unknown) cardOne.setLetterValue(letterTwo + 1);
			}
			
			handTwo.push(cardOne);
			handTwo.push(cardTwo);
		}
	}
	
	// === Helper Methods ===
	/***
	 * Creates a deck represented by a String of numbers separated by commas.
	 * The numbers represents the Cards within the deck without using as much 
	 * memory by not creating Card objects.
	 * @param numCards, the number of Cards wanted in the new deck.
	 * @return String of numbers separated by commas.
	 */
	private static String createDeckString(int numCards) {
		String output = "";
		
		for(int i = 1; i <= numCards; i++) {
			output += (i + ",");
		}
		
		return output;
	}
	/***
	 * Creates a sorted Deck of Cards.
	 * @param numCards, the number of Cards wanted in the new deck.
	 * @return a sorted Deck of Cards.
	 */
	private static Quene createDeckCards(int numCards) {
		Quene q = (Quene<Cards>) new Quene<Cards>();
		Cards c;
		
		for(int i = 0; i < numCards; i++) {
			c = Cards.createCard(i + 1);
			if(i + 1 == numCards) c.setLetterValue(0);
			q.push(c);
		}
		
		return q;
	}	
	/***
	 * Creates Hands with randomly assigned Cards.
	 * @param numCards, the total number of Cards.
	 * @return Hands with randomly assigned Cards.
	 */
	private static Hands createRandomHands(int numCards) {
		Hands h = new Hands();
		Quene deck = createDeckCards(numCards);
		Cards c;
		
		for(int i = 0; i < numCards; i++) {
			int random = (int)(Math.random() * deck.size());
			c = (Cards)deck.get(random);
			deck.remove(random);
			
			if(i % 2 == 0) h.handOne.push(c);
			else h.handTwo.push(c);			
		}
		
		return h;
	}

	/***
	 * Separates the String of numbers from createDeckString by commas into individual Strings.
	 * Done to save memory as Cards are represented by Strings and not Card objects.
	 * @param str, String output from createDeckString or Strings with values separated by commas.
	 * @return an ArrayList of Strings with each index containing a different String.
	 */
	private static ArrayList<String> getNumsString(String str) {
		ArrayList<String> nums = new ArrayList<String>();

		for(int i = 0; i < str.length(); i = str.indexOf(",", i) + 1) {
			nums.add((str.substring(i, str.indexOf(",", i))));
		}
		
		return nums;
	}
	/***
	 * Separates the String of numbers from createDeckString by commas into individual Integers.
	 * Done to save memory as Cards are represented by Strings and not Card objects.
	 * @param str, String output from createDeckString or Strings with numbers separated by commas.
	 * @return an ArrayList of Strings with each index containing a different Integer.
	 */
	private static ArrayList<Integer> getNumsInt(String str) {
		ArrayList<Integer> nums = new ArrayList<Integer>();

		for(int i = 0; i < str.length(); i = str.indexOf(",", i) + 1) {
			nums.add(Integer.parseInt((str.substring(i, str.indexOf(",", i)))));
		}
		
		return nums;
	}
	/***
	 * Inserts the given letter into all possible locations of the given word, saving results into an ArrayList.
	 * @param word, the given word to insert the letter into.
	 * @param letter, the letter to insert.
	 * @return an ArrayList of Strings with every possible insertion of a letter into a word.
	 */
	private static ArrayList<String> insertLetter(String word, String letter){
		ArrayList<String> output = new ArrayList<String>();
		
		for(int loc = 0; loc < word.length(); loc++) {
			output.add(word.substring(0, loc) + letter + word.substring(loc, word.length()));
		}
		
		output.add(word + letter);
		
		
		return output;
	}
	/***
	 * Finds all possible permutations of a given ArrayList of Strings.
	 * As the input should specifically come from getNumsString, the method finds all the possible permutations of
	 * the numbers of the ArrayList.
	 * @param str, ArrayList String output from getNumsString.
	 * @return an ArrayList of String with every permutation of the given numbers.
	 */
	private static ArrayList<String> getPermsString(ArrayList<String> str) {
		ArrayList<String> output = new ArrayList<String>();
		
		if(str.size() == 1) {
			output.add(str.get(0));
			return output;
		}
		
		String first = str.get(0);
		ArrayList<String> rest = str;
		rest.remove(0);
		
		ArrayList<String> perms = getPermsString(rest);

		for(int i = 0; i < perms.size(); i++) {
			ArrayList<String> newPerms = insertLetter(perms.get(i), first);
			output.addAll(newPerms);
		}

		return output;
	}	

	/***
	 * Splits the Cards between both Hands given a String of numbers separated by commas, numbers which represent cards.
	 * As the method should be applied onto all elements from getPermString, we should consequently get all the possible
	 * different combinations of Hands and Cards.
	 * @param str, a single element from getPermString.
	 * @return Hands with Cards split between them.
	 */
	private static Hands splitCards(String str) {
		Hands h = new Hands();
		Cards c = new Cards();
		
		for(int i = 0; i < str.length(); i++) {
			c = Cards.createCard(Integer.parseInt(str.substring(i, i+1)));
			
			if(c.getCardValue() == str.length())
				c.setLetterValue(0);
			
			if(i % 2 == 0) h.handOne.push(c);
			else h.handTwo.push(c);
		}
		
		return h;
	}
	/***
	 * Creates a set of duplicate Hands given an original.
	 * @param original, the Hands to be copied.
	 * @return a set of duplicate Hands.
	 */
	private Hands cloneHand(Hands original) {
		Hands copy = new Hands();
		Cards c;
		
		for(int i = 0; i < original.handOne.size(); i++) { // loop through the original 1st hand
			int cardValue = original.handOne.get(i).getCardValue();
			int letterValue = original.handOne.get(i).getLetterValue();
			
			c = Cards.createCard(cardValue, letterValue);
			copy.handOne.push(c);
		}
		for(int i = 0; i < original.handTwo.size(); i++) { // loop through the original 2nd hand
			int cardValue = original.handTwo.get(i).getCardValue();
			int letterValue = original.handTwo.get(i).getLetterValue();
			
			c = Cards.createCard(cardValue, letterValue);
			copy.handTwo.push(c);
		}
		
		return copy;
	}

	/***
	 * Runs multiple Helper Methods together in order to get all possible sets of Hands.
	 * createDeckString 	- Based on the number of cards, create a String of numbers separated by commas.
	 * getNumsString 	- Separate those numbers into individual elements for an ArrayList String.
	 * getPermsString 	- From the ArrayList String of numbers, find and return all possible permutations as ArrayList String.
	 * getPermsCards		- Given every permutation as Strings, find all possible sets of Hands.
	 * @param numCards
	 * @return
	 */
	public static Quene<Hands> getPermsCards(int numCards){
		return getPermsCards(getPermsString(getNumsString(createDeckString(numCards))));
	}
	/***
	 * Finds all possible sets of Hands.
	 * As the input should specifically come from getPermsString which finds all possible permutations of a given ArrayList of String,
	 * by splitting all the possible permutations we are also finding all possible sets that our Hands' Cards can be in.
	 * @param str, ArrayList String output from getPermsString
	 * @return a Quene of Hands with every possible sets of Hands.
	 */
	private static Quene<Hands> getPermsCards(ArrayList<String> str) {
		Quene<Hands> output = new Quene<Hands>();

		for(int s = 0; s < str.size(); s++) {
			output.push(splitCards(str.get(s)));
		}
		
		return output;
	}
	
	// === To String Methods ===
	/***
	 * Formats String to display the Card Values of all cards in each Hand.
	 * Card Values represent the number on the Card.
	 * @return String of numbers
	 */
  	public String toCardValue() {
		String queneOne = handOne.toCardValue();
		String queneTwo = handTwo.toCardValue();
		
		return "Hand One: " + queneOne + "\nHand Two: " + queneTwo + "\n";
	}
  	/***
  	 * Formats String to display the Letter Values of all cards in each Hand.
  	 * Letter Values represent the catagories in which the Cards fall under where 0 beats 1, 1 beats 2, etc.
  	 * If the Letter Value is unknown, the Letter Value is = -1.
  	 * @return String of numbers
  	 */
	public String toLetterValue() {
		String queneOne = handOne.toLetterValue();
		String queneTwo = handTwo.toLetterValue();
		
		return "Hand One: " + queneOne + "\nHand Two: " + queneTwo + "\n";
	}
	/***
	 * Formats String to display the Letter of all cards in each Hand.
	 * Letters represent the catagories in which the Cards fall under where A beats B, B beats C, etc.
	 * If the Letter is unknown, the Letter is "-".
	 * @return String of letters.
	 */
	public String toLetter() {
		String queneOne = handOne.toLetter();
		String queneTwo = handTwo.toLetter();
		
		return "Hand One: " + queneOne + "\nHand Two: " + queneTwo + "\n";
	}
}
