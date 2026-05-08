package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilitário de conexão com o banco de dados MySQL.
 *
 * ⚠️  Altere as constantes abaixo conforme seu ambiente antes de rodar.
 */
public class ConexaoBD {

    // ── Configurações ── altere aqui se necessário ───────────────────────────
    private static final String URL     = "jdbc:mysql://localhost:3306/meninas_digitais"
                                        + "?useSSL=false&allowPublicKeyRetrieval=true"
                                        + "&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";   // coloque sua senha do MySQL aqui
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Abre e retorna uma conexão com o banco de dados.
     *
     * @throws SQLException se não conseguir conectar
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Fecha a conexão com segurança (ignora se for null).
     */
    public static void fechar(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
