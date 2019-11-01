package ruleBasedSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Rule {
	
	private ArrayList<String> precondition;
	private ArrayList<String> consequencePositive;
	private ArrayList<String> consequenceNegative;
	
	public Rule(ArrayList<String> precondition, ArrayList<String> consequenceAdd, ArrayList<String> consequenceRemove) {
		this.precondition = precondition;
		this.consequencePositive = consequenceAdd;
		this.consequenceNegative = consequenceRemove;
	}

	public ArrayList<String> getPrecondition() {
		return precondition;
	}

	public ArrayList<String> getConsequencePositive() {
		return consequencePositive;
	}

	public ArrayList<String> getConsequenceNegative() {
		return consequenceNegative;
	}
	
	public String toString() {
		return Arrays.toString(precondition.toArray()) + ","+Arrays.toString(consequencePositive.toArray())  +","+Arrays.toString(consequenceNegative.toArray()) ;
	}
	
	public boolean equals(Object o) { 
		Rule otherRule = (Rule) o;
		for(String c : precondition) {
			if(!otherRule.getPrecondition().contains(c)) {
				return false;
			}
		}
		for(String c : consequencePositive) {
			if(!otherRule.getConsequencePositive().contains(c)) {
				return false;
			}
		}
		for(String c : consequenceNegative) {
			if(!otherRule.getConsequenceNegative().contains(c)) {
				return false;
			}
		}
		return true;
	}
	public int hashCode() {
		return Objects.hash(precondition, consequencePositive,consequenceNegative);
    }
}
