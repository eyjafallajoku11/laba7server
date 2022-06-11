package utility.dataCommand;

import utility.HashMapController;
import utility.SQLmanager;

public class DataCommandRemoveKey extends DataCommand {
    public String execute (String data, String u) {
        try {
            Long key = Long.parseLong(data);
            if (HashMapController.get_by_ID(key).getUsername().equals(u)) {
                HashMapController.remove_by_ID(key);
                SQLmanager.remove(key);
                return ("канселим город с ID=" + data);
            }
            return "не твой город";
        }
        catch (NumberFormatException e) {
            return ("че с ключом, это цифра должна быть");
        }
        catch (Exception e){
            return ("совсем фигня какая-то");
        }
    }
    public static boolean isPublic = false;

}
