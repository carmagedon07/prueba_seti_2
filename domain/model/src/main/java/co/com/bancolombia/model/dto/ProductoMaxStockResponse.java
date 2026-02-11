package co.com.bancolombia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaxStockResponse {
    private String sucursalNombre;
    private String productoNombre;
    private int stock;
}

