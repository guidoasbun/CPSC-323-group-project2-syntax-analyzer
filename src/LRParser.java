import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LRParser {
    private ParserState[] states;

    public LRParser() {
        initializeStates();
    }

    private void initializeStates() {
        states = new ParserState[12];
        for (int i = 0; i < states.length; i++) {
            states[i] = new ParserState();
        }

        // Initialize state transitions and actions based on LR table provided
        // States initialization as described earlier in your setup

        // Populating the states with actions and goTo based on the provided LR table
        states[0].actions.put("id", "S5");
        states[0].actions.put("(", "S4");
        states[0].goTo.put("E", 1);
        states[0].goTo.put("T", 2);
        states[0].goTo.put("F", 3);

        states[1].actions.put("+", "S6");
        states[1].actions.put("$", "acc");

        states[2].actions.put("+", "R2");
        states[2].actions.put("*", "S7");
        states[2].actions.put(")", "R2");
        states[2].actions.put("$", "R2");

        states[3].actions.put("+", "R4");
        states[3].actions.put("*", "R4");
        states[3].actions.put(")", "R4");
        states[3].actions.put("$", "R4");

        states[4].actions.put("id", "S5");
        states[4].actions.put("(", "S4");
        states[4].goTo.put("E", 8);
        states[4].goTo.put("T", 2);
        states[4].goTo.put("F", 3);

        states[5].actions.put("+", "R6");
        states[5].actions.put("*", "R6");
        states[5].actions.put(")", "R6");
        states[5].actions.put("$", "R6");

        states[6].actions.put("id", "S5");
        states[6].actions.put("(", "S4");
        states[6].goTo.put("T", 9);
        states[6].goTo.put("F", 3);

        states[7].actions.put("id", "S5");
        states[7].actions.put("(", "S4");
        states[7].goTo.put("F", 10);

        states[8].actions.put("+", "S6");
        states[8].actions.put(")", "S11");

        states[9].actions.put("+", "R1");
        states[9].actions.put("*", "S7");
        states[9].actions.put(")", "R1");
        states[9].actions.put("$", "R1");

        states[10].actions.put("+", "R3");
        states[10].actions.put("*", "R3");
        states[10].actions.put(")", "R3");
        states[10].actions.put("$", "R3");

        states[11].actions.put("+", "R5");
        states[11].actions.put("*", "R5");
        states[11].actions.put(")", "R5");
        states[11].actions.put("$", "R5");
    }

    private List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == ' ') {
                continue; // Skip spaces
            }

            if ( ch == '(' || ch == '+' || ch == '*' || ch == ')' || ch == '$') {
                if (!token.isEmpty()) {
                    if (token.toString().equals("id")) {
                        tokens.add(token.toString());
                        token = new StringBuilder();
                    } else {
                        throw new IllegalArgumentException("Invalid token: " + token);
                    }
                }
                tokens.add(String.valueOf(ch));
            } else if (Character.isLetterOrDigit(ch)) {
                token.append(ch);
            } else {
                throw new IllegalArgumentException("Invalid character: " + ch);
            }
        }

        if (!token.isEmpty()) {
            if (token.toString().equals("id")) {
                tokens.add(token.toString());
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        return tokens;
    }

    public void parseString(String input) {
        try {
            List<String> tokens = tokenize(input);
            performParsing(tokens);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void performParsing(List<String> tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);

        int index = 0;
        int step = 1;

        System.out.println("Step    |     Stack    |     Input    |     Action");
        System.out.println("-------------------------------------------");

        while (index < tokens.size()) {
            int state = stack.peek();
            String token = tokens.get(index);
            String action = states[state].getAction(token);

            System.out.printf("%-8d|%-15s|%-13s|%-14s%n", step++, stack.toString(), token, action);

            if (action.equals("acc")) {
                System.out.printf("%-8d|%-15s|%-13s|%-14s%n", step, stack.toString(), " ", "Accept");
                return;
            } else if (action.startsWith("S")) {
                Integer newState = Integer.parseInt(action.substring(1));
                stack.push(newState);
                index++;
            } else if (action.startsWith("R")) {
                handleReduction(stack, action);
            } else {
                System.out.printf("%-8d|%-15s|%-13s|%-14s%n", step, stack.toString(), " ", "Error: Not Accepted");
                return;
            }
        }
    }

    private void handleReduction(Deque<Integer> stack, String action) {
        // Determine which production rule the reduction corresponds to
        switch (action) {
            case "R1": // E → E + T
                stack.pop(); stack.pop(); stack.pop();
                break;
            case "R2": // E → T
            case "R4": // T → F
            case "R6": // F → id
                stack.pop();
                break;
            case "R3": // T → T * F
            case "R5": // F → (E)
                stack.pop(); stack.pop(); stack.pop();
                break;
        }

        // Use the GOTO table of the state now on top of the stack to find the next state
        String nonTerminal = getNonTerminalForProduction(action);
        Integer gotoState = states[stack.peek()].getGoto(nonTerminal);
        stack.push(gotoState);
    }

    private String getNonTerminalForProduction(String action) {
        return switch (action) {
            case "R1", "R2" -> "E";
            case "R3", "R4" -> "T";
            case "R5", "R6" -> "F";
            default -> null;
        };
    }
}
