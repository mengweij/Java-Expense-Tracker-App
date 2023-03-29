package ui;

import model.BalanceSheet;
import model.Record;
import ui.exception.InvalidInputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Represents visual user interface for reviewing all saved records.
 */
public class ReviewUI extends JInternalFrame {
    private JLabel periodLabel;
    private JLabel totalLabel;
    private JTextField periodField;
    private JButton enterButton;
    private JList<String> expenseList;
    private DefaultListModel<String> expenseModel;
    private BalanceSheet bs;
    private double totalExpenseByMonth;

    private static final String yearAndMonthFormat = "20[0-2]\\d-(0[0-9]|1[0-2])";
    private static final NumberFormat numberFormatter = new DecimalFormat("#0.00");

    public ReviewUI(BalanceSheet bs, Component parent) {
        super("Review Expenses", false, true, false, false);
        this.bs = bs;
        totalExpenseByMonth = 0.0;

        setSize(parent.getWidth(), (int) (parent.getHeight() * 0.4));
        setPosition(parent);
        setVisible(true);

        periodLabel = new JLabel("Period (YYYY-MM):");
        periodField = new JTextField();
        enterButton = new JButton(new ReviewRecordsAction());

        JPanel inputPanel = new JPanel(new GridLayout(1,3));
        inputPanel.add(periodLabel);
        inputPanel.add(periodField);
        inputPanel.add(enterButton);

        expenseModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseModel);
        JScrollPane scrollPane = new JScrollPane(expenseList);

        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(scrollPane, BorderLayout.CENTER);
        totalLabel = new JLabel(String.format("Total Expense: $%.2f", totalExpenseByMonth));
        expensePanel.add(totalLabel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(expensePanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the position of this review UI relative to parent component
     * @param parent  the parent component
     */
    private void setPosition(Component parent) {
        setLocation(0, parent.getHeight() / 2);
    }


    private class ReviewRecordsAction extends AbstractAction {
        ReviewRecordsAction() {
            super("Enter");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String yearAndMonth = periodField.getText();
            try {
                if (yearAndMonth.matches(yearAndMonthFormat)) {
                    totalExpenseByMonth = bs.totalExpenseByMonth(yearAndMonth);
                    totalLabel.setText(String.format("Total Expense: $%.2f", totalExpenseByMonth));
                    List<Record> expenseRecordList = bs.listByMonth("expense", yearAndMonth);
                    doDisplayExpense(expenseRecordList);
                } else {
                    throw new InvalidInputException("Please enter in the format YYYY-MM."
                            + "\nOnly support to review records after 2000.");
                }
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                periodField.setText("");
            }
        }

//        EFFECTS: display expense records of a given month
        private void doDisplayExpense(List<Record> expenseList) {
            expenseModel.clear();

            int orderNum = 1;

            if (expenseList.isEmpty()) {
                expenseModel.addElement("No expense in this month!");
            } else {
                for (Record i : expenseList) {
                    StringBuilder epStr = new StringBuilder();
                    i.setTempID(orderNum++);
                    epStr.append(i.getTempID());
                    epStr.append(". ").append(i.getDate()).append(" ");
                    epStr.append(numberFormatter.format(i.getAmount()));
                    epStr.append(" ").append(i.getCategoryName());
                    expenseModel.addElement(String.valueOf(epStr));
                }
            }
        }

    }
}
