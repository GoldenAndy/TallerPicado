package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Proveedor;
import com.tallerpicado.service.ProveedorService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.*;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spListarProveedores;
    private SimpleJdbcCall spInsertarProveedor;
    private SimpleJdbcCall spActualizarProveedor;
    private SimpleJdbcCall spEliminarProveedor;
    private SimpleJdbcCall fnBuscarProveedorRegex;

    @PostConstruct
    public void init() {
        spListarProveedores = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PROVEEDORES")
                .withProcedureName("SP_LISTAR_PROVEEDORES")
                .declareParameters(new SqlOutParameter("p_result", oracle.jdbc.OracleTypes.CURSOR,
                        (rs, rowNum) -> {
                            Proveedor p = new Proveedor();
                            p.setIdProveedor(rs.getLong("ID_PROVEEDOR"));
                            p.setNombreEmpresa(rs.getString("NOMBRE_EMPRESA"));
                            p.setCelular(rs.getString("CELULAR"));
                            p.setCorreo(rs.getString("CORREO"));
                            return p;
                        }));

        spInsertarProveedor = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PROVEEDORES")
                .withProcedureName("SP_INSERTAR_PROVEEDOR");

        spActualizarProveedor = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PROVEEDORES")
                .withProcedureName("SP_ACTUALIZAR_PROVEEDOR");

        spEliminarProveedor = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PROVEEDORES")
                .withProcedureName("SP_ELIMINAR_PROVEEDOR");

        fnBuscarProveedorRegex = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_PROVEEDORES")
                .withFunctionName("FN_BUSCAR_PROVEEDOR_REGEX")
                .declareParameters(new SqlParameter("p_patron", Types.VARCHAR))
                .declareParameters(new SqlOutParameter("RETURN", oracle.jdbc.OracleTypes.CURSOR,
                        (rs, rowNum) -> {
                            Proveedor p = new Proveedor();
                            p.setIdProveedor(rs.getLong("ID_PROVEEDOR"));
                            p.setNombreEmpresa(rs.getString("NOMBRE_EMPRESA"));
                            p.setCelular(rs.getString("CELULAR"));
                            p.setCorreo(rs.getString("CORREO"));
                            return p;
                        }));
    }

    @Override
    public List<Proveedor> obtenerTodos() {
        Map<String, Object> result = spListarProveedores.execute();
        return (List<Proveedor>) result.get("p_result");
    }

    @Override
    public Optional<Proveedor> obtenerPorId(Long id) {
        return obtenerTodos().stream().filter(p -> p.getIdProveedor().equals(id)).findFirst();
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_nombre_empresa", proveedor.getNombreEmpresa());
        params.put("p_celular", proveedor.getCelular());
        params.put("p_correo", proveedor.getCorreo());
        spInsertarProveedor.execute(params);
        return proveedor;
    }

    @Override
    public Proveedor actualizar(Long id, Proveedor proveedor) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id", id);
        params.put("p_nombre_empresa", proveedor.getNombreEmpresa());
        params.put("p_celular", proveedor.getCelular());
        params.put("p_correo", proveedor.getCorreo());
        spActualizarProveedor.execute(params);
        return proveedor;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_id", id);
        spEliminarProveedor.execute(params);
    }

    @Override
    public List<Proveedor> buscarPorNombreRegex(String patron) {
        return (List<Proveedor>) fnBuscarProveedorRegex.executeFunction(List.class, patron);
    }
}
