package ui;

import model.BalanceSheet;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExpenseTrackerUI extends JFrame {
    private BackgroundDesktopPane desktop;
    private JInternalFrame mainMenu;
    private BalanceSheet bs;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static final String JSON_STORE_ADDRESS = "./data/balancesheet.json";

    public ExpenseTrackerUI() {
        bs = new BalanceSheet();
        jsonWriter = new JsonWriter(JSON_STORE_ADDRESS);
        jsonReader = new JsonReader(JSON_STORE_ADDRESS);

        try {
            Image backgroundImage = ImageIO.read(new File("./resources/background.jpg"));
            desktop = new BackgroundDesktopPane(backgroundImage);
        } catch (IOException ex) {
            desktop.setBackground(new Color(255,255,255));
        }
        desktop.addMouseListener(new DesktopFocusAction());

        mainMenu = new JInternalFrame("Main Menu", false, false, false, false);
        mainMenu.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Your Expense Tracker");
        setSize(WIDTH, HEIGHT);

        addButtons();

        mainMenu.pack();
        mainMenu.setVisible(true);
        desktop.add(mainMenu);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5,1));
        buttonPanel.add(new JButton(new AddExpenseAction()));
        buttonPanel.add(new JButton("Add Income"));
        buttonPanel.add(new JButton(new ReviewAction()));
        buttonPanel.add(new JButton(new SaveDataAction()));
        buttonPanel.add(new JButton(new LoadDataAction()));
//        buttonPanel.add(new JButton(new AddIncomeAction()));

        mainMenu.add(buttonPanel, BorderLayout.CENTER);
    }

    public JsonReader getJsonReader() {
        return jsonReader;
    }

    public JsonWriter getJsonWriter() {
        return jsonWriter;
    }

    public static void main(String[] args) {
        new ExpenseTrackerUI();
    }

    private class AddExpenseAction extends AbstractAction {
        AddExpenseAction() {
            super("Add Expense");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            desktop.add(new AddExpenseUI(bs, ExpenseTrackerUI.this));
        }
    }

    private class AddIncomeAction extends AbstractAction {
        AddIncomeAction() {
            super("Add Income");
        }

        //todo
        @Override
        public void actionPerformed(ActionEvent e) {
            //desktop.add(new AddExpenseUI(bs));
        }
    }

    private class ReviewAction extends AbstractAction {
        ReviewAction() {
            super("Review My Records");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            desktop.add(new ReviewUI(bs, ExpenseTrackerUI.this));
        }
    }

    // EFFECTS: saves the balance sheet to file
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

    // EFFECTS: loads balance sheet from file
    private class LoadDataAction extends AbstractAction {
        LoadDataAction() {
            super("Load My Records");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                bs = jsonReader.read();
                JOptionPane.showMessageDialog(null, "Loaded your data from " + JSON_STORE_ADDRESS);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Unable to read from file: "
                        + JSON_STORE_ADDRESS, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Helper to center main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            ExpenseTrackerUI.this.requestFocusInWindow();
        }
    }

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


}
