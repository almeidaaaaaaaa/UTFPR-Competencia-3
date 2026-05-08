package com.mycompany.competencia_3.resources;

import dao.SugestaoDAO;
import model.Sugestao;
import model.StatusSugestao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * RF02 - Submissão de Ideia
 * RF03 - Painel de Gestão (alterar status)
 * RF04 - Histórico de Sugestões
 *
 * Endpoints:
 *   POST   /api/sugestoes                           → enviar nova sugestão
 *   GET    /api/sugestoes                           → listar todas (admin/gestor)
 *   GET    /api/sugestoes/{id}                      → buscar por ID
 *   GET    /api/sugestoes/proponente/{idUsuario}    → histórico do usuário (RF04)
 *   GET    /api/sugestoes/projeto/{idProjeto}       → painel do gestor (RF03)
 *   PATCH  /api/sugestoes/{id}/status               → alterar status (RF03)
 *   DELETE /api/sugestoes/{id}                      → remover sugestão
 */
@Path("sugestoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SugestaoResource {

    private final SugestaoDAO dao = new SugestaoDAO();

    // ── POST /api/sugestoes ───────────────────────────────────────────────────
    @POST
    public Response enviar(Sugestao sugestao) {
        try {
            dao.inserir(sugestao);
            return Response.status(Response.Status.CREATED).entity(sugestao).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/sugestoes ────────────────────────────────────────────────────
    @GET
    public Response listarTodas() {
        try {
            List<Sugestao> lista = dao.listarTodas();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/sugestoes/{id} ───────────────────────────────────────────────
    @GET
    @Path("{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Sugestao s = dao.buscarPorId(id);
            if (s == null) return naoEncontrado();
            return Response.ok(s).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/sugestoes/proponente/{idUsuario} ─────────────────────────────
    /** RF04 - Histórico de sugestões do usuário logado. */
    @GET
    @Path("proponente/{idUsuario}")
    public Response historicoPorProponente(@PathParam("idUsuario") int idUsuario) {
        try {
            List<Sugestao> lista = dao.listarPorProponente(idUsuario);
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/sugestoes/projeto/{idProjeto} ────────────────────────────────
    /** RF03 - Painel do gestor: sugestões de um projeto. */
    @GET
    @Path("projeto/{idProjeto}")
    public Response listarPorProjeto(@PathParam("idProjeto") int idProjeto) {
        try {
            List<Sugestao> lista = dao.listarPorProjeto(idProjeto);
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── PATCH /api/sugestoes/{id}/status ──────────────────────────────────────
    /**
     * RF03 - Gestor aprova/recusa uma sugestão.
     * Corpo esperado: { "status": "APROVADA", "justificativa": "..." }
     */
    @PATCH
    @Path("{id}/status")
    public Response alterarStatus(@PathParam("id") int id, AlterarStatusRequest req) {
        try {
            StatusSugestao novoStatus = StatusSugestao.valueOf(req.getStatus());
            dao.atualizarStatus(id, novoStatus, req.getJustificativa());
            return Response.ok("{\"mensagem\":\"Status atualizado com sucesso\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"erro\":\"Status inválido. Use: PENDENTE, EM_ANALISE, APROVADA, RECUSADA\"}")
                           .build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── DELETE /api/sugestoes/{id} ────────────────────────────────────────────
    @DELETE
    @Path("{id}")
    public Response deletar(@PathParam("id") int id) {
        try {
            dao.deletar(id);
            return Response.noContent().build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── Classe auxiliar para alterar status ───────────────────────────────────
    public static class AlterarStatusRequest {
        private String status;
        private String justificativa;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getJustificativa() { return justificativa; }
        public void setJustificativa(String j) { this.justificativa = j; }
    }

    private Response naoEncontrado() {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"erro\":\"Sugestão não encontrada\"}")
                       .build();
    }

    private Response erroInterno(SQLException e) {
        e.printStackTrace();
        return Response.serverError()
                       .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                       .build();
    }
}
