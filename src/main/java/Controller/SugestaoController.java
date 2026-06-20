package Controller;

import dao.ProjetoExtensaoDAO;
import dao.SugestaoDAO;
import dao.UsuarioDAO;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/sugestoes/*")
public class SugestaoController extends HttpServlet {

    private final SugestaoDAO sugestaoDAO         = new SugestaoDAO();
    private final ProjetoExtensaoDAO projetoDAO   = new ProjetoExtensaoDAO();
    private final UsuarioDAO usuarioDAO            = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if ("/status".equals(pathInfo)) {
            alterarStatus(request, response);
        } else {
            criarSugestao(request, response);
        }
    }

    private void criarSugestao(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Você precisa estar logado.");
            return;
        }

        String titulo    = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        String idProjStr = request.getParameter("idProjeto");

        if (titulo == null || titulo.isBlank() || descricao == null || descricao.isBlank() || idProjStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Preencha todos os campos.");
            return;
        }

        try {
            int idProponente = (int) session.getAttribute("usuarioId");
            int idProjeto    = Integer.parseInt(idProjStr);

            Usuario proponente = usuarioDAO.buscarPorId(idProponente);
            ProjetoExtensao projeto = projetoDAO.buscarPorId(idProjeto);

            if (projeto == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Projeto não encontrado.");
                return;
            }

            Sugestao s = new Sugestao();
            s.setTitulo(titulo);
            s.setDescricao(descricao);
            s.setStatus(StatusSugestao.PENDENTE);
            s.setDataEnvio(LocalDateTime.now());
            s.setProponente(proponente);
            s.setProjetoVinculado(projeto);

            sugestaoDAO.inserir(s);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("ok");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao salvar sugestão.");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID de projeto inválido.");
        }
    }

    private void alterarStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Não autorizado.");
            return;
        }

        String tipo = (String) session.getAttribute("usuarioTipo");
        if (!"GESTOR".equals(tipo) && !"ADMINISTRADOR".equals(tipo)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Sem permissão.");
            return;
        }

        try {
            int id                  = Integer.parseInt(request.getParameter("id"));
            StatusSugestao novoStatus = StatusSugestao.valueOf(request.getParameter("status"));
            String justificativa    = request.getParameter("justificativa");

            sugestaoDAO.atualizarStatus(id, novoStatus, justificativa);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("ok");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao atualizar status.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parâmetros inválidos.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Não autorizado.");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        try {
            List<Sugestao> lista;

            if ("/minhas".equals(pathInfo)) {
                int idUsuario = (int) session.getAttribute("usuarioId");
                lista = sugestaoDAO.listarPorProponente(idUsuario);
            } else {
                String idProjStr = request.getParameter("idProjeto");
                if (idProjStr != null && !idProjStr.isBlank()) {
                    lista = sugestaoDAO.listarPorProjeto(Integer.parseInt(idProjStr));
                } else {
                    lista = sugestaoDAO.listarTodas();
                }
            }

            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < lista.size(); i++) {
                Sugestao s = lista.get(i);
                // Busca nome do proponente
                Usuario proponente = usuarioDAO.buscarPorId(s.getProponente().getId());
                String nomeProponente = proponente != null ? proponente.getNome() : "Desconhecido";
                // Busca nome do projeto
                ProjetoExtensao proj = projetoDAO.buscarPorId(s.getProjetoVinculado().getId());
                String nomeProjeto = proj != null ? proj.getNome() : "Desconhecido";

                String dataFormatada = s.getDataEnvio().toString().replace("T", " ").substring(0, 16);
                String justif = s.getJustificativa() != null ? s.getJustificativa().replace("\"", "\\\"") : "";

                sb.append("{")
                  .append("\"id\":").append(s.getId()).append(",")
                  .append("\"titulo\":\"").append(s.getTitulo().replace("\"", "\\\"")).append("\",")
                  .append("\"descricao\":\"").append(s.getDescricao().replace("\"", "\\\"")).append("\",")
                  .append("\"status\":\"").append(s.getStatus().name()).append("\",")
                  .append("\"dataEnvio\":\"").append(dataFormatada).append("\",")
                  .append("\"nomeProponente\":\"").append(nomeProponente.replace("\"", "\\\"")).append("\",")
                  .append("\"nomeProje\":\"").append(nomeProjeto.replace("\"", "\\\"")).append("\",")
                  .append("\"justificativa\":\"").append(justif).append("\"")
                  .append("}");
                if (i < lista.size() - 1) sb.append(",");
            }
            sb.append("]");
            response.getWriter().write(sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("[]");
        }
    }
}
