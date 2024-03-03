package server.api;

import commons.Debt;
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
    void testDeleteDebtById() {
        doNothing().when(debtService).deleteDebtById(anyLong());

        var response = debtController.deleteDebtById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(debtService).deleteDebtById(1L);
    }
    @Test
    void testFindDebtsByLender() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findDebtsByLender(anyLong())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByLender(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindDebtsByDebtor() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findDebtsByDebtor(anyLong())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByDebtor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindDebtsByCollectivity() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findDebtsByCollectivity(anyBoolean())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsByCollectivity(true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindDebtsThroughDescription() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findDebtsThroughDescription(anyString())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findDebtsThroughDescription("description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindCostlierDebts() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findCostlierDebts(anyDouble())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findCostlierDebts(100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }

    @Test
    void testFindCheaperDebts() {
        List<Debt> expectedDebts = Arrays.asList(new Debt());
        when(debtService.findCheaperDebts(anyDouble())).thenReturn(expectedDebts);

        ResponseEntity<?> response = debtController.findCheaperDebts(50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDebts, response.getBody());
    }


}
