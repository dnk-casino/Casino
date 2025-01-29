package dnk.casino.api.Tragaperras;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class TragaperrasService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveResult(String collectionName, TragaperrasController.SlotMachineResult result) {
        mongoTemplate.save(result, collectionName);
    }

    public long getCountByMessage(String collectionName, String message) {
        return mongoTemplate.getCollection(collectionName)
                .countDocuments(new Document("message", message));
    }

    public int getNextAttemptNumber(String collectionName) {
        return (int) mongoTemplate.getCollection(collectionName).countDocuments() + 1;
    }
}