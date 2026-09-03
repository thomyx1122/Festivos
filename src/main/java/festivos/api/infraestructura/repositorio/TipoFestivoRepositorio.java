package festivos.api.infraestructura.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import festivos.api.dominio.entidad.TipoFestivo;

@Repository
public interface TipoFestivoRepositorio extends JpaRepository<TipoFestivo, Long> {
}