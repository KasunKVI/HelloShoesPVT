package software.kasunkavinda.util;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Component
public class UtilMatter {
    public static  String generateId(){
        return UUID.randomUUID().toString();
    }

    public static String convertBase64(String data){
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}
