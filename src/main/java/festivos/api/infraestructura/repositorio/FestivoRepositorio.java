package festivos.api.infraestructura.repositorio;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import festivos.api.dominio.entidad.Festivo;

@Repository
public interface FestivoRepositorio extends JpaRepository<Festivo, Long> {

    @Query("SELECT f FROM Festivo f WHERE f.pais.id = :idPais")
    List<Festivo> obtenerPorPais(@Param("idPais") Long idPais);
}