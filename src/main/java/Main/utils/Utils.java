package Main.utils;

import java.util.UUID;

public class Utils {
    public static String createIBAN() {
        UUID randomUUID = UUID.randomUUID();
        String random = randomUUID.toString().replaceAll("-", "");
        return random.substring(0,24);
    }
}
