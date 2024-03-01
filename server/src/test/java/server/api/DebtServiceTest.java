package server.api;

import server.DebtService;
import server.database.DebtRepository;
import commons.Debt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DebtServiceTest {
    //using mockito for testing.

    @Mock
    private DebtRepository debtRepository;

    @InjectMocks
    private DebtService debtService;

    /**
     * we initialize the mock objects (from mockito) before actually doing each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     *this is a very simple test and all below use mockito to verify how well the method in the service class works.
     * All tests below again verify (in a simple case) if method functions properly.
     */
    @Test
    public void testFindDebtsByLender() {
        Long lenderId = 1L; //lender id 1 in form of a long
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.lenderDebts(lenderId)).thenReturn(expectedDebts);
        List<Debt> resultDebts = debtService.findDebtsByLender(lenderId);
        assertEquals(expectedDebts, resultDebts);
        verify(debtRepository).lenderDebts(lenderId);
    }

    @Test
    public void testFindDebtsByDebtor() {
        Long debtorId = 1L;
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.debtorDebts(debtorId)).thenReturn(expectedDebts);
        List<Debt> resultDebts = debtService.findDebtsByDebtor(debtorId);
        assertEquals(expectedDebts, resultDebts);
        verify(debtRepository).debtorDebts(debtorId);
    }

    @Test
    public void testFindDebtsByCollectivity() {
        boolean debtCollective = true;
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.debtCollectivity(debtCollective)).thenReturn(expectedDebts);
        List<Debt> result = debtService.findDebtsByCollectivity(debtCollective);
        assertEquals(expectedDebts, result);
        verify(debtRepository).debtCollectivity(debtCollective);
    }
    @Test
    public void testFindDebtsThroughDescription() {
        String description = "BurgerFood debt";
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.debtsThroughDescription(description)).thenReturn(expectedDebts);
        List<Debt> result = debtService.findDebtsThroughDescription(description);
        assertEquals(expectedDebts, result);
        verify(debtRepository).debtsThroughDescription(description);
    }

    @Test
    public void testFindCostlierDebts() {
        double amount = 100.00;
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.costlierDebts(amount)).thenReturn(expectedDebts);
        List<Debt> result = debtService.findCostlierDebts(amount);
        assertEquals(expectedDebts, result);
        verify(debtRepository).costlierDebts(amount);
    }

    @Test
    public void testFindCheaperDebts() {
        double amount = 50.00;
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.cheaperDebts(amount)).thenReturn(expectedDebts);
        List<Debt> result = debtService.findCheaperDebts(amount);
        assertEquals(expectedDebts, result);
        verify(debtRepository).cheaperDebts(amount);
    }
}