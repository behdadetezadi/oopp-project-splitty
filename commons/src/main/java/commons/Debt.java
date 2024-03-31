package commons;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Participant debtor;
    @ManyToOne
    private Participant lender;
    @Column(name = "amount_of_money", nullable = false)
    private double amountOfMoney;
    private boolean debtCollective;
    private String description;

    /**
     * constructor for debt
     * @param debtor Participant
     * @param lender Participant
     * @param amountOfMoney double
     * @param debtCollective boolean
     * @param description string
     */
    public Debt(Participant debtor, Participant lender, double amountOfMoney,
                boolean debtCollective, String description) {
        this.debtor = debtor;
        this.lender = lender;
        this.amountOfMoney = amountOfMoney;
        this.debtCollective = debtCollective;
        this.description = description;
    }

    /**
     * Partial Constructor for debtor with fewer (optional) fields
     * @param debtor Participant who owes money
     * @param lender Participant who lends money
     * @param amountOfMoney amount of money lent
     */
    public Debt(Participant debtor, Participant lender, double amountOfMoney) {
        this.debtor = debtor;
        this.lender = lender;
        setAmountOfMoney(amountOfMoney); // we are using a setter because it has a negative check
        this.debtCollective = false; // by default
        this.description = ""; //again by default
    }

    /**
     * constructor used by the jackson methods
     * @param id the id of a debt
     * @param debtor the debtor
     * @param lender the lender
     * @param amountOfMoney the amount of money owed
     * @param debtCollective the debt collective
     * @param description the debt description
     */
    public Debt(long id, Participant debtor, Participant lender,
                double amountOfMoney, boolean debtCollective, String description) {
        this.id = id;
        this.debtor = debtor;
        this.lender = lender;
        this.amountOfMoney = amountOfMoney;
        this.debtCollective = debtCollective;
        this.description = description;
    }

    /**
     * empty constructor added for persistence.
     */
    public Debt() {}

    /**
     * getter for the id
     * @return a long formatted number.
     */
    public long getId() {
        return id;
    }

    /**
     * getter for debtor
     * @return participant
     */
    public Participant getDebtor() {
        return debtor;
    }
    /**
     * getter for lender
     * @return participant
     */
    public Participant getLender() {
        return lender;
    }
    /**
     * getter for money owed
     * @return double
     */
    public double getAmountOfMoney() {
        return amountOfMoney;
    }
    /**
     * getter for checking if debt is collective (otherwise it's partial)
     * @return boolean
     */
    public boolean isDebtCollective() {
        return debtCollective;
    }
    /**
     * getter for description of debt
     * @return string
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter for debtor
     * @param debtor
     */
    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    /**
     * setter for lender
     * @param lender
     */
    public void setLender(Participant lender) {
        this.lender = lender;
    }

    /**
     * setter for money owed
     * @param amountOfMoney double
     */
    public void setAmountOfMoney(double amountOfMoney) {
        if(amountOfMoney < 0) {
            throw new IllegalArgumentException("Amount of money can't be negative!");
        }
        this.amountOfMoney = amountOfMoney;
    }


    /**
     * collective debt setter
     * @param debtCollective
     */
    public void setDebtCollective(boolean debtCollective) {
        this.debtCollective = debtCollective;
    }

    /**
     * description setter
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * equals method
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (Double.compare(amountOfMoney, debt.amountOfMoney) != 0) return false;
        if (debtCollective != debt.debtCollective) return false;
        if (!debtor.equals(debt.debtor) || !lender.equals(debt.lender)) return false;
        return Objects.equals(description, debt.description);
    }

    /**
     * unique hashcode generator
     * @return  int
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = debtor.hashCode();
        result = 31 * result + lender.hashCode();
        temp = Double.doubleToLongBits(amountOfMoney);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (debtCollective ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    /**
     * human understandable to string method
     * @return a string that displays all relevant information about debt.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String type;
        if (debtCollective) {
            type = "Collective";
        } else {
            type = "Partial";
        }
        String debtDescription;
        if (description.isEmpty()) {
            debtDescription = "No description provided";
        } else {
            debtDescription = description;
        }

        sb.append("Debt Details:\n");
        sb.append("  Debtor: ").append(debtor.getFirstName()).append("\n");
        sb.append("  Creditor: ").append(lender.getFirstName()).append("\n");
        sb.append("  Amount: $").append(amountOfMoney).append("\n");
        sb.append("  Debt Type: ").append(type).append("\n");
        sb.append("  Description: ").append(debtDescription);

        return sb.toString();
    }

}
