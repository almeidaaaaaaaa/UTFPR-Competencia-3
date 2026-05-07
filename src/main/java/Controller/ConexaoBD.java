/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    
    private static final String url    = "jdbc:mysql://localhost:3306/meninas_digitais";
    private static final String usuario = "root";
    private static final String senha   = "";

    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao realizada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        return conn;
    }
}

