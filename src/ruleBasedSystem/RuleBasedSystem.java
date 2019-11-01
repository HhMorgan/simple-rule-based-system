package ruleBasedSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class RuleBasedSystem {
	private HashSet<HistoryElement> history;
	private boolean debug;

	public RuleBasedSystem(boolean debug) {
		history = new HashSet<HistoryElement>();
		this.debug = debug;
	}

	private ArrayList<Rule> readFile(String filename) {
		ArrayList<Rule> program = new ArrayList<Rule>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			boolean condition = false;
			boolean positiveLiterals = false;
			boolean negativeLiterals = false;
			ArrayList<String> precondition = new ArrayList<String>();
			ArrayList<String> consequencePositive = new ArrayList<String>();
			ArrayList<String> consequenceNegative = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("IF")) {
					if (positiveLiterals | negativeLiterals) {
						program.add(new Rule(precondition, consequencePositive, consequenceNegative));
					}
					condition = true;
					positiveLiterals = false;
					negativeLiterals = false;
					precondition = new ArrayList<String>();
				} else {
					if (line.equals("THEN")) {
						condition = false;
						positiveLiterals = true;
						negativeLiterals = false;
						consequencePositive = new ArrayList<String>();
					} else {
						if (line.equals("REMOVE")) {
							condition = false;
							positiveLiterals = false;
							negativeLiterals = true;
							consequenceNegative = new ArrayList<String>();
						} else {
							if (condition) {
								precondition.add(line.replaceAll(",", ""));
							} else {
								if (positiveLiterals) {
									consequencePositive.add(line.replaceAll(",", ""));
								} else {
									if (negativeLiterals) {
										consequenceNegative.add(line.replaceAll(",", ""));
									}
								}
							}
						}
					}
				}

			}
			if (positiveLiterals | negativeLiterals) {
				program.add(new Rule(precondition, consequencePositive, consequenceNegative));
			}
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
		}
		return program;
	}

	private ArrayList<Fact> input() {
		String input = "";
		Scanner scanner = new Scanner(System.in);
		System.out.print("?-");
		boolean initialInput = true;
		while (!input.contains(".")) {
			if (!initialInput) {
				System.out.print("|   ");
			}
			input += scanner.next();
			if (initialInput) {
				initialInput = false;
			}
		}
		if (input.charAt(input.length() - 1) != '.') {
			System.out.println("Invalid Syntax!");
		}
		ArrayList<Fact> facts = new ArrayList<Fact>();
		for (String s : (input.replaceAll("\\.", "")).split(",")) {
			facts.add(new Fact(s));
		}
		return facts;
	}

	private ArrayList<HistoryElement> match(ArrayList<Rule> program, ArrayList<Fact> facts) {
		ArrayList<HistoryElement> matched = new ArrayList<HistoryElement>();
		for (Rule r : program) {
			ArrayList<Integer> matchedIndices = new ArrayList<Integer>();
			ArrayList<String> rulePrecondition = r.getPrecondition();
			for (int i = 0; i < rulePrecondition.size(); i++) {
				for (int j = 0; j < facts.size(); j++) {
					if (facts.get(j).getFact().equals(rulePrecondition.get(i))) {
						matchedIndices.add(facts.get(j).getID());
						break;
					}
				}
			}
			if (matchedIndices.size() == rulePrecondition.size()) {
				HistoryElement element = new HistoryElement(r, matchedIndices);
				if (!history.contains(element)) {
					matched.add(element);
				}
			}
		}
		if (debug) {
		System.out.println(Arrays.toString(matched.toArray()));
		}
		return matched;

	}

	private HistoryElement resolve(ArrayList<HistoryElement> matched) {
		HistoryElement firedElement = null;
		if (matched.size() > 1) {
			int max = 0;
			for (HistoryElement e : matched) {
				ArrayList<Integer> matchedIndicesOfElement = e.getMatchedFacts();
				int matchedRecent = Collections.max(matchedIndicesOfElement);
				if (matchedRecent > max) {
					max = matchedRecent;
					firedElement = e;
				}
			}
		} else {
			if (matched.size() == 1) {
				firedElement = matched.get(0);
			}
		}
		history.add(firedElement);

		return firedElement;
	}

	private void fire(ArrayList<Fact> facts, HistoryElement firedElement) {
		Rule fired = firedElement.getRule();
		if (debug) {
		System.out.println(fired);
		}

		for (String s : fired.getConsequencePositive()) {
			facts.add(new Fact(s));
		}
		for (String s : fired.getConsequenceNegative()) {
			firedElement.getMatchedFacts();
			for (int i = 0; i < facts.size(); i++) {
				if (facts.get(i).getFact().equals(s)) {
					if (firedElement.getMatchedFacts().contains(facts.get(i).getID())) {
						facts.remove(facts.get(i));
					}
				}
			}
		}
		if (debug) {
			System.out.println(Arrays.toString(facts.toArray()));
			System.out.println("-------------");
		}
	}

	public void invokeSystem() {
		ArrayList<Rule> program = readFile("rule.txt");
		ArrayList<Fact> facts = input();
		if (debug) {
			System.out.println(Arrays.toString(facts.toArray()));
			for (Rule r : program) {
				System.out.println(r);
			}

			System.out.println("===============");
		}
		while (true) {
			ArrayList<HistoryElement> matched = match(program, facts);
			if (matched.size() == 0) {
				break;
			}
			HistoryElement firedElement = resolve(matched);
			fire(facts, firedElement);
		}
		System.out.println((Arrays.toString(facts.toArray()).replaceAll("\\[", "")).replaceAll("\\]", ""));
	}
}
