package server.api;

import commons.Debt;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import server.DebtService;
import server.database.DebtRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
     * All tests below again verify (in a simple case with no parameters) if method functions properly.
     */
    @Test
    public void testSaveDebt() {
        Debt debt = new Debt();

        when(debtRepository.save(debt)).thenReturn(debt);
        Debt result = debtService.saveDebt(debt);
        assertEquals(debt, result);
        verify(debtRepository).save(debt);
    }

    @Test
    void testSaveDebtException(){
        Debt debt = new Debt();
        when(debtRepository.save(debt)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> debtRepository.save(debt));
    }

    @Test
    void testSaveDebtException2(){
        Debt debt = new Debt();
        when(debtRepository.save(debt)).thenThrow(new ServiceException("Service exception"));
        assertThrows(ServiceException.class, () -> debtRepository.save(debt));
    }

    @Test
    void testSaveDebtException3(){
        Debt debt = new Debt();
        when(debtRepository.save(debt)).thenThrow(new DataAccessException("exception") {
        });
        assertThrows(DataAccessException.class, () -> debtRepository.save(debt));
    }

    @Test
    public void testFindDebtById() {
        Long id = 1L;
        Optional<Debt> expectedDebt = Optional.of(new Debt());

        when(debtRepository.findById(id)).thenReturn(expectedDebt);
        Optional<Debt> result = debtService.findDebtById(id);
        assertEquals(expectedDebt, result);
        verify(debtRepository).findById(id);
    }

    @Test
    public void testFindAllDebts() {
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());
        when(debtRepository.findAll()).thenReturn(expectedDebts);

        List<Debt> result = debtService.findAllDebts();
        assertEquals(expectedDebts, result);
        verify(debtRepository).findAll();
    }

    @Test
    public void testDeleteDebtById() {
        Long id = 1L;

        doNothing().when(debtRepository).deleteById(id);
        debtService.deleteDebtById(id);
        verify(debtRepository).deleteById(id);
    }

    @Test
    public void testDeleteDebtByIdExceptionNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> debtService.deleteDebtById(null));
        assertEquals("ID must be positive and not null", exception.getMessage());
    }

    @Test
    public void testDeleteDebtByIdException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> debtService.deleteDebtById(-1L));
        assertEquals("ID must be positive and not null", exception.getMessage());
    }

    @Test
    public void testDeleteDebtByIdServiceException() {
        doThrow(new RuntimeException("Failed to delete debt")).when(debtRepository).deleteById(1L);
        ServiceException exception = assertThrows(ServiceException.class, () ->
                debtService.deleteDebtById(1L));

        assertEquals("Error deleting debt", exception.getMessage());
    }

    @Test
    public void testFindDebtsByLender() {
        Long lenderId = 1L;
        List<Debt> expectedDebts = Arrays.asList(new Debt(), new Debt());

        when(debtRepository.lenderDebts(lenderId)).thenReturn(expectedDebts);
        List<Debt> resultDebts = debtService.findDebtsByLender(lenderId);
        assertEquals(expectedDebts, resultDebts);
        verify(debtRepository).lenderDebts(lenderId);
    }
    @Test
    void testFindDebtsByLenderIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByLender(-1L));
    }

    @Test
    void testFindDebtsByLenderServiceException() {
        when(debtRepository.lenderDebts(anyLong())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findDebtsByLender(anyLong()));
        assertEquals("Error finding debts by lender ID", exception.getMessage());
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
    void testFindDebtsByDebtorIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByDebtor(-1L));
    }

    @Test
    void testFindDebtsByDebtorServiceException() {
        when(debtRepository.debtorDebts(anyLong())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findDebtsByDebtor(anyLong()));
        assertEquals("Error finding debts by debtor ID", exception.getMessage());
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
    void testFindDebtsByCollectivityServiceException() {
        when(debtRepository.debtCollectivity(anyBoolean())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findDebtsByCollectivity(anyBoolean()));
        assertEquals("Error finding debts by collectivity", exception.getMessage());
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
    void testFindDebtsThroughDescriptionServiceException() {
        when(debtRepository.debtsThroughDescription(anyString())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findDebtsThroughDescription(anyString()));
        assertEquals("Error finding debts through description", exception.getMessage());
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
    void testFindCostlierDebtsServiceException() {
        when(debtRepository.costlierDebts(anyDouble())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findCostlierDebts(anyDouble()));
        assertEquals("Error finding costlier debts", exception.getMessage());
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

    @Test
    void testFindCheaperDebtsServiceException() {
        when(debtRepository.cheaperDebts(anyDouble())).thenThrow(new RuntimeException("Data access error"));

        ServiceException exception = assertThrows(ServiceException.class, () -> debtService.findCheaperDebts(anyDouble()));
        assertEquals("Error finding cheaper debts", exception.getMessage());
    }

    @Test
    public void testSaveDebtWithNullDebt() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.saveDebt(null));

        assertEquals("Debt not allowed to be null", exception.getMessage());
    }

    @Test
    public void testFindDebtByIdWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtById(null));
        assertThrows(IllegalArgumentException.class, () -> debtService.findDebtById(-1L));
    }


    @Test
    public void testFindAllDebtsWithRepositoryException() {
        when(debtRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(ServiceException.class, () -> debtService.findAllDebts());

        assertEquals("Error retrieving all debts", exception.getMessage());
    }
    @Test
    public void testDeleteDebtByIdWithInvalidId() {
        Long invalidId = -20L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.deleteDebtById(invalidId));

        assertEquals("ID must be positive and not null", exception.getMessage());
    }

    @Test
    public void testFindDebtsByLenderWithInvalidId() {
        Long invalidLenderId = -20L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByLender(invalidLenderId));

        assertEquals("Lender ID must be positive and not null", exception.getMessage());
    }
    @Test
    public void testFindDebtsByDebtorWithInvalidId() {
        Long invalidDebtorId = -20L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsByDebtor(invalidDebtorId));

        assertEquals("Debtor ID must be positive and not null", exception.getMessage());
    }

    @Test
    public void testFindDebtsThroughDescriptionWithNullDescription() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.findDebtsThroughDescription(null));

        assertEquals("Description must not be null", exception.getMessage());
    }
    @Test
    public void testFindCostlierDebtsWithNegativeAmount() {
        double invalidAmount = -1.00;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.findCostlierDebts(invalidAmount));

        assertEquals("Amount must not be negative", exception.getMessage());
    }

    @Test
    public void testFindCheaperDebtsWithNegativeAmount() {
        double invalidAmount = -50.00;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> debtService.findCheaperDebts(invalidAmount));

        assertEquals("Amount must not be negative", exception.getMessage());
    }

}
