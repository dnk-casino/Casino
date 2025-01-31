package dnk.casino.api.Ruleta;

import java.util.List;
import java.util.Optional;

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

    public Optional<Ruleta> findById(String id) {
        return ruletaRepository.findById(id);
    }

    public Ruleta apostar(Ruleta ruleta, Usuario usuario, Apuesta apuesta) {
        Apostador apostador = ruleta.getApostadores().stream()
                .filter(a -> a.getId().equals(usuario.getId()))
                .findFirst()
                .orElse(null);
        if (apostador != null) {
            ruleta.eliminarApostador(apostador);
        } else {
            apostador = new Apostador(usuario.getId(), usuario.getUsername(), ruleta.getId());
        }
        apostador.apostar(apuesta);
        ruleta.addApostador(apostador);
        return ruletaRepository.save(ruleta);
    }

    public Ruleta cerrarRuleta(Ruleta ruleta) {
        ruleta.setRuletaAbierta(false);
        return ruletaRepository.save(ruleta);
    }

    public Ruleta girarRuleta(Ruleta ruleta) {
        int numeroGanador = (int) (Math.random() * 36);
        ruleta.setNumeroGanador(numeroGanador);
        return ruletaRepository.save(ruleta);

    }

    public List<Ruleta> getAllRuletas() {
        return ruletaRepository.findAll();
    }
}
