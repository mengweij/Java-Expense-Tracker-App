package ui;

import model.BalanceSheet;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Expense tracker UI
public class ExpenseTrackerUI extends JFrame {
    private BackgroundDesktopPane desktop;
    private JInternalFrame mainMenu;
    private JInternalFrame addExpenseMenu;
    private JInternalFrame reviewMenu;
    private BalanceSheet bs;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static final String JSON_STORE_ADDRESS = "./data/balancesheet.json";

    // EFFECTS: set up the desktop with a main menu and background image
    public ExpenseTrackerUI() {
        bs = new BalanceSheet();
        jsonWriter = new JsonWriter(JSON_STORE_ADDRESS);
        jsonReader = new JsonReader(JSON_STORE_ADDRESS);
        addExpenseMenu = new AddExpenseUI(bs, ExpenseTrackerUI.this);
        reviewMenu = new ReviewUI(bs, ExpenseTrackerUI.this);

        try {
            Image backgroundImage = ImageIO.read(new File("./resources/background.jpg"));
            desktop = new BackgroundDesktopPane(backgroundImage);
        } catch (IOException ex) {
            desktop.setBackground(new Color(255,255,255));
        }
        desktop.addMouseListener(new DesktopFocusAction());

        setContentPane(desktop);
        setTitle("Your Expense Tracker");
        setSize(WIDTH, HEIGHT);

        setMainMenu();
        desktop.add(mainMenu);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        centreOnScreen();
        setVisible(true);

    }

    // MODIFIES: this
    // EFFECTS: set up the main menu
    private void setMainMenu() {
        mainMenu = new JInternalFrame("Main Menu", false, false, false, false);
        mainMenu.setLayout(new BorderLayout());

        addButtons();
        mainMenu.pack();
        mainMenu.setVisible(true);

        int x = WIDTH - mainMenu.getWidth();
        int y = 0;
        mainMenu.setLocation(x,y);
    }

    // MODIFIES: this
    // EFFECTS: add functional buttons
    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(new JButton(new AddExpenseAction()));
        buttonPanel.add(new JButton(new ReviewAction()));
        buttonPanel.add(new JButton(new SaveDataAction()));
        buttonPanel.add(new JButton(new LoadDataAction()));
        buttonPanel.add(new JButton(new QuitAction()));

        mainMenu.add(buttonPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: center main application window on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }


    // Starts the application
    public static void main(String[] args) {
        new ExpenseTrackerUI();
    }


    // Represents action to be taken when the user wants to add a new expense to the balance sheet
    private class AddExpenseAction extends AbstractAction {
        AddExpenseAction() {
            super("Add Expense");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            addExpenseMenu = new AddExpenseUI(bs, ExpenseTrackerUI.this);
            desktop.add(addExpenseMenu);
        }
    }

    // Represents action to be taken when the user wants to review all expenses in the balance sheet
    private class ReviewAction extends AbstractAction {
        ReviewAction() {
            super("Review My Records");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            reviewMenu = new ReviewUI(bs, ExpenseTrackerUI.this);
            desktop.add(reviewMenu);
        }
    }

    // Represents action to be taken when the user wants to save data
    private class SaveDataAction extends AbstractAction {
        SaveDataAction() {
            super("Save My Records");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(bs);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,"Saved your data to " + JSON_STORE_ADDRESS);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Unable to write to file: "
                        + JSON_STORE_ADDRESS, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Represents action to be taken when the user wants to load the saved data
    private class LoadDataAction extends AbstractAction {
        LoadDataAction() {
            super("Load My Records");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                bs = jsonReader.read();
                JOptionPane.showMessageDialog(null, "Loaded your data from " + JSON_STORE_ADDRESS);
                if (!reviewMenu.isClosed()) {
                    reviewMenu.setClosed(true);
                }
                if (!addExpenseMenu.isClosed()) {
                    addExpenseMenu.setClosed(true);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: "
                        + JSON_STORE_ADDRESS, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (PropertyVetoException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // Represents action to be taken when the user wants to quit the program
    private class QuitAction extends AbstractAction {
        QuitAction() {
            super("Quit");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (Event event : EventLog.getInstance()) {
                System.out.println(event.toString());
            }
            System.exit(0);

        }
    }

    // Represents DesktopPane allowing for setting a background image
    private class BackgroundDesktopPane extends JDesktopPane {
        private final Image backgroundImage;

        public BackgroundDesktopPane(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Represents action to be taken when user clicks desktop to switch focus.
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            ExpenseTrackerUI.this.requestFocusInWindow();
        }
    }

}
