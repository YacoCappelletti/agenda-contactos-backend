package agenda.security.repository;

import agenda.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findById(int idUsuario);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
