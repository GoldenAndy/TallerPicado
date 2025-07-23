package com.tallerpicado.serviceimpl;

import com.tallerpicado.domain.Cliente;
import com.tallerpicado.service.ClienteService;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall listarCall;
    private SimpleJdbcCall insertarCall;
    private SimpleJdbcCall actualizarCall;
    private SimpleJdbcCall eliminarCall;
    private SimpleJdbcCall buscarPorNombreCall;

    @PostConstruct
    public void init() {
        // Listar clientes
        listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_CLIENTES")
                .withProcedureName("SP_LISTAR_CLIENTES")
                .declareParameters(new SqlOutParameter("p_resultado", Types.REF_CURSOR))
                .returningResultSet("p_resultado", this::mapearCliente);

        // Insertar cliente
        insertarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_CLIENTES")
                .withProcedureName("SP_INSERTAR_CLIENTE");

        // Actualizar cliente
        actualizarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_CLIENTES")
                .withProcedureName("SP_ACTUALIZAR_CLIENTE");

        // Eliminar cliente
        eliminarCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_CLIENTES")
                .withProcedureName("SP_ELIMINAR_CLIENTE");

        // Buscar por nombre usando expresión regular
        buscarPorNombreCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PAQ_CLIENTES")
                .withFunctionName("FN_BUSCAR_CLIENTE_POR_NOMBRE")
                .declareParameters(new SqlOutParameter("RETURN", Types.REF_CURSOR))
                .returningResultSet("RETURN", this::mapearCliente);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        Map<String, Object> result = listarCall.execute();
        return (List<Cliente>) result.get("p_resultado");
    }

    @Override
    public List<Cliente> buscarPorNombre(String patron) {
        Map<String, Object> in = new HashMap<>();
        in.put("P_PATRON", patron); // asegúrate que el parámetro se llama así en Oracle
        Map<String, Object> result = buscarPorNombreCall.execute(in);
        return (List<Cliente>) result.get("RETURN");
    }

    @Override
    public Optional<Cliente> obtenerPorId(Long id) {
        return obtenerTodos().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_NOMBRE", cliente.getNombre());
        params.put("P_CEDULA", cliente.getCedula());
        params.put("P_CELULAR", cliente.getCelular());
        params.put("P_CORREO", cliente.getCorreo());

        insertarCall.execute(params);
        return cliente;
    }

    @Override
    public Cliente actualizar(Long id, Cliente cliente) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_CLIENTE", id);
        params.put("P_NOMBRE", cliente.getNombre());
        params.put("P_CEDULA", cliente.getCedula());
        params.put("P_CELULAR", cliente.getCelular());
        params.put("P_CORREO", cliente.getCorreo());

        actualizarCall.execute(params);
        cliente.setId(id);
        return cliente;
    }

    @Override
    public void eliminar(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_CLIENTE", id);
        eliminarCall.execute(params);
    }

    // Método auxiliar para mapear Cliente desde ResultSet
    private Cliente mapearCliente(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("ID_CLIENTE"));
        cliente.setNombre(rs.getString("NOMBRE"));
        cliente.setCedula(rs.getString("CEDULA"));
        cliente.setCelular(rs.getString("CELULAR"));
        cliente.setCorreo(rs.getString("CORREO"));
        return cliente;
    }
    
    
    @Override
   public Optional<Cliente> buscarPorNombreExacto(String nombre) {
       SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
           .withCatalogName("PAQ_CLIENTES")
           .withFunctionName("FN_BUSCAR_CLIENTE_EXACTO")
           .returningResultSet("clientes", BeanPropertyRowMapper.newInstance(Cliente.class));

       MapSqlParameterSource params = new MapSqlParameterSource()
           .addValue("P_NOMBRE", nombre);

       List<Cliente> resultado = jdbcCall.executeFunction(List.class, params);
       if (resultado != null && !resultado.isEmpty()) {
           return Optional.of(resultado.get(0));
       }
       return Optional.empty();
   }
}
