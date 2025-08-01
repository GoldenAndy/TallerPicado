package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Mantenimiento;
import com.tallerpicado.repository.MantenimientoRepository;
import com.tallerpicado.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.sql.Types;
import java.util.*;

@Service
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall spActualizar;
    private SimpleJdbcCall spEliminar;
    private SimpleJdbcCall spListar;

    @PostConstruct
    private void init() {
        spInsertar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_MANTENIMIENTO")
                .withProcedureName("INSERTAR");

        spActualizar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_MANTENIMIENTO")
                .withProcedureName("ACTUALIZAR");

        spEliminar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_MANTENIMIENTO")
                .withProcedureName("ELIMINAR");

        spListar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_MANTENIMIENTO")
                .withProcedureName("LISTAR")
                .declareParameters(new SqlParameter("p_resultado", Types.REF_CURSOR))
                .returningResultSet("p_resultado", (rs, rowNum) -> {
                    Mantenimiento m = new Mantenimiento();
                    m.setId(rs.getLong("ID_MANTENIMIENTO"));
                    m.setIdMaquina(rs.getLong("ID_MAQUINA"));
                    m.setIdTipo(rs.getLong("ID_TIPO"));
                    m.setFecha(rs.getDate("FECHA_MANTENIMIENTO").toLocalDate());
                    m.setDescripcion(rs.getString("DESCRIPCION"));
                    return m;
                });
    }

    @Override
    public List<Mantenimiento> obtenerTodos() {
        Map<String, Object> result = spListar.execute();
        return (List<Mantenimiento>) result.get("p_resultado");
    }

    @Override
    public Optional<Mantenimiento> obtenerPorId(Long id) {
        // Por si luego implementas buscar_por_id con SYS_REFCURSOR
        return Optional.empty();
    }

    @Override
    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_maquina", mantenimiento.getIdMaquina());
        params.put("p_id_tipo", mantenimiento.getIdTipo());
        params.put("p_fecha", Date.valueOf(mantenimiento.getFecha()));
        params.put("p_descripcion", mantenimiento.getDescripcion());

        spInsertar.execute(params);
        return mantenimiento;
    }

    @Override
    public Mantenimiento actualizar(Long id, Mantenimiento mantenimiento) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_mantenimiento", id);
        params.put("p_id_maquina", mantenimiento.getIdMaquina());
        params.put("p_id_tipo", mantenimiento.getIdTipo());
        params.put("p_fecha", Date.valueOf(mantenimiento.getFecha()));
        params.put("p_descripcion", mantenimiento.getDescripcion());

        spActualizar.execute(params);
        return mantenimiento;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_mantenimiento", id);
        spEliminar.execute(params);
    }
}
