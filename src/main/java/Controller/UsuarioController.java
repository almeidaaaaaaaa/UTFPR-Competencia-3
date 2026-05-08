package Controller;

import dao.UsuarioDAO;
import model.Administrador;
import model.Gestor;
import model.Usuario;
import model.UsuarioComum;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/usuario")
public class UsuarioController extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome  = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String tipo  = request.getParameter("tipo");

        try {
            Usuario u;
            switch (tipo != null ? tipo : "") {
                case "GESTOR":        u = new Gestor();        break;
                case "ADMINISTRADOR": u = new Administrador(); break;
                default:              u = new UsuarioComum();  break;
            }

            u.setNome(nome);
            u.setEmail(email);
            u.setSenha(senha);

            dao.inserir(u);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.sendRedirect(request.getContextPath() + "/index.html");

        } catch (SQLException e) {
            e.printStackTrace(); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao inserir usuario.");
        }
    }
}