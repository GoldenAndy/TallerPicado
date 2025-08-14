package com.tallerpicado.serviceimpl;

import com.tallerpicado.service.LoginService;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean validarCredenciales(String usuario, String contrasenia) {
        String url = "jdbc:oracle:thin:@192.168.100.69:1521/ORCLPDB";
        try (Connection conn = DriverManager.getConnection(url, usuario, contrasenia)) {
            return true; 
        } catch (SQLException e) {
            return false; 
        }
    }
}
