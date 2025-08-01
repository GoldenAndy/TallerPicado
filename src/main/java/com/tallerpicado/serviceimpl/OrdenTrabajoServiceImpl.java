package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.domain.Empleado;
import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.service.OrdenTrabajoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class OrdenTrabajoServiceImpl implements OrdenTrabajoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall;
    private SimpleJdbcCall insertarCall;
    private SimpleJdbcCall actualizarCall;
    private SimpleJdbcCall eliminarCall;

    @PostConstruct
    private void init() {
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ORDENES_TRABAJO")
                .withProcedureName("SP_LISTAR_ORDENES")
                .returningResultSet("p_resultado", new OrdenTrabajoRowMapper());

        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ORDENES_TRABAJO")
                .withProcedureName("SP_INSERTAR_ORDEN");

        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ORDENES_TRABAJO")
                .withProcedureName("SP_ACTUALIZAR_ORDEN");

        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ORDENES_TRABAJO")
                .withProcedureName("SP_ELIMINAR_ORDEN");
    }

    @Override
    public List<OrdenTrabajo> obtenerTodas() {
        Map<String, Object> result = listarCall.execute(new HashMap<>());
        return (List<OrdenTrabajo>) result.get("p_resultado");
    }

    @Override
    public Optional<OrdenTrabajo> obtenerPorId(Long id) {
        return obtenerTodas().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

@Override
public OrdenTrabajo guardar(OrdenTrabajo orden) {
    System.out.println("=== [DEBUG - ORDEN SERVICE] ===");
    if (orden.getCliente() == null) {
        System.out.println("❌ Cliente es NULL");
        throw new RuntimeException("Cliente es null");
    }
    if (orden.getEmpleado() == null) {
        System.out.println("❌ Empleado es NULL");
        throw new RuntimeException("Empleado es null");
    }

    System.out.println("✅ Cliente ID: " + orden.getCliente().getId());
    System.out.println("✅ Empleado ID: " + orden.getEmpleado().getId());
    System.out.println("✅ Estado: " + orden.getEstado());

    insertarCall.execute(Map.of(
            "p_id_cliente", orden.getCliente().getId(),
            "p_id_empleado", orden.getEmpleado().getId(),
            "p_estado", orden.getEstado()
    ));

    System.out.println("✅ Procedimiento ejecutado correctamente.");
    return orden;
}


    @Override
    public OrdenTrabajo actualizar(Long id, OrdenTrabajo orden) {
        actualizarCall.execute(Map.of(
                "p_id_orden", id,
                "p_id_cliente", orden.getCliente().getId(),
                "p_id_empleado", orden.getEmpleado().getId(),
                "p_estado", orden.getEstado()
        ));
        return orden;
    }

    @Override
    public void eliminar(Long id) {
        eliminarCall.execute(Map.of("p_id_orden", id));
    }

    // ==============================
    // RowMapper para convertir ResultSet a OrdenTrabajo
    // ==============================
    private static class OrdenTrabajoRowMapper implements RowMapper<OrdenTrabajo> {
        @Override
        public OrdenTrabajo mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrdenTrabajo orden = new OrdenTrabajo();
            orden.setId(rs.getLong("ID_ORDEN"));
            orden.setFecha(rs.getDate("FECHA"));

            Cliente cliente = new Cliente();
            cliente.setId(rs.getLong("ID_CLIENTE"));
            cliente.setNombre(rs.getString("NOMBRE_CLIENTE"));
            orden.setCliente(cliente);

            Empleado empleado = new Empleado();
            empleado.setId(rs.getLong("ID_EMPLEADO"));
            empleado.setNombre(rs.getString("NOMBRE_EMPLEADO"));
            orden.setEmpleado(empleado);

            orden.setEstado(rs.getString("ESTADO"));
            return orden;
        }
    }
}
