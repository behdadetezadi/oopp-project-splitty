package commons;
import java.util.*;
public class Debt {
    private Participant debtor;
    private Participant lender;
    private double amountOfMoney;
    private boolean debtCollective;
    private String description;

    /**
     * constructor
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
}
