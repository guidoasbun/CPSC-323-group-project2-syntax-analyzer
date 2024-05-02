import java.util.HashMap;
import java.util.Map;

public class ParserState {
    Map<String, String> actions;
    Map<String, Integer> goTo;

    public ParserState() {
        actions = new HashMap<>();
        goTo = new HashMap<>();
    }

    public void setAction(String token, String action) {
        actions.put(token, action);
    }

    public void setGoto(String nonTerminal, int state) {
        goTo.put(nonTerminal, state);
    }

    public String getAction(String token) {
        return actions.getOrDefault(token, "err");
    }

    public Integer getGoto(String nonTerminal) {
        return goTo.get(nonTerminal);
    }

}
