import src.ExpenseType;

import java.sql.Date;
import java.util.List;

public class ExpenseController
{
    //The person who paid the expense
    String paidPerson;
    //The use of the expense
    String usage;
    //The amount of expense
    Double amount;
    //The unit of expense
    String unit;
    //Time when the expense get paid
    Date time;
    //The way of split
    Boolean EquallyAttributed;
    Boolean LimitedPeople;
    //The liat of people who need to pay the bill
    List<String>peopleWhoNeedToPay;
    // The type of expense
    Enum<ExpenseType>expenseType;
    


}
