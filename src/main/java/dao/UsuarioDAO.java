package dao;

import controller.ConexaoBD;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados para a entidade Usuario.
 * RF01 - Cadastro/Login.
 */
public class UsuarioDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────

    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenha()); // idealmente armazene hash (ex.: BCrypt)
            ps.setString(4, u.getTipo().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) u.setId(rs.getInt(1));
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    public Usuario buscarPorEmailSenha(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } finally {
            ConexaoBD.fechar(conn);
        }
        return null;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } finally {
            ConexaoBD.fechar(conn);
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally {
            ConexaoBD.fechar(conn);
        }
        return lista;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET nome=?, email=?, senha=?, tipo=? WHERE id=?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenha());
            ps.setString(4, u.getTipo().name());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── Mapeamento ResultSet → Objeto ─────────────────────────────────────────

    private Usuario mapear(ResultSet rs) throws SQLException {
        TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
        switch (tipo) {
            case GESTOR:
                Gestor g = new Gestor();
                g.setId(rs.getInt("id"));
                g.setNome(rs.getString("nome"));
                g.setEmail(rs.getString("email"));
                g.setSenha(rs.getString("senha"));
                return g;
            case ADMINISTRADOR:
                Administrador a = new Administrador();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setEmail(rs.getString("email"));
                a.setSenha(rs.getString("senha"));
                return a;
            default: // COMUM
                UsuarioComum uc = new UsuarioComum();
                uc.setId(rs.getInt("id"));
                uc.setNome(rs.getString("nome"));
                uc.setEmail(rs.getString("email"));
                uc.setSenha(rs.getString("senha"));
                return uc;
        }
    }
}
