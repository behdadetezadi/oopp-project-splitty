package server.api;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.DebtService;

import java.util.*;

//instead of just controller annotation, making it restful
// (just as a build up of the Web Section of WDT course)
@RestController
@RequestMapping("/api/debts") // our base endpoint
public class DebtController {

    private final DebtService debtService;

    /**
     * dependency injection through constructor
     * @param debtService DebtService
     */
    @Autowired
    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    /**
     * we create or update a specific debt according
     * to its id and we use proper http status after done.
     * @param debt Debt
     * @return ResponseEntity<Debt>
     */
    @PostMapping
    public ResponseEntity<Debt> saveDebt(@RequestBody Debt debt) {
        Debt savedDebt = debtService.saveDebt(debt);
        return new ResponseEntity<>(savedDebt, HttpStatus.CREATED);
    }

    /**
     * we find a certain debt according to its id if it exists
     * @param id as a long number
     * @return ResponseEntity<Debt>
     */
    @GetMapping("/{id}")
    public ResponseEntity<Debt> findDebtById(@PathVariable Long id) {
        Optional<Debt> debt = debtService.findDebtById(id);
        return debt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * find all debts that there are without any parameters
     * @return response entity of array lists of debts
     */
    @GetMapping
    public ResponseEntity<List<Debt>> findAllDebts() {
        List<Debt> debts = debtService.findAllDebts();
        return ResponseEntity.ok(debts);
    }

    /**
     * delete a debt according to its id
     * @param id as a long number
     * @return  ResponseEntity<Void> (so just void in general)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebtById(@PathVariable Long id) {
        debtService.deleteDebtById(id);
        return ResponseEntity.noContent().build(); // Use noContent for delete operations
    }


    /**
     * finding debts by the lender
     * @param lenderId provided as a long number
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<List<Debt>> findDebtsByLender(@PathVariable Long lenderId) {
        List<Debt> debts = debtService.findDebtsByLender(lenderId);
        return ResponseEntity.ok(debts);
    }
    /**
     * finding debts by the debtor
     * @param debtorId provided as a long number
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/debtor/{debtorId}")
    public ResponseEntity<List<Debt>> findDebtsByDebtor(@PathVariable Long debtorId) {
        List<Debt> debts = debtService.findDebtsByDebtor(debtorId);
        return ResponseEntity.ok(debts);
    }

    /**
     * finding debts according to their collectivity
     * @param debtCollective as a true/false boolean
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/collective")
    public ResponseEntity<List<Debt>>
        findDebtsByCollectivity(@RequestParam boolean debtCollective) {
        List<Debt> debts = debtService.findDebtsByCollectivity(debtCollective);
        return ResponseEntity.ok(debts);
    }

    /**
     * finding debts through description
     * @param description as a String
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/description")
    public ResponseEntity<List<Debt>>
        findDebtsThroughDescription(@RequestParam String description) {
        List<Debt> debts = debtService.findDebtsThroughDescription(description);
        return ResponseEntity.ok(debts);
    }

    /**
     * find debts costlier than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/costlier")
    public ResponseEntity<List<Debt>> findCostlierDebts(@RequestParam double amount) {
        List<Debt> debts = debtService.findCostlierDebts(amount);
        return ResponseEntity.ok(debts);
    }
    /**
     * find debts cheaper than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/cheaper")
    public ResponseEntity<List<Debt>> findCheaperDebts(@RequestParam double amount) {
        List<Debt> debts = debtService.findCheaperDebts(amount);
        return ResponseEntity.ok(debts);
    }

}
