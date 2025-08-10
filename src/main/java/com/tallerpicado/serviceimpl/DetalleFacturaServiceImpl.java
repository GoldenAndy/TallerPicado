package com.tallerpicado.serviceimpl;
 
import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.service.DetalleFacturaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
 
@Service
public class DetalleFacturaServiceImpl implements DetalleFacturaService {
 
    @Autowired
    private JdbcTemplate jdbcTemplate;
 
    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall spActualizar;
    private SimpleJdbcCall spEliminar;
    private SimpleJdbcCall spListar;
    private SimpleJdbcCall spBuscar; // wrapper del FN que devuelve cursor
 
    // RowMapper inline (sin clase aparte)
    private final RowMapper<DetalleFactura> rowMapper = new RowMapper<>() {
        @Override
        public DetalleFactura mapRow(ResultSet rs, int rowNum) throws SQLException {
            DetalleFactura d = new DetalleFactura();
            d.setId(rs.getLong("ID_DETALLE"));
            d.setIdFactura(rs.getLong("ID_FACTURA"));
            d.setIdArticulo(rs.getLong("ID_ARTICULO"));
            d.setCantidad(rs.getInt("CANTIDAD"));
            d.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
            d.setSubtotal(rs.getDouble("SUBTOTAL"));
            // alias opcional que traen tus SELECT del package
            try {
                d.setNombreArticulo(rs.getString("NOMBRE_ARTICULO"));
            } catch (SQLException ignore) { /* no pasa nada si no viene */ }
            return d;
        }
    };
 
    @PostConstruct
    private void init() {
        spInsertar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_CREAR_DETALLE_FACTURA");
 
        spActualizar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_ACTUALIZAR_DETALLE_FACTURA");
 
        spEliminar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_ELIMINAR_DETALLE_FACTURA");
 
        spListar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_LISTAR_DETALLES_FACTURA")
                .returningResultSet("p_resultado", rowMapper);
 
        // Requiere el wrapper PROCEDURE SP_BUSCAR_DETALLE_FACTURA_POR_ARTICULO(...)
        spBuscar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_BUSCAR_DETALLE_FACTURA_POR_ARTICULO")
                .returningResultSet("p_resultado", rowMapper);
    }
 
    @Override
    public Long insertar(DetalleFactura detalle) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", detalle.getIdFactura())
                .addValue("p_id_articulo", detalle.getIdArticulo())
                .addValue("p_cantidad", detalle.getCantidad())
                .addValue("p_precio_unitario", detalle.getPrecioUnitario());
 
        Map<String, Object> out = spInsertar.execute(in);
        Number id = (Number) out.get("p_id_detalle"); // OUT definido en el package
        return (id != null) ? id.longValue() : null;
    }
 
    @Override
    public void actualizar(DetalleFactura detalle) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_detalle", detalle.getId())
                .addValue("p_id_articulo", detalle.getIdArticulo())
                .addValue("p_cantidad", detalle.getCantidad())
                .addValue("p_precio_unitario", detalle.getPrecioUnitario());
 
        spActualizar.execute(in);
    }
 
    @Override
    public void eliminar(Long idDetalle) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_detalle", idDetalle);
        spEliminar.execute(in);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    public List<DetalleFactura> listarPorFactura(Long idFactura) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", idFactura);
 
        Map<String, Object> out = spListar.execute(in);
        return (List<DetalleFactura>) out.get("p_resultado");
    }
 
    @Override
    @SuppressWarnings("unchecked")
    public List<DetalleFactura> buscarPorArticulo(Long idFactura, String patron) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", idFactura)
                .addValue("p_patron", patron);
 
        Map<String, Object> out = spBuscar.execute(in);
        return (List<DetalleFactura>) out.get("p_resultado");
    }
 
    @Override
    public Long guardar(DetalleFactura detalle) {
        if (detalle.getId() == null) {
            return insertar(detalle);
        } else {
            actualizar(detalle);
            return detalle.getId();
        }
    }
}