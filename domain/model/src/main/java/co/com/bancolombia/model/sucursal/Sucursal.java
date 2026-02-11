package co.com.bancolombia.model.sucursal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    private Long id;
    private String nombre;
    private Long franquiciaId;
}

