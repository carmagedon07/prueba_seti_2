package com.prueba.seti.api_test.infrastructure.config;

import com.prueba.seti.api_test.application.usecase.AgregarProductoUseCase;
import com.prueba.seti.api_test.application.usecase.AgregarSucursalUseCase;
import com.prueba.seti.api_test.application.usecase.CrearFranquiciaUseCase;
import com.prueba.seti.api_test.application.usecase.EliminarProductoUseCase;
import com.prueba.seti.api_test.application.usecase.ModificarStockProductoUseCase;
import com.prueba.seti.api_test.application.usecase.ObtenerProductoMaxStockPorSucursalUseCase;
import com.prueba.seti.api_test.application.usecase.ActualizarNombreFranquiciaUseCase;
import com.prueba.seti.api_test.application.usecase.ActualizarNombreSucursalUseCase;
import com.prueba.seti.api_test.application.usecase.ActualizarNombreProductoUseCase;
import com.prueba.seti.api_test.domain.port.out.FranquiciaRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.ProductoRepositoryPort;
import com.prueba.seti.api_test.domain.port.out.SucursalRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CrearFranquiciaUseCase crearFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        return new CrearFranquiciaUseCase(franquiciaRepositoryPort);
    }

    @Bean
    public AgregarSucursalUseCase agregarSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort,
                                                         FranquiciaRepositoryPort franquiciaRepositoryPort) {
        return new AgregarSucursalUseCase(sucursalRepositoryPort, franquiciaRepositoryPort);
    }

    @Bean
    public AgregarProductoUseCase agregarProductoUseCase(ProductoRepositoryPort productoRepositoryPort,
                                                         SucursalRepositoryPort sucursalRepositoryPort) {
        return new AgregarProductoUseCase(productoRepositoryPort, sucursalRepositoryPort);
    }

    @Bean
    public EliminarProductoUseCase eliminarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new EliminarProductoUseCase(productoRepositoryPort);
    }

    @Bean
    public ModificarStockProductoUseCase modificarStockProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new ModificarStockProductoUseCase(productoRepositoryPort);
    }

    @Bean
    public ObtenerProductoMaxStockPorSucursalUseCase obtenerProductoMaxStockPorSucursalUseCase(
        ProductoRepositoryPort productoRepositoryPort,
        FranquiciaRepositoryPort franquiciaRepositoryPort) {
        return new ObtenerProductoMaxStockPorSucursalUseCase(productoRepositoryPort, franquiciaRepositoryPort);
    }

    @Bean
    public ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase(FranquiciaRepositoryPort franquiciaRepositoryPort) {
        return new ActualizarNombreFranquiciaUseCase(franquiciaRepositoryPort);
    }

    @Bean
    public ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase(SucursalRepositoryPort sucursalRepositoryPort) {
        return new ActualizarNombreSucursalUseCase(sucursalRepositoryPort);
    }

    @Bean
    public ActualizarNombreProductoUseCase actualizarNombreProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new ActualizarNombreProductoUseCase(productoRepositoryPort);
    }
}
