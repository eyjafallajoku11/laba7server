package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Logins {
    private static final MessageDigest md;
    static {
        try {
            md = MessageDigest.getInstance("SHA-384");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static HashMap<String, String> users = new HashMap<>();
    public static void addToList(String login, String password){
        users.put(login, password);
    }
    public static boolean containsLogin(String login){
        return users.containsKey(login);
    }
    public static boolean authorise(String login, String password){
        return (users.containsKey(login) && users.get(login).equals(password));
    }
}
