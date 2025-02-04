package dnk.casino.api.Blackjack;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de juegos de Blackjack.
 * 
 * @author Danikileitor
 */
@Repository
public interface JuegoRepository extends MongoRepository<Juego, String> {

    /**
     * Busca un juego por su ID.
     * 
     * @param id el ID del juego a buscar
     * @return el juego encontrado, o un Optional vacío si no se encuentra
     */
    Optional<Juego> findById(String id);

    /**
     * Busca el último juego activo de un jugador.
     * 
     * @param idJugador el ID del jugador
     * @return el juego encontrado, o un Optional vacío si no se encuentra
     */
    @Query(value = "{idJugador: ?0, activo: true}", sort = "{_id: -1}")
    Optional<Juego> findLastActiveJuegoByJugador(String idJugador);
}
