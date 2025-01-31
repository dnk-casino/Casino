package dnk.casino.api.Ruleta;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuletaRepository extends MongoRepository<Ruleta, String> {
}
