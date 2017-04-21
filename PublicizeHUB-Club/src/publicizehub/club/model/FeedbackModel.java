package publicizehub.club.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import publicizehub.club.controller.FormSumActivityController;

/**
 *
 * @author budsagorn_ss
 */
public class FeedbackModel {

    ConnectionBuilder cb = new ConnectionBuilder();
    ArrayList<FeedbackStd> myArrList = new ArrayList<FeedbackStd>();
    FormSumActivityController fsc = new FormSumActivityController();

    //method รับค่าจาก class FormEvaluationsController เพื่อจะส่งค่าเข้า DB
    public void insertValue(int evId, long stdId, int valueRadio1, int valueRadio2, int valueRadio3,
            int valueRadio4, int valueRadio5, int valueRadio6, int valueRadio7, int valueRadio8,
            int valueRadio9, int valueRadio10) {
        Statement s = null;
        String sql;
        cb.connecting(); //connect database
        try {
            s = cb.getConnect().createStatement();  // สร้าง Statement
            sql = "INSERT INTO std_feedback (evId,stdId,sumQ1,sumQ2,sumQ3,sumQ4,sumQ5,sumQ6,sumQ7,sumQ8,sumQ9,sumQ10) "
                    + "VALUES ('" + evId + "','" + stdId + "',"
                    + "'" + valueRadio1 + "','" + valueRadio2 + "','" + valueRadio3 + "','" + valueRadio4 + "','"
                    + valueRadio5 + "','" + valueRadio6 + "','" + valueRadio7 + "','" + valueRadio8 + "','"
                    + valueRadio9 + "','" + valueRadio10 + "') ";

            s.executeUpdate(sql); // ส่งข้อมูลไป Database 

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

            e.printStackTrace();
        }
        cb.logout();
    }

    public void setSumQ() {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet result, result2;
        cb.connecting(); //เรียกใช้ method connecting()เพื่อ connect database
        int sumQ1 = 0;
        int sumQ2 = 0;
        int sumQ3 = 0;
        int sumQ4 = 0;
        int sumQ5 = 0;
        int sumQ6 = 0;
        int sumQ7 = 0;
        int sumQ8 = 0;
        int sumQ9 = 0;
        int sumQ10 = 0;
        int numPeple = 0;
        int evId = 10048;
        long stdId = 59130500045L;

        try {
            ps = cb.getConnect().prepareStatement("SELECT * FROM std_feedback"); //ดึงค่าจาก std_feedback
            ps2 = cb.getConnect().prepareStatement("SELECT COUNT(*) number FROM std_feedback");//คำสั่งดูจำนวน row ทั้งหมด (จำนวนคน)
            result = ps.executeQuery();
            result2 = ps2.executeQuery();

            result2.next();
            numPeple = result2.getInt("number");

            while (result.next()) {
            
                evId = result.getInt("evId");
                stdId = result.getLong("stdId");
                sumQ1 += result.getInt("sumQ1");
                sumQ2 += result.getInt("sumQ2");
                sumQ3 += result.getInt("sumQ3");
                sumQ4 += result.getInt("sumQ4");
                sumQ5 += result.getInt("sumQ5");
                sumQ6 += result.getInt("sumQ6");
                sumQ7 += result.getInt("sumQ7");
                sumQ8 += result.getInt("sumQ8");
                sumQ9 += result.getInt("sumQ9");
                sumQ10 += result.getInt("sumQ10");

                /*ส่งค่าไป method calculateFeedback เพื่อคำนวณหาค่าเฉลี่ย*/
                fsc.calculateFeedback(evId,stdId,numPeple,sumQ1,sumQ2,sumQ3,sumQ4,sumQ5, sumQ6,sumQ7, sumQ8,sumQ9,sumQ10);
                
            }

       
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }

        cb.logout();
    }

    //method รับค่าหลังคำนวณเสร็จแล้วส่งข้อมูลหลังคำนวณไป tb_feedback
    public void insertAvgrValue(int evId, int numPeple, int averQ1, int averQ2, int averQ3,
            int averQ4, int averQ5, int averQ6, int averQ7, int averQ8, int averQ9, int averQ10, int setSumQ1, int setSumQ2) {
        Statement s = null;
        String sql = "";
        cb.connecting(); //connect database
        try {
            s = cb.getConnect().createStatement();  // สร้าง Statement
            sql = "INSERT INTO tb_feedback (evId,stdEstimated,sumQ1,sumQ2,sumQ3,sumQ4,sumQ5,sumQ6,sumQ7,sumQ8,sumQ9,sumQ10,setSumQ1,setSumQ2) "
                    + "VALUES ('" + evId + "','" + numPeple + "'," + "'" + averQ1 + "','" + averQ2 + "','" + averQ3 + "','"
                    + averQ4 + "','" + averQ5 + "','" + averQ6 + "','" + averQ7 + "','" + averQ8 + "','" + averQ9
                    + "','" + averQ10 + "','" + setSumQ1 + "','" + setSumQ2 + "') ";

            s.executeUpdate(sql); // ส่งข้อมูลไป Database 

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

            e.printStackTrace();
        }
        cb.logout();
    }

    
    //ดึงข้อมูลหลังคำนวณจาก tb_feedback
    public ResultSet selectValueFeedback(int eventId) {
        PreparedStatement ps = null;
        ResultSet result = null;
        cb.connecting(); //เรียกใช้ method connecting()เพื่อ connect database
        try {
            ps = cb.getConnect().prepareStatement("SELECT * FROM tb_feedback where evId = ?");

            ps.setInt(1, eventId);  //ให้แสดงชื่อตาม id 
            result = ps.executeQuery();
//            if (result.next()) {
//                String tempSum = result.getInt("sumQ1") + "   " + result.getInt("sumQ2") + "  " + result.getInt("sumQ3") + "   " + result.getInt("sumQ4")
//                        + "   " + result.getInt("sumQ5") + "   " + result.getInt("sumQ6") + "   " + result.getInt("sumQ7") + "   " + result.getInt("sumQ8")
//                        + "   " + result.getInt("sumQ9") + "   " + result.getInt("sumQ10");
//                String tempSetSum = result.getInt("setSumQ1") + "   " + result.getInt("setSumQ2");
//                String tempNumPeple = result.getString("stdEstimated");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insertToLog(int eventId, long stdId) {
        ResultSet log = null;
        cb.connecting();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            PreparedStatement ps = cb.getConnect().prepareStatement("INSERT into logFeedback (evId,stdId,datestamp,timestamp) VALUES('" + eventId + "','"
                    + +stdId + "','" + LocalDate.now() + "','" + timeFormat.format(LocalTime.now()) + "')");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getFormLog(int eventId, long stdId) {
        ResultSet log = null;
        cb.connecting();
        try {
            PreparedStatement ps = cb.getConnect().prepareStatement("SELECT * FROM logFeedback where evId = ? and stdId = ?");
            ps.setInt(1, eventId);
            ps.setLong(2, stdId);
            log = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return log;
    }

}
