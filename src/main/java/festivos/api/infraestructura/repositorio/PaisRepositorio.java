package festivos.api.infraestructura.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import festivos.api.dominio.entidad.Pais;

@Repository
public interface PaisRepositorio extends JpaRepository<Pais, Long> {
}