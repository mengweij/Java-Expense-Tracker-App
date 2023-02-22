package ui;

import model.*;
import model.exception.InvalidInputException;
import model.exception.NullRecordException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

//Expense tracker App
public class ExpenseTrackerApp {
    private BalanceSheet bs;
    private Scanner input;
    private YearMonth currentYearMonth;

    private final String yearAndMonthFormat = "20[0-2]\\d-(0[0-9]|1[0-2])";
    private final Pattern yearAndMonthPattern = Pattern.compile("20[0-2]\\d-(0[0-9]|1[0-2])");
    private final Pattern datePattern = Pattern.compile("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    private final Pattern orderNumPattern = Pattern.compile("\\d|\\d\\d");
    private final NumberFormat numberFormatter = new DecimalFormat("#0.00");


    // EFFECTS: run this app
    public ExpenseTrackerApp() {
        runExpenseTracker();
    }

    //MODIFIES: this
    //EFFECTS: process user input
    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command;

        try {
            initialize();

            while (keepGoing) {
                displayMainMenu();
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("q")) {
                    keepGoing = false;
                } else {
                    processMainMenu(command);
                }
            }
        } catch (InvalidInputException e) {
            System.out.println("Oops! Invalid input. Try again!");
        }

        System.out.println("\nLook forward to see your next record!");
    }

    //MODIFIES: this
    //EFFECT: initialize a balance sheet
    private void initialize() {
        bs = new BalanceSheet();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    //EFFECTS: display the main menu for users
    private void displayMainMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("\ta -> add a new transaction");
        System.out.println("\ts -> see some statistics");
        System.out.println("\tl -> look back at your records or edit them");
        System.out.println("\tq -> quit");
    }

    //EFFECTS: process user's command on main menu
    private void processMainMenu(String command) throws InvalidInputException {
        switch (command) {
            case "a":
                doAddRecord();
                break;
            case "s":
                doShowStat();
                break;
            case "l":
                doShowRecords();
                break;
            default:
                invalidInput();
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: add a transaction record to the balance sheet
    private void doAddRecord() {
        System.out.println("\nTo add: expense or income?");
        System.out.println("\te -> expense");
        System.out.println("\ti -> income");

        String commandA = input.next();
        commandA = commandA.toLowerCase();

        if (commandA.equals("e")) {
            try {
                doAddExpense();
            } catch (NullRecordException e) {
                doAddRecord();
            }
        } else if (commandA.equals("i")) {
            try {
                doAddIncome();
            } catch (NullRecordException e) {
                doAddRecord();
            }
        } else {
            invalidInput();
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add an income record
    private void doAddIncome() throws NullRecordException {
        System.out.println("Enter how much you earn: $");

        double amount;
        try {
            amount = Double.parseDouble(input.next());

            Income newIncome = new Income(amount);
            newIncome = doClassifyIncome(newIncome);
            if (newIncome == null) {
                throw new NullRecordException();
            }

            boolean statusOfAddAmt = bs.addRecord(newIncome);
            if (statusOfAddAmt) {
                String res = "Successfully added! Your current balance is $"
                        + numberFormatter.format(bs.getBalance());
                System.out.println(res);
            } else {
                invalidInput();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new income record
    private Income doClassifyIncome(Income newIncome) {
        System.out.println("What category is this earning? Enter one of: ");
        for (IncomeCategory i : IncomeCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();

        IncomeCategory categoryNormalized;
        try {
            categoryNormalized = IncomeCategory.valueOf(category);
            for (IncomeCategory i : IncomeCategory.values()) {
                if (i == categoryNormalized) {
                    newIncome.classify(i);
                    return newIncome;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid category. Try again!");
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: add an expense record
    private void doAddExpense() throws NullRecordException {
        System.out.println("Enter how much you cost: $");

        double amount;
        try {
            amount = Double.parseDouble(input.next());
            Expense newExpense = new Expense(amount);
            doClassifyExpense(newExpense);
            if (newExpense == null) {
                throw new NullRecordException();
            }

            boolean statusOfAddAmt = bs.addRecord(newExpense);
            if (statusOfAddAmt) {
                String res = "Successfully added! Your current balance is $"
                        + numberFormatter.format(bs.getBalance());
                System.out.println(res);
            } else {
                invalidInput();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new expense record
    private void doClassifyExpense(Expense newExpense) {
        System.out.println("What category is this expenditure? Enter one of: ");
        for (ExpenseCategory i : ExpenseCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();
        ExpenseCategory categoryNormalized;
        try {
            categoryNormalized = ExpenseCategory.valueOf(category);
            for (ExpenseCategory i : ExpenseCategory.values()) {
                if (categoryNormalized == i) {
                    newExpense.classify(i);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Not a valid category. Try again!");
        }
    }

    //EFFECTS: display all records of current month
    private void doShowRecords() {
        currentYearMonth = YearMonth.now();
        String currentYearMonth = this.currentYearMonth.toString();

        processShowRecordsMenu(currentYearMonth);
    }

    //MODIFIES: this
    //EFFECTS: process user's command on records menu
    private void processShowRecordsMenu(String yearAndMonth) {
        System.out.println("Here is all records of " + yearAndMonth + ".");
        doDisplayMonthlyRecord(yearAndMonth);
        recordsMenu();

        String commandS;
        commandS = input.next();
        commandS = commandS.toLowerCase();

        if (commandS.matches(yearAndMonthFormat)) {
            processShowRecordsMenu(commandS);
        } else if ("e".equals(commandS)) {
            doEditOneExpense(yearAndMonth);
        } else if ("i".equals(commandS)) {
            doEditOneIncome(yearAndMonth);
        } else {
            invalidInput();
        }
    }

    //EFFECTS: display records menu for users
    private void recordsMenu() {
        System.out.println("\nTo edit: expense or income?");
        System.out.println("\te -> expense");
        System.out.println("\ti -> income");
        System.out.println("Or look at records of other month?");
        System.out.println("\tEnter in the format of 'yyyy-mm'");
        System.out.println("Any other keys back to main menu.");
    }

    //MODIFIES: this
    //EFFECTS: allow users to pick up one income which is wanted to be edited
    private void doEditOneIncome(String yearAndMonth) {
        String command;
        List<Record> incomeList = bs.listByMonth("income", yearAndMonth);
        doDisplayIncome(incomeList);

        if (incomeList.isEmpty()) {
            System.out.println("Have a record first!");
        } else {
            System.out.println("Which one do you want to edit? Enter the order number.");
            if (input.hasNext(orderNumPattern)) {
                command = input.next();
                Record target = bs.fetchIncome(Integer.parseInt(command));
                if (target != null) {
                    doDisplayOneRecord(target);
                    editMenu();
                    processEditIncome(target);
                } else {
                    invalidInput();
                }
            } else {
                invalidInput();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: process user's input on income edit menu
    private void processEditIncome(Record record) {
        switch (input.next()) {
            case "a":
                doResetAmount(record);
                break;
            case "d":
                doResetDate(record);
                break;
            case "c":
                doClassifyIncome((Income) record);
                break;
            case "delete":
                boolean status = bs.deleteRecord(record);
                if (status) {
                    System.out.println("Successfully deleted.");
                } else {
                    System.out.println("Oops! Something goes wrong!");
                }
                break;
            default:
                invalidInput();
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: allow users to pick up one expense which is wanted to be edited
    private void doEditOneExpense(String yearAndMonth) {
        String commendE;
        List<Record> expenseList = bs.listByMonth("expense", yearAndMonth);
        doDisplayExpense(expenseList);

        if (expenseList.isEmpty()) {
            System.out.println("Have a record first!");
        } else {
            System.out.println("Which one do you want to edit? Enter the order number.");
            if (input.hasNext(orderNumPattern)) {
                commendE = input.next();
                Record target = bs.fetchExpense(Integer.parseInt(commendE));
                if (target != null) {
                    doDisplayOneRecord(target);
                    editMenu();
                    processEditExpense(target);
                } else {
                    invalidInput();
                }
            } else {
                invalidInput();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: process user's input on expense edit menu
    private void processEditExpense(Record record) {
        switch (input.next()) {
            case "a":
                doResetAmount(record);
                break;
            case "d":
                doResetDate(record);
                break;
            case "c":
                doClassifyExpense((Expense) record);
                break;
            case "delete":
                boolean status = bs.deleteRecord(record);
                if (status) {
                    System.out.println("Successfully deleted.");
                } else {
                    System.out.println("Oops! Something goes wrong!");
                }
                break;
            default:
                invalidInput();
                break;
        }
    }

    //MODIFIES: this
    //EFFECTS: allow users to reset the date of one record
    private void doResetDate(Record record) {
        System.out.println("The new date (after 2000): ");
        System.out.println("Enter in the format of 'yyyy-mm-dd'");

        try {
            String newDate;
            newDate = input.next();
            record.resetDate(newDate);
            System.out.println("Successfully reset the date!");
            doDisplayOneRecord(record);
        } catch (DateTimeParseException e) {
            System.out.println("Please enter in right format.");
        }

    }

    //MODIFIES: this
    //EFFECTS: allow users to reset the amount of one record
    private void doResetAmount(Record record) {
        System.out.println("The new amount: ");

        double amount;

        try {
            amount = Double.parseDouble(input.next());
            record.resetAmount(amount);
            System.out.println("Successfully reset the amount!");
            doDisplayOneRecord(record);
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numbers.");
            doShowRecords();
        }
    }

    //EFFECTS: display the record editing menu for users
    private void editMenu() {
        System.out.println("What to change for this record?");
        System.out.println("\ta -> amount");
        System.out.println("\td -> date");
        System.out.println("\tc -> category");
        System.out.println("\nOr just delete it? -> delete");
        System.out.println("Any other keys back to main menu.");
    }

    //EFFECTS: show statistic data of the balance sheet
    private void doShowStat() {
        currentYearMonth = YearMonth.now();
        String currentYearMonth = this.currentYearMonth.toString();
        processStatMenu(currentYearMonth);
    }

    //MODIFIES: this
    //EFFECTS: process user's command on statistics menu
    private void processStatMenu(String yearAndMonth) {
        statisticsMenu(yearAndMonth);

        String commandS;
        commandS = input.next();
        commandS = commandS.toLowerCase();

        switch (commandS) {
            case "m":
                doShowMonthStat(yearAndMonth);
                break;
            case "o":
                doShowOtherMonthStat();
                break;
        }
    }

    //EFFECTS: display statistics menu for users
    private void statisticsMenu(String yearAndMonth) {
        String res = "In " + yearAndMonth + ", your total balance is $"
                + numberFormatter.format(bs.totalBalanceByMonth(yearAndMonth))
                + "\n" + "\tTotal expense: "
                + numberFormatter.format(bs.totalExpenseByMonth(yearAndMonth))
                + "\tTotal income: "
                + numberFormatter.format(bs.totalIncomeByMonth(yearAndMonth));
        System.out.println(res);

        System.out.println("\nKnow more about this month? -> m");
        System.out.println("Look at other month? -> o");
        System.out.println("Any other keys back to main menu.");
    }

    //MODIFIES: this
    //EFFECTS: display statistic data of current months
    private void doShowMonthStat(String yearAndMonth) {
        StringBuilder res = new StringBuilder();

        LocalDate date = LocalDate.now();
        int daysPast = date.getDayOfMonth();
        double averageExp = bs.totalExpenseByMonth(yearAndMonth) / daysPast;
        double averageInc = bs.totalIncomeByMonth(yearAndMonth) / daysPast;
        res.append("In ").append(yearAndMonth).append(", your daily average expense is ");
        res.append(numberFormatter.format(averageExp));
        res.append(";\nand the daily average income is ");
        res.append(numberFormatter.format(averageInc));
        res.append(".\n");
        if (averageExp == averageInc) {
            if (averageExp == 0) {
                res.append("No data can be analysed.");
            } else {
                res.append("Your are perfectly balanced!");
            }
        } else if (averageExp > averageInc) {
            res.append("Try to be more economic!");
        } else {
            res.append("Well done!");
        }
        System.out.println(res);
    }

    //MODIFIES: this
    //EFFECTS: display statistic data of a given month
    private void doShowOtherMonthStat() {
        System.out.println("At which year and month you wanna have a look?");
        System.out.println("Enter in the format of 'yyyy-mm'");

        if (input.hasNext(yearAndMonthPattern)) {
            String commandM = input.next();
            doShowMonthStat(commandM);
        }
    }

    //EFFECTS: display the required record information
    private void doDisplayOneRecord(Record record) {
        String res = ">>> "
                + record.getTempID()
                + ". " + record.getDate()
                + " "
                + numberFormatter.format(record.getAmount())
                + " "
                + record.getCategory();
        System.out.println(res);
    }

    //EFFECTS: display all records of a given month,
    // and lists are sorted by timeID
    private void doDisplayMonthlyRecord(String yearAndMonth) {
        List<Record> expenseList = bs.listByMonth("expense", yearAndMonth);
        List<Record> incomeList = bs.listByMonth("income", yearAndMonth);

        expenseList.sort(Comparator.comparing(Record::getTimeID));
        incomeList.sort(Comparator.comparing(Record::getTimeID));

        doDisplayExpense(expenseList);
        doDisplayIncome(incomeList);
    }

    //EFFECTS: display income records of a given month
    private void doDisplayIncome(List<Record> incomeList) {
        StringBuilder incStr = new StringBuilder();
        int orderNum = 1;

        if (incomeList.isEmpty()) {
            System.out.println("No income record.");
        } else {
            System.out.println("Income:");
            for (Record i : incomeList) {
                i.setTempID(orderNum++);
                incStr.append(i.getTempID());
                incStr.append(". ").append(i.getDate()).append(" ");
                incStr.append(numberFormatter.format(i.getAmount()));
                incStr.append(" ").append(i.getCategory());
                incStr.append("\n");
            }
            System.out.println(incStr);
        }
    }

    //EFFECTS: display expense records of a given month
    private void doDisplayExpense(List<Record> expenseList) {
        StringBuilder epStr = new StringBuilder();
        int orderNum = 1;

        if (expenseList.isEmpty()) {
            System.out.println("No expense record.");
        } else {
            System.out.println("Expense:");
            for (Record i : expenseList) {
                i.setTempID(orderNum++);
                epStr.append(i.getTempID());
                epStr.append(". ").append(i.getDate()).append(" ");
                epStr.append(numberFormatter.format(i.getAmount()));
                epStr.append(" ").append(i.getCategory());
                epStr.append("\n");
            }
            System.out.println(epStr);
        }
    }

    //EFFECTS: return an invalid input message
    private void invalidInput() {
        System.out.println("Oops! Invalid input. Try again!");
    }

}
