package client.scenes;

import commons.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {


    /**
     * TBA
     */
    @Test
    void importEvent() {
        Event event = new Event("chilling");
        AdminController adminController = new AdminController();
        adminController.exportEvent(event);

    }
    @Test
    void exportEvent() {
        Event event = new Event("chilling");
        AdminController adminController = new AdminController();
        adminController.exportEvent(event);
    }

}