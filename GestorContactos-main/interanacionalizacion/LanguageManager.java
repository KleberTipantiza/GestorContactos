package interanacionalizacion;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static Locale locale = new Locale("es"); // Español por defecto

    public static void setLocale(String lang) {
        locale = new Locale(lang);
    }

    public static String getString(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("interanacionalizacion.Messages", locale);
        return bundle.getString(key);
    }
    
    public static String getOriginalString(String translatedCategory) {
        switch (translatedCategory) {
            case "Family":
            case "Famille":
                return "Familia";

            case "Friends":
            case "Amis":
                return "Amigos";

            case "Work":
            case "Travail":
                return "Trabajo";

            default:
                return translatedCategory; // Retorna el mismo valor si no encuentra coincidencia
        }
    }

}
