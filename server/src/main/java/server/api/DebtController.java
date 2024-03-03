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
     * @return ResponseEntity<Debt> or error message
     */
    @PostMapping
    public ResponseEntity<?> saveDebt(@RequestBody Debt debt) {
        try {
            Debt savedDebt = debtService.saveDebt(debt);
            return new ResponseEntity<>(savedDebt, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save the debt.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * we find a certain debt according to its id if it exists
     * @param id as a long number
     * @return ResponseEntity<Debt> or error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findDebtById(@PathVariable Long id) {
        try {
            return debtService.findDebtById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find the debt by ID.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find all debts that there are without any parameters
     * @return response entity of array lists of debts or error message
     */

    @GetMapping
    public ResponseEntity<?> findAllDebts() {
        try {
            List<Debt> debts = debtService.findAllDebts();
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve all debts.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * delete a debt according to its id
     * @param id as a long number
     * @return  ResponseEntity<Void> (so just void in general) or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDebtById(@PathVariable Long id) {
        try {
            debtService.deleteDebtById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete the debt.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * finding debts by the lender
     * @param lenderId provided as a long number
     * @return ResponseEntity<List<Debt>> or error message
     */

    @GetMapping("/lender/{lenderId}")
    public ResponseEntity<?> findDebtsByLender(@PathVariable Long lenderId) {
        try {
            List<Debt> debts = debtService.findDebtsByLender(lenderId);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find debts by lender.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * finding debts by the debtor
     * @param debtorId provided as a long number
     * @return ResponseEntity<List<Debt>> or error message
     */
    @GetMapping("/debtor/{debtorId}")
    public ResponseEntity<?> findDebtsByDebtor(@PathVariable Long debtorId) {
        try {
            List<Debt> debts = debtService.findDebtsByDebtor(debtorId);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find debts by debtor.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * finding debts according to their collectivity
     * @param debtCollective as a true/false boolean
     * @return ResponseEntity<List<Debt>> or error message
     */

    @GetMapping("/collective")
    public ResponseEntity<?> findDebtsByCollectivity(@RequestParam boolean debtCollective) {
        try {
            List<Debt> debts = debtService.findDebtsByCollectivity(debtCollective);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find debts by collectivity.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * finding debts through description
     * @param description as a String
     * @return ResponseEntity<List<Debt>> or error message
     */

    @GetMapping("/description")
    public ResponseEntity<?> findDebtsThroughDescription(@RequestParam String description) {
        try {
            List<Debt> debts = debtService.findDebtsThroughDescription(description);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find debts through description.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find debts costlier than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>> or error message
     */
    @GetMapping("/costlier")
    public ResponseEntity<?> findCostlierDebts(@RequestParam double amount) {
        try {
            List<Debt> debts = debtService.findCostlierDebts(amount);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find costlier debts.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * find debts cheaper than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>> or error message
     */
    @GetMapping("/cheaper")
    public ResponseEntity<?> findCheaperDebts(@RequestParam double amount) {
        try {
            List<Debt> debts = debtService.findCheaperDebts(amount);
            return ResponseEntity.ok(debts);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to find cheaper debts.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
