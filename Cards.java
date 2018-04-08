package projects.cyclesInWar;

public class Cards implements Comparable<Cards>{
		private int cardValue;
		private int letterValue;
		private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		private Cards(int cardValue, int letterValue) {
			this.cardValue = cardValue;
			this.letterValue = letterValue;
		}
		private Cards(int cardValue) {
			this.cardValue = cardValue;
			this.letterValue = -1;
		}
		public Cards() {
			cardValue = -1;
			letterValue = -1;
		}
		
		public static Cards createCard(int cardValue, int letterValue) {
			Cards c = new Cards(cardValue, letterValue);
			return c;
		}
		public static Cards createCard(int cardValue) {
			Cards c = new Cards(cardValue);
			return c;
		}
		
		// Getter/Setter Methods
		public int getCardValue() {
			return cardValue;
		}
		public void setCardValue(int cardValue) {
			this.cardValue = cardValue;
		}
		public int getLetterValue() {
			return letterValue;
		}
		public void setLetterValue(int letterValue) {
			this.letterValue = letterValue;
		}
		public String getLetter(int letterValue) {
			if(letterValue == -1) return "-";
			return alphabet.substring(letterValue, letterValue + 1);
		}
		
		private Cards cloneCard(Cards original) {
			Cards copy = new Cards(original.getCardValue(), original.getLetterValue());
			return copy;
		}
		
		public String toString() {
			return "Card Value: " + cardValue + ", Letter Value: " + letterValue;
		}
	
	@Override
	public int compareTo(Cards other) {
		// return positive if this card is larger than the other
		// will never return 0 since there are no duplicate cardValues
		return this.cardValue - other.cardValue;
	}

}
