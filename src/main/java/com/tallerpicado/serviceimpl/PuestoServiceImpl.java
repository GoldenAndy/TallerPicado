package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Puesto;
import com.tallerpicado.service.PuestoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import oracle.jdbc.OracleTypes;

@Service
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spListar;
    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall fnBuscar;

    @PostConstruct
    public void init() {
        spListar = new SimpleJdbcCall(dataSource)
                .withCatalogName("PAQ_PUESTOS")
                .withProcedureName("SP_LISTAR_PUESTOS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("P_RESULTADO", OracleTypes.CURSOR, new PuestoRowMapper())
                );

        fnBuscar = new SimpleJdbcCall(dataSource)
                .withCatalogName("PAQ_PUESTOS")
                .withFunctionName("FN_BUSCAR_PUESTO_NOMBRE")
                .returningResultSet("RETURN", new PuestoRowMapper());
        
        
        spInsertar = new SimpleJdbcCall(dataSource)
                .withCatalogName("PAQ_PUESTOS")
                .withProcedureName("SP_INSERTAR_PUESTO")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                    new org.springframework.jdbc.core.SqlParameter("p_nombre", java.sql.Types.VARCHAR)
                );
    }

    @Override
    public void guardarPuesto(String nombre) {
        jdbcTemplate.update("{call PAQ_PUESTOS.SP_INSERTAR_PUESTO(?)}", nombre);

        // Verifica si se insertó más de una vez para lanzar advertencia
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM PUESTOS WHERE LOWER(NOMBRE) = LOWER(?)", 
            Integer.class, nombre.trim()
        );

        if (count != null && count > 1) {
            throw new RuntimeException("⚠️ El puesto '" + nombre + "' ya existe más de una vez.");
        }
    }


    @Override
    public void actualizarPuesto(Long id, String nombre) {
        jdbcTemplate.update("BEGIN PAQ_PUESTOS.SP_ACTUALIZAR_PUESTO(?, ?); END;", id, nombre);
    }

    @Override
    public void eliminarPuesto(Long id) {
        jdbcTemplate.update("BEGIN PAQ_PUESTOS.SP_ELIMINAR_PUESTO(?); END;", id);
    }

    @Override
    public List<Puesto> listarPuestos() {
        Map<String, Object> result = spListar.execute(new HashMap<>());
        return (List<Puesto>) result.get("P_RESULTADO");
    }

    @Override
    public List<Puesto> buscarPorNombre(String patron) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_patron", patron);
        return (List<Puesto>) fnBuscar.execute(params).get("RETURN");
    }

    private static class PuestoRowMapper implements RowMapper<Puesto> {
        @Override
        public Puesto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Puesto p = new Puesto();
            p.setId(rs.getLong("ID_PUESTO"));
            p.setNombre(rs.getString("NOMBRE"));
            return p;
        }
    }
}
