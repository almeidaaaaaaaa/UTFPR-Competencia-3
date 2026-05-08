package dao;

import Controller.ConexaoBD;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco de dados para a entidade Sugestao.
 * RF02 - Submissão de Ideia
 * RF03 - Painel de Gestão (alteração de status)
 * RF04 - Histórico de Sugestões
 */
public class SugestaoDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────

    public void inserir(Sugestao s) throws SQLException {
        String sql = "INSERT INTO sugestao (titulo, descricao, status, data_envio, "
                   + "id_proponente, id_projeto) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getTitulo());
            ps.setString(2, s.getDescricao());
            ps.setString(3, s.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(s.getDataEnvio()));
            ps.setInt(5, s.getProponente().getId());
            ps.setInt(6, s.getProjetoVinculado().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) s.setId(rs.getInt(1));
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    public Sugestao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM sugestao WHERE id = ?";
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

    /** RF04 - lista todas as sugestões de um usuário específico. */
    public List<Sugestao> listarPorProponente(int idProponente) throws SQLException {
        List<Sugestao> lista = new ArrayList<>();
        String sql = "SELECT * FROM sugestao WHERE id_proponente = ? ORDER BY data_envio DESC";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProponente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally {
            ConexaoBD.fechar(conn);
        }
        return lista;
    }

    /** RF03 - lista todas as sugestões de um projeto para o gestor. */
    public List<Sugestao> listarPorProjeto(int idProjeto) throws SQLException {
        List<Sugestao> lista = new ArrayList<>();
        String sql = "SELECT * FROM sugestao WHERE id_projeto = ? ORDER BY data_envio DESC";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjeto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } finally {
            ConexaoBD.fechar(conn);
        }
        return lista;
    }

    public List<Sugestao> listarTodas() throws SQLException {
        List<Sugestao> lista = new ArrayList<>();
        String sql = "SELECT * FROM sugestao ORDER BY data_envio DESC";
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

    /** RF03 - Gestor altera o status da sugestão. */
    public void atualizarStatus(int id, StatusSugestao novoStatus,
                                String justificativa) throws SQLException {
        String sql = "UPDATE sugestao SET status=?, justificativa=? WHERE id=?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus.name());
            ps.setString(2, justificativa);
            ps.setInt(3, id);
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM sugestao WHERE id = ?";
        Connection conn = ConexaoBD.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            ConexaoBD.fechar(conn);
        }
    }

    // ── Mapeamento ResultSet → Objeto ─────────────────────────────────────────

    private Sugestao mapear(ResultSet rs) throws SQLException {
        Sugestao s = new Sugestao();
        s.setId(rs.getInt("id"));
        s.setTitulo(rs.getString("titulo"));
        s.setDescricao(rs.getString("descricao"));
        s.setStatus(StatusSugestao.valueOf(rs.getString("status")));
        s.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());
        s.setJustificativa(rs.getString("justificativa"));

        // Referências leves (apenas com ID) — carregue completo se necessário
        UsuarioComum proponente = new UsuarioComum();
        proponente.setId(rs.getInt("id_proponente"));
        s.setProponente(proponente);

        ProjetoExtensao projeto = new ProjetoExtensao();
        projeto.setId(rs.getInt("id_projeto"));
        s.setProjetoVinculado(projeto);

        return s;
    }
}
