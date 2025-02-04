package dnk.casino.api.Ruleta;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio de ruletas.
 * 
 * @author Danikileitor
 */
@Repository
public interface RuletaRepository extends MongoRepository<Ruleta, String> {

    /**
     * Busca ruletas por su estado de apertura.
     * 
     * @param ruletaAbierta true para buscar ruletas abiertas, false para buscar
     *                      ruletas cerradas
     * @return la lista de ruletas
     */
    List<Ruleta> findByRuletaAbierta(boolean ruletaAbierta);
}
