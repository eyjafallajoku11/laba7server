package utility.bareCommand;

import utility.HashMapController;

import java.util.Iterator;

public class BareCommandClear extends BareCommand {
    public String execute(String login) {
        HashMapController.getMap().keySet().removeIf(key -> HashMapController.get_by_ID(key).getUsername().equals(login));
        return "коллекция очищена";
    }
    public final static boolean isPublic = false;
}
