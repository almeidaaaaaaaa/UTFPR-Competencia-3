package Controller;

import dao.UsuarioDAO;
import model.TipoUsuario;
import model.Usuario;
import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/login")
public class LoginController extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try {
            Usuario u = dao.buscarPorEmailSenha(email, senha);

            if (u == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("E-mail ou senha incorretos.");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioId", u.getId());
            session.setAttribute("usuarioNome", u.getNome());
            session.setAttribute("usuarioTipo", u.getTipo().name());

            String destino;
            if (u.getTipo() == TipoUsuario.ADMINISTRADOR) {
                destino = request.getContextPath() + "/admin.html";
            } else if (u.getTipo() == TipoUsuario.GESTOR) {
                destino = request.getContextPath() + "/gestor.html";
            } else {
                destino = request.getContextPath() + "/usuario.html";
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(destino);

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro interno ao fazer login.");
        }
    }
}
