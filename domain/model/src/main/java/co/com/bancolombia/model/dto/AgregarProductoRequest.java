package co.com.bancolombia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarProductoRequest {
    private String nombre;
    private int stock;
    private Long sucursalId;
}

