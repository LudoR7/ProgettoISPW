import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.PrenotazioneController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrenotazioneControllerTest {

    @Test
    void testConfermaAppuntamentoSuccess() {
        PrenotazioneController controller = new PrenotazioneController();
        AppuntamentoBean bean = new AppuntamentoBean();

        bean.setData("15");
        bean.setMese("Maggio");
        bean.setOrario("M");
        bean.setClienteEmail("test@example.com");
        bean.addTrattamento("Taglio");
        bean.addTrattamento("Piega");

        assertDoesNotThrow(() -> {
            controller.confermaAppuntamento(bean);
        }, "La prenotazione con dati validi dovrebbe avere successo.");
    }
}
