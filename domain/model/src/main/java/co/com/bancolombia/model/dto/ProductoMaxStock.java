package co.com.bancolombia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaxStock {
    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private Long sucursalId;
    private String nombreSucursal;
}
