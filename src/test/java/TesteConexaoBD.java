import Controller.ConexaoBD;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class TesteConexaoBD {

    @Test
    public void testConexao() {
        Connection conn = ConexaoBD.conectar();
        assertNotNull(conn, "A conexao deve ser diferente de null");
    }
}
