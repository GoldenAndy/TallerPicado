package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.DetalleFactura;
import com.tallerpicado.service.DetalleFacturaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Service
public class DetalleFacturaServiceImpl implements DetalleFacturaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spInsertar;
    private SimpleJdbcCall spActualizar;
    private SimpleJdbcCall spEliminar;
    private SimpleJdbcCall spListar;
    private SimpleJdbcCall spBuscar;

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
            try { d.setNombreArticulo(rs.getString("NOMBRE_ARTICULO")); } catch (SQLException ignore) {}
            return d;
        }
    };

    @PostConstruct
    private void init() {
        // Insertar
        spInsertar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_CREAR_DETALLE_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_id_factura", Types.NUMERIC),
                        new SqlParameter("p_id_articulo", Types.NUMERIC),
                        new SqlParameter("p_cantidad", Types.NUMERIC),
                        new SqlOutParameter("p_id_detalle", Types.NUMERIC)
                );

        // Actualizar
        spActualizar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_ACTUALIZAR_DETALLE_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("p_id_detalle", Types.NUMERIC),
                        new SqlParameter("p_id_articulo", Types.NUMERIC),
                        new SqlParameter("p_cantidad", Types.NUMERIC)
                );

        spEliminar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_ELIMINAR_DETALLE_FACTURA")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("p_id_detalle", Types.NUMERIC));

        spListar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_LISTAR_DETALLES_FACTURA")
                .returningResultSet("p_resultado", rowMapper);

        spBuscar = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_DETALLE_FACTURAS")
                .withProcedureName("SP_BUSCAR_DETALLE_FACTURA_POR_ARTICULO")
                .returningResultSet("p_resultado", rowMapper);
    }

    @Override
    public Long insertar(DetalleFactura detalle) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", detalle.getIdFactura())
                .addValue("p_id_articulo", detalle.getIdArticulo())
                .addValue("p_cantidad", detalle.getCantidad());

        Map<String, Object> out = spInsertar.execute(in);
        Number id = (Number) out.get("p_id_detalle");
        return id != null ? id.longValue() : null;
    }

    @Override
    public void actualizar(DetalleFactura detalle) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_detalle", detalle.getId())
                .addValue("p_id_articulo", detalle.getIdArticulo())
                .addValue("p_cantidad", detalle.getCantidad());
        spActualizar.execute(in);
    }

    @Override
    public void eliminar(Long idDetalle) {
        spEliminar.execute(new MapSqlParameterSource().addValue("p_id_detalle", idDetalle));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DetalleFactura> listarPorFactura(Long idFactura) {
        Map<String, Object> out = spListar.execute(new MapSqlParameterSource().addValue("p_id_factura", idFactura));
        return (List<DetalleFactura>) out.get("p_resultado");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DetalleFactura> buscarPorArticulo(Long idFactura, String patron) {
        MapSqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_id_factura", idFactura)
                .addValue("p_patron", patron);
        Map<String, Object> out = spBuscar.execute(in);
        return (List<DetalleFactura>) out.get("p_resultado");
    }

    @Override
    public Long guardar(DetalleFactura detalle) {
        if (detalle.getId() == null) return insertar(detalle);
        actualizar(detalle);
        return detalle.getId();
    }
}
