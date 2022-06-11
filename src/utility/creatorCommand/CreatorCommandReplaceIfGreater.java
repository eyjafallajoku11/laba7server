package utility.creatorCommand;


import utility.CityCreator;
import utility.CommandManager;
import utility.HashMapController;
import utility.Request;

public class CreatorCommandReplaceIfGreater extends CreatorCommand {
    public String execute(String[] creatorData, String u) {
        CityCreator creator = new CityCreator(creatorData, u);
        try {
            String id = creatorData[12];
            if (!HashMapController.contains_ID(Long.parseLong(id))) {
                CommandManager.execute(new Request("insert",creatorData),u);
                return "город добавлен";
            } else {
                try{
                    creator.create_city();
                    Long key = Long.parseLong(id);
                    if (creator.getCity().compareTo(HashMapController.get_by_ID(key)) > 0 && HashMapController.get_by_ID(key).getUsername().equals(u)) {
                        CommandManager.execute(new Request("remove_by_key", id),u);
                        creator.add_city_to_map();
                        return "город заменем";
                    }
                    return "город не заменен";
                } catch (NullPointerException e) {
                    return ("не хватает обязательных переменных, город не создан");
                }
                catch (Exception e){
                    return ("ошибка в формате данных, город не создан");
                }
            }
        }
        catch (NumberFormatException e) {
            return ("че с ключом, это цифра должна быть");
        }
        catch (Exception e){
            return ("вообще всё плохо");
        }
    }
    public static boolean isPublic = false;

}
