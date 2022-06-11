package utility.creatorCommand;

import utility.CityCreator;
import utility.CommandManager;
import utility.Request;

public class CreatorCommandUpdate extends CreatorCommand {
    public String execute(String[] creatorData, String u) {
        CommandManager.execute(new Request( "remove_by_key",creatorData[12]),u);
        CommandManager.execute(new Request("insert", creatorData),u);
        return ("город обновлён");
    }
    public static boolean isPublic = false;

}
