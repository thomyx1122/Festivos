package festivos.api.core.servicios;

import festivos.api.dominio.dto.FestivoDTO;
import festivos.api.dominio.entidad.Festivo;
import festivos.api.infraestructura.repositorio.FestivoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class FestivoServicio {

    @Autowired
    private FestivoRepositorio festivoRepositorio;

    // 1. Obtener la fecha del Domingo de Pascua para un año dado
    public LocalDate calcularDomingoPascua(int anio) {
        int a = anio % 19;
        int b = anio % 4;
        int c = anio % 7;
        int d = (19 * a + 24) % 30;
        int dias = d + (2 * b + 4 * c + 6 * d + 5) % 7;
        
        // El domingo de ramos es el 15 de marzo + 'dias'
        LocalDate domingoRamos = LocalDate.of(anio, 3, 15).plusDays(dias);
        // El domingo de pascua es 7 días después del domingo de ramos
        return domingoRamos.plusDays(7);
    }

    // 2. Calcular la fecha exacta de un festivo para un año específico
    public LocalDate calcularFechaFestiva(Festivo festivo, int anio) {
        LocalDate fecha = null;
        int tipo = festivo.getTipoFestivo().getId().intValue();
        switch (tipo) {
            case 1: // Fijo (No varía)
                try {
                    fecha = LocalDate.of(anio, festivo.getMes(), festivo.getDia());
                } catch (Exception e) {
                    return null;
                }
                break;

            case 2: // Ley de Puente Festivo (Se traslada al siguiente lunes)
                try {
                    fecha = LocalDate.of(anio, festivo.getMes(), festivo.getDia());
                    fecha = siguienteLunes(fecha);
                } catch (Exception e) {
                    return null;
                }
                break;

            case 3: // Basado en el domingo de pascua
                LocalDate pascua = calcularDomingoPascua(anio);
                fecha = pascua.plusDays(festivo.getDiasPascua());
                break;

            case 4: // Basado en pascua y ley de puente festivo
                LocalDate pascuaPuentin = calcularDomingoPascua(anio);
                LocalDate fechaPascua = pascuaPuentin.plusDays(festivo.getDiasPascua());
                fecha = siguienteLunes(fechaPascua);
                break;
        }
        return fecha;
    }

    // Método auxiliar para mover la fecha al siguiente lunes si no cae en lunes
    private LocalDate siguienteLunes(LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        if (diaSemana == DayOfWeek.MONDAY) {
            return fecha;
        }
        // Calcula cuántos días faltan para el próximo lunes
        int diasHastaLunes = (8 - diaSemana.getValue()) % 7;
        if (diasHastaLunes == 0) diasHastaLunes = 7;
        return fecha.plusDays(diasHastaLunes);
    }

    // 3. Listar todos los festivos de un país en un año
    public List<FestivoDTO> obtenerFestivosPorAnio(Long idPais, int anio) {
        List<Festivo> festivos = festivoRepositorio.obtenerPorPais(idPais);
        java.util.List<FestivoDTO> listaDTO = new java.util.ArrayList<>();

        for (Festivo f : festivos) {
            LocalDate fechaCalculada = calcularFechaFestiva(f, anio);
            if (fechaCalculada != null) {
                listaDTO.add(new FestivoDTO(f.getNombre(), fechaCalculada));
            }
        }
        return listaDTO;
    }

    // 4. Validar si una fecha específica es festiva
    public String verificarFecha(Long idPais, int anio, int mes, int dia) {
        try {
            LocalDate fechaValidar = LocalDate.of(anio, mes, dia);
            List<FestivoDTO> festivosAnio = obtenerFestivosPorAnio(idPais, anio);

            for (FestivoDTO f : festivosAnio) {
                if (f.getFecha().equals(fechaValidar)) {
                    return "Es Festivo";
                }
            }
            return "No es festivo";
        } catch (Exception e) {
            return "Fecha No valida";
        }
    }
}