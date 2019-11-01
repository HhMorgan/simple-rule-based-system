package ruleBasedSystem;

public class Fact {
	private String fact;
	private int id;
	static int idSoFar;
	public Fact(String fact) {
		this.fact = fact;
		this.id = idSoFar;
		idSoFar++;
	}
	public String getFact() {
		return fact;
	}
	public int getID() {
		return id;
	}
	public boolean equals(Object o) { 
		Fact otherFact = (Fact) o;
		return id == otherFact.getID();
	}
	public String toString() {
		return fact;
	}
}
