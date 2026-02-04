import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.PrenotazioneException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrenotazioneControllerInvalidMonthTest {

    @Test
    void testConfermaAppuntamentoInvalidMonth() {
        PrenotazioneController controller = new PrenotazioneController();
        AppuntamentoBean bean = new AppuntamentoBean();

        bean.setData("10");
        bean.setMese("MeseInesistente");
        bean.setOrario("P");
        bean.setClienteEmail("user@test.it");

        PrenotazioneException exception = assertThrows(PrenotazioneException.class, () -> {
            controller.confermaAppuntamento(bean);
        }, "Dovrebbe lanciare PrenotazioneException per un mese non valido.");

        assertEquals("Mese non valido.", exception.getMessage());
    }
}
