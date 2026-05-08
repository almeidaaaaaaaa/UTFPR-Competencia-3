package dao;

import controller.ConexaoBD;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados para ProjetoExtensao.
 */
public class ProjetoExtensaoDAO {

    public void inserir(ProjetoExtensao p) throws SQLException {
        String sql = "INSERT INTO projeto_extensao (nome, descricao, id_coordenador) VALUES (?, ?, ?)";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setInt(3, p.getCoordenador().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId(rs.getInt(1));
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    public ProjetoExtensao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM projeto_extensao WHERE id = ?";
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

    public List<ProjetoExtensao> listarTodos() throws SQLException {
        List<ProjetoExtensao> lista = new ArrayList<>();
        String sql = "SELECT * FROM projeto_extensao";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally {
            ConexaoBD.fechar(conn);
        }
        return lista;
    }

    public void atualizar(ProjetoExtensao p) throws SQLException {
        String sql = "UPDATE projeto_extensao SET nome=?, descricao=?, id_coordenador=? WHERE id=?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setInt(3, p.getCoordenador().getId());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM projeto_extensao WHERE id = ?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    private ProjetoExtensao mapear(ResultSet rs) throws SQLException {
        ProjetoExtensao p = new ProjetoExtensao();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        Gestor g = new Gestor();
        g.setId(rs.getInt("id_coordenador"));
        p.setCoordenador(g);
        return p;
    }
}
