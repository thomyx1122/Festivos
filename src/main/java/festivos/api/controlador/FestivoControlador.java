package festivos.api.controlador;

import festivos.api.core.servicios.FestivoServicio;
import festivos.api.dominio.dto.FestivoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendario")
public class FestivoControlador {

    @Autowired
    private FestivoServicio festivoServicio;

    // 1. Listar los festivos de un país y año
    @GetMapping("/festivos/{idPais}/{anio}")
    public List<FestivoDTO> obtenerFestivos(@PathVariable Long idPais, @PathVariable int anio) {
        return festivoServicio.obtenerFestivosPorAnio(idPais, anio);
    }

    // 2. Validar si una fecha específica es festiva
    @GetMapping("/verificar/{idPais}/{anio}/{mes}/{dia}")
    public String verificarFecha(@PathVariable Long idPais, @PathVariable int anio, @PathVariable int mes, @PathVariable int dia) {
        return festivoServicio.verificarFecha(idPais, anio, mes, dia);
    }
}