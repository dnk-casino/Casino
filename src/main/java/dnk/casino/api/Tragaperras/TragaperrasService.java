package dnk.casino.api.Tragaperras;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de resultados de la tragaperras.
 * 
 * @author Danikileitor
 */
@Service
public class TragaperrasService {

    /**
     * Plantilla de MongoDB.
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Guarda un resultado de la tragaperras en la base de datos.
     * 
     * @param collectionName el nombre de la colección
     * @param result         el resultado a guardar
     */
    public void saveResult(String collectionName, TragaperrasController.SlotMachineResult result) {
        mongoTemplate.save(result, collectionName);
    }

    /**
     * Obtiene el número de documentos en la colección que coinciden con un mensaje
     * específico.
     * 
     * @param collectionName el nombre de la colección
     * @param message        el mensaje a buscar
     * @return el número de documentos que coinciden con el mensaje
     */
    public long getCountByMessage(String collectionName, String message) {
        return mongoTemplate.getCollection(collectionName)
                .countDocuments(new Document("message", message));
    }

    /**
     * Obtiene el número de intentos siguientes en la colección.
     * 
     * @param collectionName el nombre de la colección
     * @return el número de intentos siguientes
     */
    public int getNextAttemptNumber(String collectionName) {
        return (int) mongoTemplate.getCollection(collectionName).countDocuments() + 1;
    }
}
