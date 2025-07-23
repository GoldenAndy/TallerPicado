package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Maquinaria;
import com.tallerpicado.service.MaquinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class MaquinariaServiceImpl implements MaquinariaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall;
    private SimpleJdbcCall insertarCall;
    private SimpleJdbcCall actualizarCall;
    private SimpleJdbcCall eliminarCall;
    private SimpleJdbcCall buscarPorNombreCall;

    @PostConstruct
    public void init() {
        // LISTAR
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_MAQUINARIA")
                .withProcedureName("SP_LISTAR_MAQUINAS")
                .declareParameters(new SqlOutParameter("p_resultado", Types.REF_CURSOR))
                .returningResultSet("p_resultado", this::mapearMaquinaria);

        // INSERTAR
        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_MAQUINARIA")
                .withProcedureName("SP_INSERTAR_MAQUINA");

        // ACTUALIZAR
        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_MAQUINARIA")
                .withProcedureName("SP_ACTUALIZAR_MAQUINA");

        // ELIMINAR
        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_MAQUINARIA")
                .withProcedureName("SP_ELIMINAR_MAQUINA");

        // BUSCAR POR NOMBRE (expresión regular)
        buscarPorNombreCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_MAQUINARIA")
                .withFunctionName("FN_BUSCAR_MAQUINA_POR_NOMBRE")
                .declareParameters(new SqlOutParameter("RETURN", Types.REF_CURSOR))
                .returningResultSet("RETURN", this::mapearMaquinaria);
    }

    @Override
    public List<Maquinaria> obtenerTodas() {
        Map<String, Object> result = listarCall.execute();
        return (List<Maquinaria>) result.get("p_resultado");
    }

    @Override
    public Optional<Maquinaria> obtenerPorId(Long id) {
        return obtenerTodas().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Maquinaria guardar(Maquinaria maquinaria) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_NOMBRE", maquinaria.getNombre());
        params.put("P_MARCA", maquinaria.getMarca());
        params.put("P_FECHA_ADQUISICION", java.sql.Date.valueOf(maquinaria.getFechaAdquisicion()));
        params.put("P_ESTADO", maquinaria.getEstado());

        insertarCall.execute(params);
        return maquinaria;
    }

    @Override
    public Maquinaria actualizar(Long id, Maquinaria maquinaria) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_MAQUINA", id);
        params.put("P_NOMBRE", maquinaria.getNombre());
        params.put("P_MARCA", maquinaria.getMarca());
        params.put("P_FECHA_ADQUISICION", java.sql.Date.valueOf(maquinaria.getFechaAdquisicion()));
        params.put("P_ESTADO", maquinaria.getEstado());

        actualizarCall.execute(params);
        maquinaria.setId(id);
        return maquinaria;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_MAQUINA", id);
        eliminarCall.execute(params);
    }

    @Override
    public List<Maquinaria> buscarPorNombre(String patron) {
        Map<String, Object> in = new HashMap<>();
        in.put("P_PATRON", patron);
        Map<String, Object> result = buscarPorNombreCall.execute(in);
        return (List<Maquinaria>) result.get("RETURN");
    }

    // Auxiliar para mapear desde ResultSet
    private Maquinaria mapearMaquinaria(ResultSet rs, int rowNum) throws SQLException {
        Maquinaria m = new Maquinaria();
        m.setId(rs.getLong("ID_MAQUINA"));
        m.setNombre(rs.getString("NOMBRE"));
        m.setMarca(rs.getString("MARCA"));
        m.setFechaAdquisicion(rs.getDate("FECHA_ADQUISICION").toLocalDate());
        m.setEstado(rs.getString("ESTADO"));
        return m;
    }
}
