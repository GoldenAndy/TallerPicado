package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Empleado; 
import com.tallerpicado.service.EmpleadoService; 
import jakarta.annotation.PostConstruct; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.jdbc.core.JdbcTemplate; 
import org.springframework.jdbc.core.RowMapper; 
import org.springframework.jdbc.core.simple.SimpleJdbcCall; 
import org.springframework.stereotype.Service; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Service public class EmpleadoServiceImpl implements EmpleadoService {

@Autowired
private JdbcTemplate jdbcTemplate;

private SimpleJdbcCall spListar, spInsertar, spActualizar, spEliminar;
private SimpleJdbcCall fnBuscarPorNombre, fnBuscarPorPuesto, fnBuscarPorProveedor, fnBuscarPorEstado;

@PostConstruct
public void init() {
    spListar = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withProcedureName("SP_LISTAR_EMPLEADOS")
            .returningResultSet("p_resultado", empleadoMapper);

    spInsertar = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withProcedureName("SP_INSERTAR_EMPLEADO");

    spActualizar = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withProcedureName("SP_ACTUALIZAR_EMPLEADO");

    spEliminar = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withProcedureName("SP_ELIMINAR_EMPLEADO");

    fnBuscarPorNombre = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withFunctionName("FN_BUSCAR_EMPLEADO_POR_NOMBRE")
            .returningResultSet("RETURN", empleadoMapper);

    fnBuscarPorPuesto = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withFunctionName("FN_BUSCAR_EMPLEADO_POR_PUESTO")
            .returningResultSet("RETURN", empleadoMapper);

    fnBuscarPorProveedor = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withFunctionName("FN_BUSCAR_EMPLEADO_POR_PROVEEDOR")
            .returningResultSet("RETURN", empleadoMapper);

    fnBuscarPorEstado = new SimpleJdbcCall(jdbcTemplate)
            .withCatalogName("PAQ_EMPLEADOS")
            .withFunctionName("FN_BUSCAR_EMPLEADO_POR_ESTADO")
            .returningResultSet("RETURN", empleadoMapper);
}

private final RowMapper<Empleado> empleadoMapper = new RowMapper<Empleado>() {
    @Override
    public Empleado mapRow(ResultSet rs, int rowNum) throws SQLException {
        Empleado e = new Empleado();
        e.setId(rs.getLong("ID_EMPLEADO"));
        e.setNombre(rs.getString("NOMBRE"));
        e.setApellido(rs.getString("APELLIDO"));
        e.setIdPuesto(rs.getLong("ID_PUESTO"));
        e.setCelular(rs.getString("CELULAR"));
        e.setCorreo(rs.getString("CORREO"));
        e.setEstado(rs.getString("ESTADO"));
        e.setNombrePuesto(rs.getString("NOMBRE_PUESTO"));
        return e;
    }
};

    @Override
    public List<Empleado> obtenerTodos() {
        Map<String, Object> result = spListar.execute(new HashMap<>());
        List<Empleado> empleados = (List<Empleado>) result.get("p_resultado");

        for (Empleado e : empleados) {
            List<String> nombresProveedores = obtenerNombresProveedoresDeEmpleado(e.getId());
            e.setProveedoresAsignados(nombresProveedores);

            e.setProveedores(obtenerIdsProveedoresDeEmpleado(e.getId()));
        }
        return empleados;
    }


    @Override
    public Optional<Empleado> obtenerPorId(Long id) {
        return obtenerTodos().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(e -> {
                    e.setProveedores(obtenerIdsProveedoresDeEmpleado(id));
                    e.setProveedoresAsignados(obtenerNombresProveedoresDeEmpleado(id));
                    return e;
                });
    }


    @Override
    public Empleado guardar(Empleado empleado) {
        jdbcTemplate.execute((ConnectionCallback<Void>) con -> {
            var conn = con.unwrap(oracle.jdbc.OracleConnection.class);
            var arr = conn.createOracleArray("SYS.ODCINUMBERLIST", empleado.getProveedores().toArray());
            spInsertar.execute(
                    Map.of(
                            "p_nombre", empleado.getNombre(),
                            "p_apellido", empleado.getApellido(),
                            "p_id_puesto", empleado.getIdPuesto(),
                            "p_celular", empleado.getCelular(),
                            "p_correo", empleado.getCorreo(),
                            "p_estado", empleado.getEstado(),
                            "p_proveedores", arr
                    )
            );
            return null;
        });
        return empleado;
    }

    @Override
    public Empleado actualizar(Long id, Empleado empleado) {
        jdbcTemplate.execute((ConnectionCallback<Void>) con -> {
            var conn = con.unwrap(oracle.jdbc.OracleConnection.class);
            var arr = conn.createOracleArray("SYS.ODCINUMBERLIST", empleado.getProveedores().toArray());
            spActualizar.execute(
                    Map.of(
                            "p_id_empleado", id,
                            "p_nombre", empleado.getNombre(),
                            "p_apellido", empleado.getApellido(),
                            "p_id_puesto", empleado.getIdPuesto(),
                            "p_celular", empleado.getCelular(),
                            "p_correo", empleado.getCorreo(),
                            "p_estado", empleado.getEstado(),
                            "p_proveedores", arr
                    )
            );
            return null;
        });
        return empleado;
    }


    @Override
    public void eliminar(Long id) {
        spEliminar.execute(Map.of("p_id_empleado", id));
    }

    @Override
    public List<Empleado> buscarPorNombre(String patron) {
        Map<String, Object> result = fnBuscarPorNombre.execute(Map.of("p_patron", patron));
        return (List<Empleado>) result.get("RETURN");
    }

    @Override
    public List<Empleado> filtrarPorPuesto(Long idPuesto) {
        Map<String, Object> result = fnBuscarPorPuesto.execute(Map.of("p_id_puesto", idPuesto));
        return (List<Empleado>) result.get("RETURN");
    }

    @Override
    public List<Empleado> filtrarPorProveedor(Long idProveedor) {
        Map<String, Object> result = fnBuscarPorProveedor.execute(Map.of("p_id_proveedor", idProveedor));
        return (List<Empleado>) result.get("RETURN");
    }

    @Override
    public List<Empleado> filtrarPorEstado(String estado) {
        Map<String, Object> result = fnBuscarPorEstado.execute(Map.of("p_estado", estado));
        return (List<Empleado>) result.get("RETURN");
    }

    private List<String> obtenerNombresProveedoresDeEmpleado(Long idEmpleado) {
        String sql = """
        SELECT P.NOMBRE_EMPRESA 
        FROM PROVEEDORES P 
        JOIN PROVEEDORES_EMPLEADOS PE ON P.ID_PROVEEDOR = PE.ID_PROVEEDOR 
        WHERE PE.ID_EMPLEADO = ?
    """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("NOMBRE_EMPRESA"), idEmpleado);
    }

    private List<Long> obtenerIdsProveedoresDeEmpleado(Long idEmpleado) {
        String sql = """
        SELECT PE.ID_PROVEEDOR
        FROM PROVEEDORES_EMPLEADOS PE
        WHERE PE.ID_EMPLEADO = ?
    """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("ID_PROVEEDOR"), idEmpleado);
    }

    @Override
    public List<Empleado> filtrarMultiples(String nombre, Long idPuesto, Long idProveedor, String estado) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_EMPLEADOS")
                .withFunctionName("FN_FILTRAR_EMPLEADOS_MULTIPLE")
                .returningResultSet("result", (rs, rowNum) -> {
                    Empleado e = new Empleado();
                    e.setId(rs.getLong("ID_EMPLEADO"));
                    e.setNombre(rs.getString("NOMBRE"));
                    e.setApellido(rs.getString("APELLIDO"));
                    e.setIdPuesto(rs.getLong("ID_PUESTO"));
                    e.setCelular(rs.getString("CELULAR"));
                    e.setCorreo(rs.getString("CORREO"));
                    e.setEstado(rs.getString("ESTADO"));
                    e.setNombrePuesto(rs.getString("NOMBRE_PUESTO"));
                    e.setProveedoresAsignados(obtenerNombresProveedoresDeEmpleado(rs.getLong("ID_EMPLEADO")));
                    return e;
                });

        SqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("p_nombre", (nombre != null && !nombre.isBlank()) ? nombre : null)
                .addValue("p_id_puesto", idPuesto)
                .addValue("p_id_prov", idProveedor)
                .addValue("p_estado", (estado != null && !estado.isBlank()) ? estado.toUpperCase() : null);

        return jdbcCall.executeFunction(List.class, inParams);
    }

    @Override
    public Optional<Empleado> buscarPorNombreExacto(String nombre) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_EMPLEADOS")
                .withFunctionName("FN_BUSCAR_EMPLEADO_EXACTO")
                .returningResultSet("empleados", BeanPropertyRowMapper.newInstance(Empleado.class));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("P_NOMBRE", nombre);

        List<Empleado> resultado = jdbcCall.executeFunction(List.class, params);
        if (resultado != null && !resultado.isEmpty()) {
            return Optional.of(resultado.get(0));
        }
        return Optional.empty();
    }

}