package ruleBasedSystem;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryElement {
	private Rule rule;
	private ArrayList<Integer> matchedFacts;
	public HistoryElement(Rule rule, ArrayList<Integer> matchedFacts) {
		this.rule = rule;
		this.matchedFacts = matchedFacts;
	}
	
	public Rule getRule() {
		return rule;
	}

	public ArrayList<Integer> getMatchedFacts() {
		return matchedFacts;
	}

	public boolean equals(Object o) { 
		HistoryElement otherHistoryElement = (HistoryElement) o;
		for(int i = 0; i <  matchedFacts.size(); i++) {
			if(matchedFacts.get(i)!=otherHistoryElement.getMatchedFacts().get(i)) {
				return false;
			}
		}
		return rule.equals(otherHistoryElement.getRule());
	}
	public String toString() {
		return rule.toString();
	}
	public int hashCode() {
		return Objects.hash(rule, matchedFacts);
    }
}
