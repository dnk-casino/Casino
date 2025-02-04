package dnk.casino.api.Ruleta;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dnk.casino.Users.Usuario;

/**
 * Servicio para la gestión de ruletas.
 * 
 * @author Danikileitor
 */
@Service
public class RuletaService {

    /**
     * Repositorio de ruletas.
     */
    @Autowired
    private RuletaRepository ruletaRepository;

    /**
     * Crea una nueva ruleta.
     * 
     * @return la ruleta creada
     */
    public Ruleta crearRuleta() {
        Ruleta ruleta = new Ruleta();
        ruletaRepository.save(ruleta);
        return ruleta;
    }

    /**
     * Busca una ruleta por su ID.
     * 
     * @param id el ID de la ruleta a buscar
     * @return la ruleta encontrada, o un Optional vacío si no se encuentra
     */
    public Optional<Ruleta> findById(String id) {
        return ruletaRepository.findById(id);
    }

    /**
     * Realiza una apuesta en una ruleta.
     * 
     * @param ruleta  la ruleta en la que se realiza la apuesta
     * @param usuario el usuario que realiza la apuesta
     * @param apuesta la apuesta a realizar
     * @return la ruleta actualizada
     */
    public Ruleta apostar(Ruleta ruleta, Usuario usuario, Apuesta apuesta) {
        Apostador apostador = ruleta.getApostadores().stream()
                .filter(a -> a.getId().equals(usuario.getId()))
                .findFirst()
                .orElse(null);
        if (apostador != null) {
            ruleta.eliminarApostador(apostador);
        } else {
            apostador = new Apostador(usuario.getId(), usuario.getUsername());
        }
        apostador.apostar(apuesta);
        ruleta.addApostador(apostador);
        return ruletaRepository.save(ruleta);
    }

    /**
     * Cierra una ruleta.
     * 
     * @param ruleta la ruleta a cerrar
     * @return la ruleta cerrada
     */
    public Ruleta cerrarRuleta(Ruleta ruleta) {
        ruleta.setRuletaAbierta(false);
        return ruletaRepository.save(ruleta);
    }

    /**
     * Gira una ruleta.
     * 
     * @param ruleta la ruleta a girar
     * @return la ruleta girada
     */
    public Ruleta girarRuleta(Ruleta ruleta) {
        ruleta.setRuletaAbierta(false);
        ruleta.girar();
        return ruletaRepository.save(ruleta);
    }

    /**
     * Obtiene los ganadores de una ruleta.
     * 
     * @param ruleta la ruleta de la que se obtienen los ganadores
     * @return la lista de ganadores
     */
    public List<Object[]> obtenerGanadores(Ruleta ruleta) {
        return ruleta.determinarGanadores();
    }

    /**
     * Obtiene todas las ruletas.
     * 
     * @return la lista de ruletas
     */
    public List<Ruleta> getAllRuletas() {
        return ruletaRepository.findAll();
    }

    /**
     * Obtiene todas las ruletas abiertas o cerradas según el parámetro.
     * 
     * @param abiertas true para obtener ruletas abiertas, false para obtener
     *                 ruletas cerradas
     * @return la lista de ruletas
     */
    public List<Ruleta> getAllRuletasAbiertas(boolean abiertas) {
        return ruletaRepository.findByRuletaAbierta(abiertas);
    }
}
