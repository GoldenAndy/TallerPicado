package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.domain.OrdenTrabajo;
import com.tallerpicado.domain.Produccion;
import com.tallerpicado.service.ProduccionService;
import jakarta.annotation.PostConstruct;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ProduccionServiceImpl implements ProduccionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spListar;
    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall spActualizar;
    private SimpleJdbcCall spEliminar;
    private SimpleJdbcCall fnBuscarEstado;
    private SimpleJdbcCall fnBuscarMaquina;

    @PostConstruct
    public void init() {
        spListar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withProcedureName("SP_LISTAR_PRODUCCION")
                .declareParameters(new SqlOutParameter("p_resultado", OracleTypes.CURSOR, new ProduccionRowMapper()));

        spInsertar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withProcedureName("SP_INSERTAR_PRODUCCION");

        spActualizar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withProcedureName("SP_ACTUALIZAR_PRODUCCION");

        spEliminar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withProcedureName("SP_ELIMINAR_PRODUCCION");

        fnBuscarEstado = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withFunctionName("FN_BUSCAR_PRODUCCION_ESTADO")
                .declareParameters(new SqlOutParameter("RETURN", OracleTypes.CURSOR, new ProduccionRowMapper()));

        fnBuscarMaquina = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PRODUCCION")
                .withFunctionName("FN_BUSCAR_PRODUCCION_MAQUINA")
                .declareParameters(new SqlOutParameter("RETURN", OracleTypes.CURSOR, new ProduccionRowMapper()));
    }

    @Override
    public List<Produccion> listarTodas() {
        Map<String, Object> result = spListar.execute(new HashMap<>());
        return (List<Produccion>) result.get("p_resultado");
    }

    @Override
    public void insertarProduccion(Long idMaquina, Long idOrden) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_maquina", idMaquina);
        params.put("p_id_orden", idOrden);
        spInsertar.execute(params);
    }

    @Override
    public void actualizarProduccion(Produccion produccion) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_produccion", produccion.getId());
        params.put("p_id_maquina", produccion.getMaquina().getId());
        params.put("p_fecha_inicio", produccion.getFechaInicio());
        params.put("p_fecha_fin", produccion.getFechaFin());
        params.put("p_estado", produccion.getEstado());
        spActualizar.execute(params);
    }

    @Override
    public void eliminarProduccion(Long idProduccion) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id_produccion", idProduccion);
        spEliminar.execute(params);
    }

    @Override
    public List<Produccion> buscarPorEstado(String patronEstado) {
        Map<String, Object> result = fnBuscarEstado.execute(patronEstado);
        return (List<Produccion>) result.get("RETURN");
    }

    @Override
    public List<Produccion> buscarPorNombreMaquina(String patronNombre) {
        Map<String, Object> result = fnBuscarMaquina.execute(patronNombre);
        return (List<Produccion>) result.get("RETURN");
    }

    @Override
    public List<Produccion> buscarPorOrden(Long idOrden) {
        List<Produccion> todas = listarTodas();
        List<Produccion> filtradas = new ArrayList<>();
        for (Produccion p : todas) {
            if (p.getOrden() != null && p.getOrden().getId().equals(idOrden)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    // Mapeador para convertir el resultado del cursor en objetos Produccion
    private static class ProduccionRowMapper implements RowMapper<Produccion> {
        @Override
        public Produccion mapRow(ResultSet rs, int rowNum) throws SQLException {
            Produccion p = new Produccion();
            p.setId(rs.getLong("ID_PRODUCCION"));
            p.setFechaInicio(rs.getDate("FECHA_INICIO"));
            p.setFechaFin(rs.getDate("FECHA_FIN"));
            p.setEstado(rs.getString("ESTADO"));

            Maquinaria m = new Maquinaria();
            m.setId(rs.getLong("ID_MAQUINA"));
            m.setNombre(rs.getString("NOMBRE_MAQUINA")); // viene del JOIN
            p.setMaquina(m);

            OrdenTrabajo o = new OrdenTrabajo();
            o.setId(rs.getLong("ID_ORDEN"));
            p.setOrden(o);

            return p;
        }
    }
}
