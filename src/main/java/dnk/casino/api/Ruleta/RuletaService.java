package dnk.casino.api.Ruleta;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dnk.casino.Users.Usuario;

@Service
public class RuletaService {
    @Autowired
    private RuletaRepository ruletaRepository;

    public Ruleta crearRuleta() {
        Ruleta ruleta = new Ruleta();
        ruletaRepository.save(ruleta);
        return ruleta;
    }

    public Ruleta obtenerRuleta(String id) {
        return ruletaRepository.findById(id).orElse(null);
    }

    public Ruleta apostar(String ruletaId, Usuario usuario, Apuesta apuesta) {
        Ruleta ruleta = obtenerRuleta(ruletaId);
        if (ruleta != null && ruleta.isRuletaAbierta()) {
            Apostador apostador = ruleta.getApostadores().stream()
                    .filter(a -> a.getId().equals(usuario.getId()))
                    .findFirst()
                    .orElse(null);
            if (apostador == null) {
                if (usuario != null) {
                    apostador = new Apostador(usuario.getId(), usuario.getUsername(), ruletaId);
                }
            } else {
                ruleta.getApostadores().remove(apostador);
            }
            apostador.apostar(apuesta);
            ruleta.getApostadores().add(apostador);
            return ruletaRepository.save(ruleta);
        } else {
            return ruleta;
        }
    }

    public void cerrarRuleta(String id) {
        Ruleta ruleta = obtenerRuleta(id);
        if (ruleta != null) {
            ruleta.setRuletaAbierta(false);
            ruletaRepository.save(ruleta);
        }
    }

    public void girarRuleta(String id) {
        Ruleta ruleta = obtenerRuleta(id);
        if (ruleta != null && !ruleta.isRuletaAbierta()) {
            int numeroGanador = (int) (Math.random() * 36);
            ruleta.setNumeroGanador(numeroGanador);
            ruletaRepository.save(ruleta);
        }
    }

    public List<Ruleta> getAllRuletas() {
        return ruletaRepository.findAll();
    }
}
