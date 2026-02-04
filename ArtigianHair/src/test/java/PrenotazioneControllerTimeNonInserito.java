import com.artigianhair.bean.AppuntamentoBean;
import com.artigianhair.controller.PrenotazioneController;
import com.artigianhair.engineering.exception.PrenotazioneException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrenotazioneControllerMissingTimeTest {

    @Test
    void testConfermaAppuntamentoMissingTimeSlot() {
        PrenotazioneController controller = new PrenotazioneController();
        AppuntamentoBean bean = new AppuntamentoBean();

        bean.setData("20");
        bean.setMese("Giugno");
        bean.setClienteEmail("cliente@esempio.com");
        PrenotazioneException exception = assertThrows(PrenotazioneException.class, () -> {
            controller.confermaAppuntamento(bean);
        }, "Dovrebbe lanciare PrenotazioneException se l'orario non è specificato.");

        assertEquals("Fascia oraria non specificata.", exception.getMessage());
    }
}
