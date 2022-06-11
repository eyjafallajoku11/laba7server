package utility.dataCommand;

import gorod.Government;
import utility.HashMapController;


import java.util.Iterator;
import java.util.Objects;

public class DataCommandRemoveAllByGovernment extends DataCommand {
    public String execute(String data, String u) {
        Government government;
        switch (data){
            case "ITMOCRACY":
                government=Government.ITMOCRACY;
                break;
            case "KLEPTOCRACY":
                government = Government.KLEPTOCRACY;
                break;
            case "MERITOCRACY":
                government = Government.MERITOCRACY;
                break;
            case "MONARCHY":
                government = Government.MONARCHY;
                break;
            case "TELLUROCRACY":
                government = Government.TELLUROCRACY;
                break;
            default:
                return ("Такого типа государства не знаю, есть только ITMOCRACY, KLEPTOCRACY, MERITOCRACY, MONARCHY, TELLUROCRACY)");
        }
        int count = 0;
        Iterator<Long> it = HashMapController.getMap().keySet().iterator();
        while (it.hasNext()) {
            long key = it.next();
            if (HashMapController.get_by_ID(key).getGovernment().equals(government) && HashMapController.get_by_ID(key).getUsername().equals(u)) {
                it.remove();
                count++;
            }
        }
        return "удалено " + count + " городов";
    }
    public static boolean isPublic = false;

}
