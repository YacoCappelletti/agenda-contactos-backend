package agenda.repository;

import agenda.entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository <Contacto, Integer> {
}
