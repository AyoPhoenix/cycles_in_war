package projects.cyclesInWar;

public class Quene <E> {
	private Object[] list;
	private int freeLoc;
	
	public Quene() {
		list = new Object[1];
		freeLoc = 0;
	}
	
	public Quene<E> createQuene() {
		Object q = new Quene<E>();
		return (Quene<E>) q;
	}
	
	public void push(Object item) {		
		if(freeLoc >= list.length) {
			Object[] newList = new Object[list.length * 2];
			
			for(int i = 0; i < list.length; i++) {
				newList[i] = list[i];
			}
			
			list = newList;
		}
			
		list[freeLoc] = item;
		freeLoc++;
	}
	public E pop() {
		Object[] newList = new Object[list.length];
		Object item = list[0];
		freeLoc--;
		
		for(int i = 1; i < list.length; i++) {
			newList[i - 1] = list[i];
		}
		list = newList;
		
		return(E)item;
	}
	
	public void remove(int loc) {
		Object[] newList = new Object[list.length];
		freeLoc--;
		
		for(int i = 0; i < list.length - 1; i++) {
			if(i < loc) newList[i] = list[i];
			else newList[i] = list[i + 1];
		}
		
		list = newList;
	}
	public E get(int loc) {
		return (E)list[loc];
	}
	
	public Quene<E> subQuene(int beginIndex, int endIndex){
		Quene output = new Quene();
		
		for(int i = beginIndex; i < endIndex; i++) {
			output.push(list[i]);
		}
		
		return output;
	}
	
	public int size() {
		// returns the current number of cards in the hand;
		return freeLoc;
	}
	
	public String toCardValue() {
		String quene = "";
		Cards c;
		
		for(int i = 0; i < freeLoc; i++) {
			c = (Cards) list[i];
			quene += c.getCardValue() + ", ";
		}
		
		return quene;
	}
	public String toLetterValue() {
		String quene = "";
		Cards c;
		
		for(int i = 0; i < freeLoc; i++) {
			c = (Cards) list[i];
			quene += c.getLetterValue() + ", ";
		}
		
		return quene;
	}
	public String toLetter() {
		String quene = "";
		Cards c;
		
		for(int i = 0; i < freeLoc; i++) {
			c = (Cards) list[i];
			quene += c.getLetter(c.getLetterValue()) + ", ";
		}
		
		return quene;
	}
	
}
