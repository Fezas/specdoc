package mo.specdoc.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtilBD {
    //private static final Logger logger = LogManager.getLogger();
    private static final Properties PROPERTIES = new Properties();
    static {
        loadPropertiesDB();
    }


    private PropertiesUtilBD() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Функция загрузки настроек
     */
    private static void loadPropertiesDB() {
        try (var inputStream = PropertiesUtilBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            //logger.error("Error", e);
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}