package com.tallerpicado.serviceimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.tallerpicado.domain.Articulo;
import com.tallerpicado.service.ArticuloService;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall;
    private SimpleJdbcCall insertarCall;
    private SimpleJdbcCall actualizarCall;
    private SimpleJdbcCall eliminarCall;
    private SimpleJdbcCall buscarPorNombreCall;

    @PostConstruct
    public void init() {
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ARTICULOS")
                .withProcedureName("SP_LISTAR_ARTICULOS")
                .declareParameters(new SqlOutParameter("p_resultado", Types.REF_CURSOR))
                .returningResultSet("p_resultado", this::mapearArticulo);

        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ARTICULOS")
                .withProcedureName("SP_INSERTAR_ARTICULO");

        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ARTICULOS")
                .withProcedureName("SP_ACTUALIZAR_ARTICULO");

        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ARTICULOS")
                .withProcedureName("SP_ELIMINAR_ARTICULO");

        buscarPorNombreCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_ARTICULOS")
                .withFunctionName("FN_BUSCAR_ARTICULO_POR_NOMBRE")
                .declareParameters(new SqlOutParameter("RETURN", Types.REF_CURSOR))
                .returningResultSet("RETURN", this::mapearArticulo);
    }

    @Override
    public List<Articulo> obtenerTodos() {
        Map<String, Object> result = listarCall.execute();
        return (List<Articulo>) result.get("p_resultado");
    }

    @Override
    public List<Articulo> buscarPorNombre(String patron) {
        Map<String, Object> in = new HashMap<>();
        in.put("P_PATRON", patron);
        Map<String, Object> result = buscarPorNombreCall.execute(in);
        return (List<Articulo>) result.get("RETURN");
    }

    @Override
    public Optional<Articulo> obtenerPorId(Long id) {
        return obtenerTodos().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public Articulo guardar(Articulo articulo) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_NOMBRE", articulo.getNombre());
        params.put("P_DESCRIPCION", articulo.getDescripcion());
        params.put("P_PRECIO", articulo.getPrecio());
        params.put("P_STOCK", articulo.getStock());
        params.put("P_TIPO", String.valueOf(articulo.getTipo()));

        insertarCall.execute(params);
        return articulo;
    }

    @Override
    public Articulo actualizar(Long id, Articulo articulo) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_ARTICULO", id);
        params.put("P_NOMBRE", articulo.getNombre());
        params.put("P_DESCRIPCION", articulo.getDescripcion());
        params.put("P_PRECIO", articulo.getPrecio());
        params.put("P_STOCK", articulo.getStock());
        params.put("P_TIPO", String.valueOf(articulo.getTipo()));

        actualizarCall.execute(params);
        articulo.setId(id);
        return articulo;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_ARTICULO", id);
        eliminarCall.execute(params);
    }

   private Articulo mapearArticulo(ResultSet rs, int rowNum) throws SQLException {
    Articulo articulo = new Articulo();
    articulo.setId(rs.getLong("ID_ARTICULO"));
    articulo.setNombre(rs.getString("NOMBRE"));
    articulo.setDescripcion(rs.getString("DESCRIPCION"));
    articulo.setPrecio(rs.getDouble("PRECIO"));  // Usar getDouble porque tu entidad usa Double
    articulo.setStock(rs.getInt("STOCK"));
    articulo.setTipo(rs.getString("TIPO")); // Ya es String, no usar charAt
    return articulo;
}

} 
