/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chickenSaleAccount;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class mainClass {

    private SimpleDateFormat simple = new SimpleDateFormat("dd-MM-YYYY");
    private Connection con = null;
    Statement stat = null;
    ResultSet rs = null;
    PreparedStatement prep = null;

    Connection getCon() throws SQLException, ClassNotFoundException {

        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        return DriverManager.getConnection("jdbc:ucanaccess://database//database.accdb");

    }

    mainClass() throws Exception {
        con = getCon();
        stat = con.createStatement();
    }

    public void add(String count, String price) throws SQLException {
        Date d1 = new Date();
        String allDate = simple.format(d1);
        String s[] = allDate.split("-");
        String year, month, day;
        day = s[0];
        month = s[1];
        year = s[2];
        stat.executeUpdate("INSERT INTO details (count,price,day,month,year) VALUES('" + count + "' , '" + price + "','" + day + "' , '" + month + "' , '" + year + "')");

    }

    public void addDebtHistory(String moneyDebt, String name) throws SQLException {
        Date d1 = new Date();

        String allDate = simple.format(d1);
        stat.executeUpdate("INSERT INTO DebtHistory (NAME,DATA,MONEY) VALUES('" + name + "' , '" + allDate + "','" + moneyDebt + "')");

    }

    public void addDebt(JTextField name, JTextField money) throws SQLException {

        String number = searchName(name);

        if (number != null) {
            int a1 = Integer.parseInt(number);
            int a2 = Integer.parseInt(money.getText());
            int a3 = a1 + a2;
            String s = "" + a3;

            stat.executeUpdate("UPDATE DEBT SET MONEY1= '" + s + "' where name = '" + name.getText() + "'");
        } else {

            stat.executeUpdate("INSERT INTO DEBT (NAME,MONEY1) VALUES('" + name.getText() + "' , '" + money.getText() + "')");

        }
        addDebtHistory(money.getText(), name.getText());

    }

    public void addBills(String Weight, String prise, String sum, String numOfCage, String WEIGHTWITHOUTCAGE) throws SQLException {
        Date d1 = new Date();
        String allDate = simple.format(d1);
        stat.executeUpdate("INSERT INTO BILLS (WEIGHTWITHCAGE,PRISE,SUM,DATA,NUMOFCAGE,WEIGHTWITHOUTCAGE) VALUES('" + Weight + "' , '" + prise + "','" + sum + "' , '" + allDate + "' , '" + numOfCage + "' , '" + WEIGHTWITHOUTCAGE + "')");

    }

    public void printBills(DefaultTableModel model) throws SQLException {
        rs = stat.executeQuery("select * from   BILLS ORDER BY DATA");

        while (rs.next()) {

            String b[] = new String[6];
            b[5] = rs.getString(6);
            b[2] = rs.getString(1);
            b[1] = rs.getString(2);
            b[0] = rs.getString(3);
            b[3] = rs.getString(4);
            b[4] = rs.getString(5);
            model.addRow(b);

        }

    }

    public String padyDebt(JTextField name, JTextField money) throws SQLException {
        String number = searchName(name);

        if (number != null) {
            int a1 = Integer.parseInt(number);
            int a2 = Integer.parseInt(money.getText());
            if (a1 < a2) {
                return "notdone";
            }

            if (a1 == a2) {
                stat.executeUpdate("DELETE FROM DEBT  WHERE name= '" + name.getText() + "'");
                return "delete";
            }
            int a3 = a1 - a2;
            stat.executeUpdate("UPDATE DEBT SET MONEY1= '" + a3 + "' where name = '" + name.getText() + "'");
            return "don";

        }
        return null;

    }

    public void searchFortable(DefaultTableModel model, JTextField name) throws SQLException {
        if (!name.getText().isEmpty()) {
            rs = stat.executeQuery("select * from   DEBT WHERE NAME  LIKE '" + name.getText() + "%' ");
            model.setRowCount(0);
            while (rs.next()) {

                String b[] = new String[2];
                b[1] = rs.getString(1);
                b[0] = rs.getString(2);

                model.addRow(b);

            }
        } else {
            model.setRowCount(0);

        }

    }

    public int carh(String day1, String month1, String year1) throws SQLException {
        int i = 0;
        rs = stat.executeQuery("select * from  details where DAY=  '" + day1 + "'and month= '" + month1 + "'and year='" + year1 + "'");

        while (rs.next()) {
            i = i + Integer.parseInt(rs.getString(2));

        }
        return i;

    }

    public void printDept(DefaultTableModel model) throws SQLException {
        rs = stat.executeQuery("select * from   DEBT   ");
        while (rs.next()) {
            String b[] = new String[2];
            b[0] = rs.getString(1);
            b[1] = rs.getString(2);

            model.addRow(b);

        }

    }

    public void changePrise(JTextField name) throws SQLException {
        stat.executeUpdate("UPDATE THENEWPRICE SET prise = '" + name.getText() + "'");

    }

    public String returnprise() throws SQLException {
        rs = stat.executeQuery("select * from   THENEWPRICE");
        if (rs.next() == false) {
            return "";

        }
        String a = rs.getString(1);
        if (a == null) {
            return "";
        } else {
            return a;
        }

    }

    public void print() throws SQLException {

        rs = stat.executeQuery("select * from   details ");

        while (rs.next()) {
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" + rs.getString(4) + "\t" + rs.getString(5));

        }

    }

    public String searchName(JTextField name) throws SQLException {
        rs = stat.executeQuery("select * from   DEBT WHERE NAME  = '" + name.getText() + "'");
        if (rs.next() == true) {
            return rs.getString(2);
        }
        return null;
    }

    public void displayDebtHistory(DefaultTableModel model) throws SQLException {
        rs = stat.executeQuery("select * from DEBTHISTORY ORDER BY DATA");
        while (rs.next()) {
            String b[] = new String[3];
            b[1] = rs.getString(1);
            b[0] = rs.getString(2);
            b[2] = rs.getString(3);
            model.addRow(b);

        }

    }

    public static void main(String args[]) throws SQLException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    mainFrame fr = new mainFrame();
                    fr.setVisible(true);

                    fr.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } catch (Exception ex) {
                    Logger.getLogger(mainClass.class.getName()).log(Level.SEVERE, null, ex);

                }
            }

        });

    }

}
