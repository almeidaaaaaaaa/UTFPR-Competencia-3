package com.mycompany.competencia_3.resources;

import dao.ProjetoExtensaoDAO;
import model.ProjetoExtensao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * Endpoints para gerenciar Projetos de Extensão.
 *
 *   POST   /api/projetos      → criar projeto
 *   GET    /api/projetos      → listar todos
 *   GET    /api/projetos/{id} → buscar por ID
 *   PUT    /api/projetos/{id} → atualizar
 *   DELETE /api/projetos/{id} → remover
 */
@Path("projetos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjetoExtensaoResource {

    private final ProjetoExtensaoDAO dao = new ProjetoExtensaoDAO();

    @POST
    public Response criar(ProjetoExtensao projeto) {
        try {
            dao.inserir(projeto);
            return Response.status(Response.Status.CREATED).entity(projeto).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    @GET
    public Response listarTodos() {
        try {
            List<ProjetoExtensao> lista = dao.listarTodos();
            return Response.ok(lista).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    @GET
    @Path("{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            ProjetoExtensao p = dao.buscarPorId(id);
            if (p == null) return Response.status(Response.Status.NOT_FOUND)
                                          .entity("{\"erro\":\"Projeto não encontrado\"}")
                                          .build();
            return Response.ok(p).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

    @PUT
    @Path("{id}")
    public Response atualizar(@PathParam("id") int id, ProjetoExtensao projeto) {
        try {
            projeto.setId(id);
            dao.atualizar(projeto);
            return Response.ok(projeto).build();
        } catch (SQLException e) {
            return erroInterno(e);
        }
    }

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

    private Response erroInterno(SQLException e) {
        e.printStackTrace();
        return Response.serverError()
                       .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                       .build();
    }
}
