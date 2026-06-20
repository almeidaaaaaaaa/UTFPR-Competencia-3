package Controller;

import dao.UsuarioDAO;
import model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/usuarios")
public class UsuariosController extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String tipo = (String) session.getAttribute("usuarioTipo");
        if (!"ADMINISTRADOR".equals(tipo)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Usuario> usuarios = dao.listarTodos();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                sb.append("{")
                  .append("\"id\":").append(u.getId()).append(",")
                  .append("\"nome\":\"").append(u.getNome().replace("\"", "\\\"")).append("\",")
                  .append("\"email\":\"").append(u.getEmail().replace("\"", "\\\"")).append("\",")
                  .append("\"tipo\":\"").append(u.getTipo().name()).append("\"")
                  .append("}");
                if (i < usuarios.size() - 1) sb.append(",");
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String tipo = (String) session.getAttribute("usuarioTipo");
        if (!"ADMINISTRADOR".equals(tipo)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("ID não informado.");
            return;
        }

        try {
            dao.deletar(Integer.parseInt(idStr));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("ok");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao excluir.");
        }
    }
}
