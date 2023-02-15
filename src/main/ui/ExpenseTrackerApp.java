package ui;

import model.*;
import model.Exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

//Expense tracker App
public class ExpenseTrackerApp {
    private BalanceSheet bs;
    private Scanner input;
    private YearMonth yearMonth;
    private LocalDate date;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    //TODO: improve the regex
    private final Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}");

    // EFFECTS: run this app
    public ExpenseTrackerApp() {
        runExpenseTracker();
    }

    //MODIFIES: this
    //EFFECTS: process user input
    public void runExpenseTracker() {
        boolean keepGoing = true;
        String command = null;

        try {
            initialize();

            while (keepGoing) {
                displayMenu();
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
    private void displayMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("\tn -> add a new transaction");
        System.out.println("\tl -> look through past records");
        System.out.println("\tq -> quit");
    }

    //MODIFIES: this
    //EFFECTS: process user's command on main menu
    private void processMainMenu(String command) throws InvalidInputException {
        if (command.equals("n")) {
            doAddRecord();
        } else if (command.equals("l")) {
            doStatistics();
        } else {
            invalidInput();
        }
    }

    //EFFECTS: return an invalid input alarm
    private void invalidInput() {
        System.out.println("Oops! Invalid input. Try again!");
    }

    //MODIFIES: this
    //EFFECTS: add a transaction record to the balance sheet
    private void doAddRecord() {
        System.out.println("\nExpense or Income?");
        System.out.println("\te -> expense");
        System.out.println("\ti -> income");

        String commandA = input.next();
        commandA = commandA.toLowerCase();

        if (commandA.equals("e")) {
            doAddExpense();
        } else if (commandA.equals("i")) {
            doAddIncome();
        } else {
            invalidInput();
            doAddRecord();
        }
    }

    //MODIFIES: this
    //EFFECTS: add an income record
    private void doAddIncome() {
        System.out.println("Enter how much you earn: $");

        double amount = Double.parseDouble(input.next());
        Income newIncome = new Income(amount);
        newIncome = doClassifyIncome(newIncome);

        boolean statusOfAddAmt = bs.addRecord(newIncome);
        if (statusOfAddAmt) {
            System.out.println("Successfully added! Current balance is $" + bs.getBalance());
        } else {
            invalidInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new income record
    private Income doClassifyIncome(Income newIncome) {
        System.out.println("What category is this earning? Type in one of: ");
        for (IncomeCategory i : IncomeCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();
        IncomeCategory categoryNormalized = IncomeCategory.valueOf(category);
        for (IncomeCategory i : IncomeCategory.values()) {
            if (i == categoryNormalized) {
                newIncome.classify(i);
                return newIncome;
            }
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: add an expense record
    private void doAddExpense() {
        System.out.println("Enter how much you cost: $");

        double amount = Double.parseDouble(input.next());
        Expense newExpense = new Expense(amount);
        newExpense = doClassifyExpense(newExpense);

        boolean statusOfAddAmt = bs.addRecord(newExpense);
        if (statusOfAddAmt) {
            System.out.println("Successfully added! Current balance is $" + bs.getBalance());
        } else {
            invalidInput();
        }
    }

    //MODIFIES: this
    //EFFECTS: add category to the new expense record
    private Expense doClassifyExpense(Expense newExpense) {
        System.out.println("What category is this expenditure? Type in one of: ");
        for (ExpenseCategory i : ExpenseCategory.values()) {
            System.out.println(i);
        }
        String category = input.next();
        category = category.toUpperCase();
        ExpenseCategory categoryNormalized = ExpenseCategory.valueOf(category);
        for (ExpenseCategory i : ExpenseCategory.values()) {
            if (categoryNormalized == i) {
                newExpense.classify(i);
                return newExpense;
            }
        }
        return null;
    }

    //MODIFIES: this
    //EFFECTS: show statistic data of the balance sheet
    private void doStatistics() {
        String commandS;
        commandS = input.next();
        commandS = commandS.toLowerCase();

        yearMonth = YearMonth.now();
        String currentYearMonth = yearMonth.toString();

        statisticsMenu();

        if (commandS.equals("a")) {
            doDisplayMonthlyRecord(currentYearMonth);
        } else if (commandS.equals("m")) {
            doThisMonthStat(currentYearMonth);
        } else if (commandS.equals("o")) {
            doMonthStat();
        }
    }

    //EFFECTS: display the statistics menu for users
    private void statisticsMenu() {
        yearMonth = YearMonth.now();
        String currentYearMonth = yearMonth.toString();

        System.out.println("In this month, your total balance is $" + bs.totalBalanceByMonth(currentYearMonth) + ":");
        System.out.println("\tTotal expense: " + bs.totalExpenseByMonth(currentYearMonth));
        System.out.println("\tTotal income: " + bs.totalIncomeByMonth(currentYearMonth));
        System.out.println("\nSee all records of this month? -> a");
        System.out.println("Know more about this month? -> m");
        System.out.println("Look at other month? -> o");
        System.out.println("Any other keys back to main menu.");
    }

    private void doThisMonthStat(String currentYearMonth) {
        StringBuilder res = new StringBuilder();
        int daysPast = date.getDayOfMonth();
        double averageExp = bs.totalExpenseByMonth(currentYearMonth) / daysPast;
        double averageInc = bs.totalIncomeByMonth(currentYearMonth) / daysPast;
        res.append("In " + currentYearMonth + ", your daily average expense is ");
        res.append(averageExp);
        res.append(";\nand the daily average income is ");
        res.append(averageInc);
        res.append(".");
        if (averageExp == averageInc) {
            res.append("Your are perfectly balanced!");
        } else if (averageExp > averageInc) {
            res.append("Try to be more economic!");
        } else {
            res.append("Well done!");
        }
        System.out.println(res);
    }

    //MODIFIES: this
    //EFFECTS: show statistic data of a given month
    private void doMonthStat() {
        boolean keepGoing = true;

        System.out.println("At which year and month you wanna have a look?");
        System.out.println("Type in the format of 'yyyy-mm'");

        while (keepGoing) {
            if (input.hasNext(pattern)) {
                String commandM = input.next();
                System.out.println("In " + commandM + ", your balance is " + bs.totalBalanceByMonth(commandM) + ":");
                System.out.println("\tTotal expense: " + bs.totalExpenseByMonth(commandM));
                System.out.println("\tTotal income: " + bs.totalIncomeByMonth(commandM));
                doDisplayMonthlyRecord(commandM);
            } else {
                invalidInput();
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: display all records of a given month
    private void doDisplayMonthlyRecord(String yyyymm) {
        List<Record> expenseList = bs.listByMonth("expense", yyyymm);
        List<Record> incomeList = bs.listByMonth("income", yyyymm);

        doDisplayExpense(expenseList);
        System.out.println("\n");
        doDisplayIncome(incomeList);
    }

    //MODIFIES: this
    //EFFECTS: display income records of a given month
    //TODO: sorting the list
    private void doDisplayIncome(List<Record> incomeList) {
        StringBuilder incStr = new StringBuilder();
        int orderNum = 1;

        if (incomeList.isEmpty()) {
            System.out.println("No income record.");
        } else {
            System.out.println("All income records:");
            for (Record i : incomeList) {
                incStr.append(orderNum++);
                incStr.append(". " + i.getDate() + " ");
                incStr.append(i.getAmount());
                incStr.append(" " + i.getCategory());
            }
            System.out.println(incStr);
        }
    }

    //MODIFIES: this
    //EFFECTS: display expense records of a given month
    //TODO: sorting the list
    private void doDisplayExpense(List<Record> expenseList) {
        StringBuilder epStr = new StringBuilder();
        int orderNum = 1;

        if (expenseList.isEmpty()) {
            System.out.println("No expense record.");
        } else {
            System.out.println("All expense records:");
            for (Record i : expenseList) {
                epStr.append(orderNum++);
                epStr.append(". " + i.getDate() + " ");
                epStr.append(i.getAmount());
                epStr.append(" " + i.getCategory());
            }
            System.out.println(epStr);
        }
    }

}
