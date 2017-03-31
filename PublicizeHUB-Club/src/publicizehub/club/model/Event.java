package publicizehub.club.model;

import java.util.Date;
import java.sql.*;
import java.text.*;
import javax.swing.*;
/**
 *
 * @author ImagineRabbits
 */
public class Event {
    ConnectionBuilder cb = new ConnectionBuilder();
    
    private int evId;
    private String evName="";
    private String evDescrip="";
    private Date evDate;
    private Date evEndDate;
    private Time evTime;
    private Time evEndTime;
    private String evPlace;
    private int evTicket;
    private int eventType;
    private long stdId;
    
    Date date = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    
    PreparedStatement ps;
    ResultSet rs;
    
    public void newEvent() {
        cb.connecting();
        Statement s = null;
       
        try {
            s = cb.getConnect().createStatement();
            // SQL Insert
            String sql = "INSERT INTO tb_event"
                    + "(evName,evDescrip,evDate,evEndDate,evTime,evEndTime,evPlace,evTicket,evType,stuId) "
                    + "VALUES ('" 
                    + evName + "','"
                    + evDescrip + "','"
                    + evDate + "','"
                    + evEndDate + "','"
                    + evTime + "','"
                    + evEndTime + "','"
                    + evPlace + "','"
                    + evTicket + "','"
                    + eventType + "','"
                    + stdId + "') ";
            s.executeUpdate(sql);
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }

        cb.logout();
    }
    
    public void EditEvent(){
    
    }
    
    public void DeleteEvent(){
        System.out.println("Call deleteEv");
        String command;
        PreparedStatement s;
        try{
            command ="DELETE FROM tb_event WHERE evId = ?";
            System.out.println(evId);
            System.out.println(command);
            s = cb.getConnect().prepareStatement(command);
            s.setInt(1,evId);
            s.executeUpdate();
            System.out.println("Delete Success");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            cb.logout();
        }
    }
    
    public ResultSet getEvent(){
        cb.connecting();
        try{
            ps = cb.getConnect().prepareStatement("SELECT * FROM tb_event");
            rs = ps.executeQuery();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQL ผิดพลาด");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("getEvent() Exception!");
        }
        return rs;
    }

    
}