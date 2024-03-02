package server.api;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.DebtService;

import java.util.*;

//instead of just controller annotation, making it restful(just as a build up of the Web Section of WDT course)
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
    public ResponseEntity<List<Debt>> findDebtsByCollectivity(@RequestParam boolean debtCollective) {
        List<Debt> debts = debtService.findDebtsByCollectivity(debtCollective);
        return ResponseEntity.ok(debts);
    }

    /**
     * finding debts through description
     * @param description as a String
     * @return ResponseEntity<List<Debt>>
     */
    @GetMapping("/description")
    public ResponseEntity<List<Debt>> findDebtsThroughDescription(@RequestParam String description) {
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
