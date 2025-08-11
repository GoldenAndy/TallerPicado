package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.domain.Empleado;
import com.tallerpicado.domain.Factura;
import com.tallerpicado.service.FacturaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall, insertarCall, actualizarCall, eliminarCall;
    private SimpleJdbcCall buscarPorClienteFn, buscarPorIdFn;
    private SimpleJdbcCall obtenerPorIdCall;

    @PostConstruct
    private void init() {
        // LISTAR
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_LISTAR_FACTURAS")
                .returningResultSet("p_resultado", new FacturaRowMapper());

        // INSERTAR (sin total, OUT id)
        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_INSERTAR_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_fecha", Types.DATE),
                        new SqlParameter("p_id_cliente", Types.NUMERIC),
                        new SqlParameter("p_id_empleado", Types.NUMERIC),
                        new SqlOutParameter("p_id_factura", Types.NUMERIC)
                );

        // ACTUALIZAR (sin total)
        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_ACTUALIZAR_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_id_factura", Types.NUMERIC),
                        new SqlParameter("p_fecha", Types.DATE),
                        new SqlParameter("p_id_cliente", Types.NUMERIC),
                        new SqlParameter("p_id_empleado", Types.NUMERIC)
                );

        // ELIMINAR
        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_ELIMINAR_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_id_factura", Types.NUMERIC));

        // FUNCIONES de búsqueda (retornan SYS_REFCURSOR)
        buscarPorClienteFn = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withFunctionName("FN_BUSCAR_FACTURAS_POR_CLIENTE")
                .returningResultSet("RETURN_VALUE", new FacturaRowMapper());

        buscarPorIdFn = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withFunctionName("FN_BUSCAR_FACTURAS_POR_ID")
                .returningResultSet("RETURN_VALUE", new FacturaRowMapper());
        
        
        obtenerPorIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_FACTURAS")
                .withProcedureName("SP_OBTENER_FACTURA_POR_ID")
                .returningResultSet("p_resultado", new FacturaRowMapper());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Factura> listarTodas() {
        Map<String, Object> result = listarCall.execute();
        return (List<Factura>) result.get("p_resultado");
    }

    @Override
    public Long insertar(Factura factura) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_fecha", factura.getFechaEmision())
                .addValue("p_id_cliente", factura.getCliente().getId())
                .addValue("p_id_empleado", factura.getEmpleado().getId());

        Map<String, Object> out = insertarCall.execute(in);
        Number id = (Number) out.get("p_id_factura");
        return id != null ? id.longValue() : null;
    }

    @Override
    public void actualizar(Long idFactura, Factura factura) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", idFactura)
                .addValue("p_fecha", factura.getFechaEmision())
                .addValue("p_id_cliente", factura.getCliente().getId())
                .addValue("p_id_empleado", factura.getEmpleado().getId());

        actualizarCall.execute(in);
    }

    @Override
    public void eliminar(Long idFactura) {
        eliminarCall.execute(new MapSqlParameterSource().addValue("p_id_factura", idFactura));
    }

    @Override
    public List<Factura> buscarPorCliente(String patron) {
        MapSqlParameterSource in = new MapSqlParameterSource().addValue("p_patron", patron);
        // El return type es List<Factura> gracias al returningResultSet registrado
        return buscarPorClienteFn.executeFunction(List.class, in);
    }

    @Override
    public List<Factura> buscarPorId(String patron) {
        MapSqlParameterSource in = new MapSqlParameterSource().addValue("p_patron", patron);
        return buscarPorIdFn.executeFunction(List.class, in);
    }

    @Override
    public Optional<Factura> obtenerPorId(Long id) {
        MapSqlParameterSource in = new MapSqlParameterSource().addValue("p_id_factura", id);
        @SuppressWarnings("unchecked")
        var out = (Map<String, Object>) obtenerPorIdCall.execute(in);
        @SuppressWarnings("unchecked")
        var list = (List<Factura>) out.get("p_resultado");

        if (list == null || list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }
    
    
    
 

    // ---------- RowMapper ----------
    private static class FacturaRowMapper implements RowMapper<Factura> {
        @Override
        public Factura mapRow(ResultSet rs, int rowNum) throws SQLException {
            Factura f = new Factura();
            f.setId(rs.getLong("ID_FACTURA"));
            f.setFechaEmision(rs.getDate("FECHA_EMISION"));
            f.setTotal(rs.getDouble("TOTAL"));

            Cliente cliente = new Cliente();
            cliente.setId(rs.getLong("ID_CLIENTE"));
            try { cliente.setNombre(rs.getString("NOMBRE_CLIENTE")); } catch (SQLException ignored) {}
            f.setCliente(cliente);

            Empleado empleado = new Empleado();
            empleado.setId(rs.getLong("ID_EMPLEADO"));
            // Soporta alias NOMBRE_EMPLEADO o EMPLEADO (por compatibilidad)
            String empNombre;
            try { empNombre = rs.getString("NOMBRE_EMPLEADO"); }
            catch (SQLException e) { empNombre = null; }
            if (empNombre == null) {
                try { empNombre = rs.getString("EMPLEADO"); } catch (SQLException ignored) {}
            }
            empleado.setNombre(empNombre);
            f.setEmpleado(empleado);

            return f;
        }
    }
}
