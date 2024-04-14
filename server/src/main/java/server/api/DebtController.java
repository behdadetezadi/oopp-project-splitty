package server.api;

import commons.Debt;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.DebtService;
import server.database.DebtRepository;

import java.util.*;

//instead of just controller annotation, making it restful
// (just as a build up of the Web Section of WDT course)
@RestController
@RequestMapping("/api/debts") // our base endpoint
public class DebtController {

    private final DebtService debtService;
    private DebtRepository db;

    /**
     * dependency injection through constructor
     * @param debtService DebtService
     * @param db DebtRepository
     */
    @Autowired
    public DebtController(DebtService debtService, DebtRepository db) {
        this.debtService = debtService;
        this.db=db;
    }

    /**
     * we create or update a specific debt according
     * to its id and we use proper http status after done.
     * @param debt Debt
     * @return ResponseEntity<Debt> or error message
     */
    @PostMapping("/saveDebt")
    public ResponseEntity<?> saveDebt(@RequestBody Debt debt) {
        try {
            Debt savedDebt = debtService.saveDebt(debt);
//            db.save(debt);
            return new ResponseEntity<>(savedDebt, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            // extra error handling in all methods will be added to help dubugging if necessary
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to save debt: " + e.getMessage(),
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
            Optional<Debt> debtOptional = debtService.findDebtById(id);
            if (debtOptional.isPresent()) {
                return ResponseEntity.ok(debtOptional.get());
            } else {
                return new ResponseEntity<>("Debt not found with ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to find debt by ID: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find all debts that there are without any parameters
     * @return response entity of array lists of debts or error message
     */

    @GetMapping("/findAllDebts")
    public ResponseEntity<?> findAllDebts() {
        try {
            List<Debt> debts = debtService.findAllDebts();
            return ResponseEntity.ok(debts);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to retrieve all debts: " + e.getMessage(),
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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Failed to delete debt: " + e.getMessage(),
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
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find debts by lender: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
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
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find debts by debtor: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * finding debts according to their collectivity
     * @param debtCollective as a true/false boolean
     * @return ResponseEntity<List<Debt>> or error message
     */

    @GetMapping("/collective/{debtCollective}")
    public ResponseEntity<?> findDebtsByCollectivity(@PathVariable boolean debtCollective) {
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

    @GetMapping("/description/{description}")
    public ResponseEntity<?> findDebtsThroughDescription(@PathVariable String description) {
        try {
            List<Debt> debts = debtService.findDebtsThroughDescription(description);
            return ResponseEntity.ok(debts);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find debts by description: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * find debts costlier than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>> or error message
     */
    @GetMapping("/costlier/{amount}")
    public ResponseEntity<?> findCostlierDebts(@PathVariable double amount) {
        try {
            List<Debt> debts = debtService.findCostlierDebts(amount);
            return ResponseEntity.ok(debts);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find costlier debts: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
            // instead of internal server errors (for several examples btw), we have bad request
        }
    }
    /**
     * find debts cheaper than the provided number
     * @param amount as a double
     * @return ResponseEntity<List<Debt>> or error message
     */
    @GetMapping("/cheaper/{amount}")
    public ResponseEntity<?> findCheaperDebts(@PathVariable double amount) {
        try {
            List<Debt> debts = debtService.findCheaperDebts(amount);
            return ResponseEntity.ok(debts);
        } catch (IllegalArgumentException | ServiceException e) {
            return new ResponseEntity<>("Failed to find cheaper debts: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
