package server.api;

import commons.Debt;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.DebtService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DebtControllerTest {

    @Mock
    private DebtService debtService;

    @InjectMocks
    private DebtController debtController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDebt() {
        Debt debt = new Debt();
        when(debtService.saveDebt(any(Debt.class))).thenReturn(debt);

        var response = debtController.saveDebt(debt);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(debtService).saveDebt(debt);
    }

    @Test
    void testFindDebtById() {
        Optional<Debt> optionalDebt = Optional.of(new Debt());
        when(debtService.findDebtById(anyLong())).thenReturn(optionalDebt);

        var response = debtController.findDebtById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(debtService).findDebtById(1L);
    }

    @Test
    void testFindAllDebts() {
        List<Debt> debts = Arrays.asList(new Debt(), new Debt());
        when(debtService.findAllDebts()).thenReturn(debts);

        var response = debtController.findAllDebts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(debtService).findAllDebts();
    }

    @Test
    void testFindAllDebtsServiceException() {
        when(debtService.findAllDebts()).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findAllDebts();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(debtService, times(1)).findAllDebts();
    }

    @Test
    void testDeleteDebtById() {
        doNothing().when(debtService).deleteDebtById(anyLong());

        var response = debtController.deleteDebtById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(debtService).deleteDebtById(1L);
    }
    @Test
    void testFindDebtsByLender() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findDebtsByLender(anyLong())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByLender(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }
    @Test
    void testFindDebtsByLenderServiceException() {
        when(debtService.findDebtsByLender(anyLong())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findDebtsByLender(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(debtService).findDebtsByLender(1L);
    }

    @Test
    void testFindDebtsByLenderIllegalArgumentException() {
        when(debtService.findDebtsByLender(anyLong())).thenThrow(new IllegalArgumentException("Invalid ID"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByLender(anyLong()));
    }

    @Test
    void testFindDebtsByDebtor() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findDebtsByDebtor(anyLong())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByDebtor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindDebtsByDebtorServiceException() {
        when(debtService.findDebtsByDebtor(anyLong())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findDebtsByDebtor(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(debtService).findDebtsByDebtor(1L);
    }

    @Test
    void testFindDebtsByDebtorIllegalArgumentException() {
        when(debtService.findDebtsByDebtor(anyLong())).thenThrow(new IllegalArgumentException("Invalid ID"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByDebtor(anyLong()));
    }

    @Test
    void testFindDebtsByCollectivity() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findDebtsByCollectivity(anyBoolean())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByCollectivity(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindDebtsByCollectivityServiceException() {
        when(debtService.findDebtsByCollectivity(anyBoolean())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findDebtsByCollectivity(anyBoolean());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(debtService).findDebtsByCollectivity(anyBoolean());
    }

    @Test
    void testFindDebtsByCollectivityIllegalArgumentException() {
        when(debtService.findDebtsByCollectivity(anyBoolean())).thenThrow(new IllegalArgumentException("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByCollectivity(anyBoolean()));
    }

    @Test
    void testFindDebtsThroughDescription() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findDebtsThroughDescription(anyString())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsThroughDescription("description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }
    @Test
    void testFindDebtsThroughDescriptionException() {
        when(debtService.findDebtsThroughDescription(anyString())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findDebtsThroughDescription(anyString());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(debtService).findDebtsThroughDescription(anyString());
    }

    @Test
    void testFindDebtsThroughDescriptionIllegalArgumentException() {
        when(debtService.findDebtsThroughDescription(anyString())).thenThrow(new IllegalArgumentException("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsThroughDescription(anyString()));
    }

    @Test
    void testFindCostlierDebts() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findCostlierDebts(anyDouble())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findCostlierDebts(100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindCostlierDebtsException() {
        when(debtService.findCostlierDebts(anyDouble())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findCostlierDebts(anyDouble());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(debtService).findCostlierDebts(anyDouble());
    }

    @Test
    void testFindCostlierDebtsIllegalArgumentException() {
        when(debtService.findCostlierDebts(anyDouble())).thenThrow(new IllegalArgumentException("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findCostlierDebts(anyDouble()));
    }

    @Test
    void testFindCheaperDebts() {
        List<Debt> expectedDebts = List.of(new Debt());
        when(debtService.findCheaperDebts(anyDouble())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findCheaperDebts(50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindCheaperDebtsException() {
        when(debtService.findCheaperDebts(anyDouble())).thenThrow(new ServiceException("Service exception"));

        ResponseEntity<?> response = debtController.findCheaperDebts(anyDouble());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(debtService).findCheaperDebts(anyDouble());
    }

    @Test
    void testFindCheaperDebtsIllegalArgumentException() {
        when(debtService.findCheaperDebts(anyDouble())).thenThrow(new IllegalArgumentException("Invalid"));
        assertThrows(IllegalArgumentException.class, () -> debtService.findCheaperDebts(anyDouble()));
    }
    @Test
    void testFindDebtByIdNotFound() {
        when(debtService.findDebtById( anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = debtController.findDebtById(1L);

        assertEquals(HttpStatus.NOT_FOUND,  response.getStatusCode());
        assertEquals("Debt not found with ID: 1", response.getBody());
    }

    @Test
    void testFindDebtByIdThrowsIllegalArgumentException() {
        when(debtService.findDebtById(anyLong())).thenThrow(new IllegalArgumentException("Invalid id"));

        ResponseEntity<?> response = debtController.findDebtById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid id", response.getBody());
    }

    @Test
    void testFindDebtByIdThrowsServiceException() {
        when(debtService.findDebtById(anyLong())).thenThrow(new ServiceException("Internal error"));

        ResponseEntity<?> response = debtController.findDebtById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to find debt by ID: Internal error", response.getBody());
    }

    @Test
    void testSaveDebtThrowsIllegalArgumentException() {
        Debt invalidDebt = new Debt(); // Assume this debt is somehow invalid
        when(debtService.saveDebt(any(Debt.class))).thenThrow(new IllegalArgumentException("Invalid debt details"));

        ResponseEntity<?> response = debtController.saveDebt(invalidDebt);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid debt details", response.getBody());
    }

    @Test
    void testSaveDebtThrowsServiceException() {
        Debt debt = new Debt();
        when(debtService.saveDebt(any(Debt.class))).thenThrow(new ServiceException("Database error"));

        ResponseEntity<?> response = debtController.saveDebt(debt);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to save debt: Database error", response.getBody());
    }

    @Test
    void testDeleteDebtByIdThrowsIllegalArgumentException() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(debtService).deleteDebtById(anyLong());

        ResponseEntity<?> response = debtController.deleteDebtById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody());
    }

    @Test
    void testDeleteDebtByIdThrowsServiceException() {
        doThrow(new ServiceException("Database error")).when(debtService).deleteDebtById(anyLong());

        ResponseEntity<?> response = debtController.deleteDebtById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete debt: Database error", response.getBody());
    }
}
