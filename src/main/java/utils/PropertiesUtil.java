package utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import static exception.ErrorMessages.DataBaseError.CONFIGURATION_LOAD_ERROR;

public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    static {
        logger.info("Loading properties file");
        loadProperties();
    }
    private PropertiesUtil() {
    }
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("db.properties");) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            logger.error(CONFIGURATION_LOAD_ERROR, e);
            throw new RuntimeException(CONFIGURATION_LOAD_ERROR);
        }
    }
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}