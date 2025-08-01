package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.TipoMantenimiento;
import com.tallerpicado.service.TipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.*;

@Service
public class TipoMantenimientoServiceImpl implements TipoMantenimientoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall spActualizar;
    private SimpleJdbcCall spEliminar;
    private SimpleJdbcCall spListar;
    private SimpleJdbcCall fnBuscarPorNombre;

    @PostConstruct
    private void init() {
        spInsertar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_TIPO_MANTENIMIENTO")
                .withProcedureName("INSERTAR");

        spActualizar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_TIPO_MANTENIMIENTO")
                .withProcedureName("ACTUALIZAR");

        spEliminar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_TIPO_MANTENIMIENTO")
                .withProcedureName("ELIMINAR");

        spListar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_TIPO_MANTENIMIENTO")
                .withProcedureName("LISTAR")
                .declareParameters(new SqlParameter("p_resultado", Types.REF_CURSOR))  // Declaramos el cursor
                .returningResultSet("p_resultado", (rs, rowNum) -> {                   // Usamos ese mismo nombre
                    TipoMantenimiento tipo = new TipoMantenimiento();
                    tipo.setId(rs.getLong("ID_TIPO"));
                    tipo.setNombre(rs.getString("NOMBRE"));
                    return tipo;
                });

        fnBuscarPorNombre = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_TIPO_MANTENIMIENTO")
                .withFunctionName("FN_BUSCAR_TIPO_POR_NOMBRE")
                .declareParameters(new SqlParameter("p_patron", Types.VARCHAR))
                .returningResultSet("RETURN", (rs, rowNum) -> {
                    TipoMantenimiento tipo = new TipoMantenimiento();
                    tipo.setId(rs.getLong("ID_TIPO"));
                    tipo.setNombre(rs.getString("NOMBRE"));
                    return tipo;
                });
    }

    @Override
    public List<TipoMantenimiento> obtenerTodos() {
        Map<String, Object> result = spListar.execute();
        return (List<TipoMantenimiento>) result.get("p_resultado"); // ¡Usa el nombre declarado!
    }

    @Override
    public Optional<TipoMantenimiento> obtenerPorId(Long id) {
        // Opción futura si agregas FN o SP de búsqueda por ID
        return Optional.empty();
    }

    @Override
    public TipoMantenimiento guardar(TipoMantenimiento tipo) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_nombre", tipo.getNombre());

        spInsertar.execute(params);
        return tipo;
    }

    @Override
    public TipoMantenimiento actualizar(Long id, TipoMantenimiento tipo) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_tipo", id);
        params.put("p_nombre", tipo.getNombre());

        spActualizar.execute(params);
        return tipo;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_tipo", id);

        spEliminar.execute(params);
    }

    @Override
    public List<TipoMantenimiento> buscarPorNombre(String nombre) {
        Map<String, Object> result = fnBuscarPorNombre.execute(new HashMap<>() {{
            put("p_patron", nombre);
        }});
        return (List<TipoMantenimiento>) result.get("RETURN");
    }
}
