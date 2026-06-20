package Controller;

import dao.ProjetoExtensaoDAO;
import model.ProjetoExtensao;
import model.Usuario;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/projetos")
public class ProjetoController extends HttpServlet {

    private final ProjetoExtensaoDAO dao = new ProjetoExtensaoDAO();

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

        try {
            List<ProjetoExtensao> projetos = dao.listarTodos();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < projetos.size(); i++) {
                ProjetoExtensao p = projetos.get(i);
                String desc = p.getDescricao() != null ? p.getDescricao().replace("\"", "\\\"") : "";
                sb.append("{")
                  .append("\"id\":").append(p.getId()).append(",")
                  .append("\"nome\":\"").append(p.getNome().replace("\"", "\\\"")).append("\",")
                  .append("\"descricao\":\"").append(desc).append("\"")
                  .append("}");
                if (i < projetos.size() - 1) sb.append(",");
            }
            sb.append("]");
            response.getWriter().write(sb.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("[]");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Não autorizado.");
            return;
        }

        String tipo = (String) session.getAttribute("usuarioTipo");
        if (!"ADMINISTRADOR".equals(tipo)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Sem permissão.");
            return;
        }

        String nome        = request.getParameter("nome");
        String descricao   = request.getParameter("descricao");
        String idCoordeStr = request.getParameter("idCoordenador");

        if (nome == null || nome.isBlank() || idCoordeStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Nome e coordenador são obrigatórios.");
            return;
        }

        try {
            int idCoordenador = Integer.parseInt(idCoordeStr);

            ProjetoExtensao p = new ProjetoExtensao();
            p.setNome(nome);
            p.setDescricao(descricao);

            Usuario coordenador = new Usuario() {};
            coordenador.setId(idCoordenador);
            p.setCoordenador(coordenador);

            dao.inserir(p);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("ok");

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao salvar projeto.");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID do coordenador inválido.");
        }
    }
}
