package dnk.casino.api.Ruleta;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RuletaRepository extends MongoRepository<Ruleta, String> {
    
    List<Ruleta> findByRuletaAbierta(boolean ruletaAbierta);
}
