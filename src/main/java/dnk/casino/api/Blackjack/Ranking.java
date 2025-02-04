package dnk.casino.api.Blackjack;

import java.util.ArrayList;
import java.util.List;

import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

/**
 * Representa un ranking de usuarios en el juego de Blackjack.
 * 
 * @author Danikileitor
 */
public class Ranking {

    /**
     * Lista de usuarios en el ranking.
     */
    private List<Usuario> usuarios;
    /**
     * Lista de victorias de cada usuario en el ranking.
     */
    private List<Integer> victorias;

    /**
     * Constructor que inicializa el ranking con una lista de usuarios y un servicio
     * de usuarios.
     * 
     * @param usuarios       la lista de usuarios
     * @param usuarioService el servicio de usuarios
     */
    public Ranking(List<Usuario> usuarios, UsuarioService usuarioService) {
        this.usuarios = usuarios;

        List<Integer> victorias = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            victorias.add(usuarioService.getBjwins(usuario.getId()));
        }

        this.victorias = victorias;
    }

    /**
     * Obtiene la lista de victorias de cada usuario en el ranking.
     * 
     * @return la lista de victorias
     */
    public List<Integer> getVictorias() {
        return victorias;
    }

    /**
     * Obtiene la lista de usuarios en el ranking.
     * 
     * @return la lista de usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}
