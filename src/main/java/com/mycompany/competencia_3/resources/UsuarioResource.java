package com.mycompany.competencia_3.resources;

import dao.UsuarioDAO;
import model.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * RF01 - Cadastro e Login de Usuários.
 *
 * Endpoints:
 *   POST   /api/usuarios          → cadastrar novo usuário
 *   GET    /api/usuarios          → listar todos (admin)
 *   GET    /api/usuarios/{id}     → buscar por ID
 *   PUT    /api/usuarios/{id}     → atualizar dados
 *   DELETE /api/usuarios/{id}     → remover usuário
 *   POST   /api/usuarios/login    → autenticar (email + senha)
 */
@Path("usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioDAO dao = new UsuarioDAO();

    // ── POST /api/usuarios ────────────────────────────────────────────────────
    @POST
    public Response cadastrar(Usuario usuario) {
        try {
            dao.inserir(usuario);
            return Response.status(Response.Status.CREATED).entity(usuario).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/usuarios ─────────────────────────────────────────────────────
    @GET
    public Response listarTodos() {
        try {
            List<Usuario> lista = dao.listarTodos();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── GET /api/usuarios/{id} ────────────────────────────────────────────────
    @GET
    @Path("{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Usuario u = dao.buscarPorId(id);
            if (u == null) return Response.status(Response.Status.NOT_FOUND)
                                          .entity("{\"erro\":\"Usuário não encontrado\"}")
                                          .build();
            return Response.ok(u).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── PUT /api/usuarios/{id} ────────────────────────────────────────────────
    @PUT
    @Path("{id}")
    public Response atualizar(@PathParam("id") int id, Usuario usuario) {
        try {
            usuario.setId(id);
            dao.atualizar(usuario);
            return Response.ok(usuario).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── DELETE /api/usuarios/{id} ─────────────────────────────────────────────
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

    // ── POST /api/usuarios/login ──────────────────────────────────────────────
    /**
     * Corpo esperado: { "email": "...", "senha": "..." }
     */
    @POST
    @Path("login")
    public Response login(LoginRequest req) {
        try {
            Usuario u = dao.buscarPorEmailSenha(req.getEmail(), req.getSenha());
            if (u == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("{\"erro\":\"Email ou senha inválidos\"}")
                               .build();
            }
            return Response.ok(u).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    // ── Classe auxiliar para deserializar o corpo do login ────────────────────
    public static class LoginRequest {
        private String email;
        private String senha;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    private Response erroInterno(SQLException e) {
        e.printStackTrace();
        return Response.serverError()
                       .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                       .build();
    }
}
