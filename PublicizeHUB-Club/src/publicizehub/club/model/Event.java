package publicizehub.club.model;

import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
/**
 *
 * @author ImagineRabbits
 */
public class Event {
    ConnectionBuilder cb = new ConnectionBuilder();
    
    private String evName; 
    private String evDescrip;
    private LocalDate evDate;
    private LocalDate evEndDate;
    private LocalDate evStartRegis;
    private LocalDate evEndFeedback;
    private String evPlace;
    private int evTicket;
    private int currentMember;
    private LocalTime evTime;
    private LocalTime evEndTime;
    private int evType;
    private int evId;
    
    
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
    
    PreparedStatement ps;
    ResultSet rs;

    public Event() {
    }

    public Event(String evName, String evDescrip, LocalDate evDate, LocalDate evEndDate, LocalDate evStartRegis, 
                    LocalDate evEndFeedback, String evPlace, int evTicket, int currentMember, 
                    LocalTime evTime, LocalTime evEndTime, int evType, int evId) {
        this.evName = evName;
        this.evDescrip = evDescrip;
        this.evDate = evDate;
        this.evEndDate = evEndDate;
        this.evStartRegis = evStartRegis;
        this.evEndFeedback = evEndFeedback;
        this.evPlace = evPlace;
        this.evTicket = evTicket;
        this.currentMember = currentMember;
        this.evTime = evTime;
        this.evEndTime = evEndTime;
        this.evType = evType;
        this.evId = evId;
    }

    public String getEvName() {
        return evName;
    }

    public void setEvName(String evName) {
        this.evName = evName;
    }

    public String getEvDescrip() {
        return evDescrip;
    }

    public void setEvDescrip(String evDescrip) {
        this.evDescrip = evDescrip;
    }

    public LocalDate getEvDate() {
        return evDate;
    }

    public void setEvDate(LocalDate evDate) {
        this.evDate = evDate;
    }

    public LocalDate getEvEndDate() {
        return evEndDate;
    }

    public void setEvEndDate(LocalDate evEndDate) {
        this.evEndDate = evEndDate;
    }

    public LocalDate getEvStartRegis() {
        return evStartRegis;
    }

    public void setEvStartRegis(LocalDate evStartRegis) {
        this.evStartRegis = evStartRegis;
    }

    public LocalDate getEvEndFeedback() {
        return evEndFeedback;
    }

    public void setEvEndFeedback(LocalDate evEndFeedback) {
        this.evEndFeedback = evEndFeedback;
    }

    public String getEvPlace() {
        return evPlace;
    }

    public void setEvPlace(String evPlace) {
        this.evPlace = evPlace;
    }

    public int getEvTicket() {
        return evTicket;
    }

    public void setEvTicket(int evTicket) {
        this.evTicket = evTicket;
    }

    public int getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(int currentMember) {
        this.currentMember = currentMember;
    }

    public LocalTime getEvTime() {
        return evTime;
    }

    public void setEvTime(LocalTime evTime) {
        this.evTime = evTime;
    }

    public LocalTime getEvEndTime() {
        return evEndTime;
    }

    public void setEvEndTime(LocalTime evEndTime) {
        this.evEndTime = evEndTime;
    }

    public int getEvType() {
        return evType;
    }

    public void setEvType(int evType) {
        this.evType = evType;
    }

    public int getEvId() {
        return evId;
    }

    public void setEvId(int evId) {
        this.evId = evId;
    }
    
    /* รับค่าที่ประมวลผลแล้วจาก Controller เพื่อ ส่งไป Database
    โดยใช้คำสั่ง SQL และ ประมวลผลคำสั่งโดย 
    การสร้าง Statement และเรียกใช้ method */
    public void createEvent(String name,String desc,LocalDate date,LocalDate endDate,LocalDate startRegis,LocalDate endFeedback,
                        LocalTime time,LocalTime endTime,String place,int ticket,
                        int evType,long stdId) {
        cb.connecting(); // Connect ไป Database ผ่าน Connection Builder
        Statement s = null; // สร้างตัวแปร Statement
        try {
            s = cb.getConnect().createStatement();  // สร้าง Statement
            // SQL Insert (คำสัง SQL)
            String sql = "INSERT INTO tb_event"
                    + "(evName,evDescrip,evStartDate,evEndDate,evStartRegis,evEndFeedback,evTime,evEndTime,evPlace,evTicket,evType,stdId) "
                    + "VALUES ('"  + name + "','" + desc + "','"+ date + "','"+ endDate+ "','" +startRegis+ "','"+endFeedback+ "','" + time + "','"
                    + endTime + "','" + place + "','" + ticket + "','" + evType + "','" + stdId + "') ";
            s.executeUpdate(sql); // ส่งข้อมูลไป Database
        } 
        /* ดัก SQLException และ Exception ปกติ */
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        cb.logout(); // ปิดการ connection กับ database
    }
    
    public void updateEvent(Event event){
        // update
        System.out.println(this.evId);
        PreparedStatement ps = null;    
        cb.connecting();
        String command;
        try {
            command = "UPDATE tb_event set evName = '"+event.getEvName()+"' , "
                    + "evDescrip = '"+event.getEvDescrip()+"' , evStartDate = '"+event.getEvDate()+"' , "
                    + "evEndDate = '"+event.getEvEndDate()+"' , evStartRegis = '"+event.getEvStartRegis()+"' , "
                    +"evTime = '"+event.getEvTime()+"' , evEndFeedback = '"+event.getEvEndFeedback()+"' , "
                    + "evEndTime = '"+event.getEvEndTime()+"' , evPlace = '"+event.getEvPlace()+"' , "
                    + "evTicket = '"+event.getEvTicket()+"' , evType = '"+evType+"' "
                    + "where evId = "+evId;
            ps = cb.getConnect().prepareStatement(command); 
            System.out.println("Before Update");
            ps.executeUpdate();
            System.out.println("Success Update");
        }
        catch(SQLException ex){
            ex.printStackTrace();
            System.out.println("SQL ERROR at updateEvent()");
        }
        cb.logout();
    }
    
    public void DeleteEvent(int deleteId){
        System.out.println("Call deleteEv");
        String command;
        PreparedStatement s;
        cb.connecting();
        try{
            command ="DELETE FROM tb_event WHERE evId = ?";
            System.out.println("DelID "+deleteId);
            System.out.println(command);
            s = cb.getConnect().prepareStatement(command);
            s.setInt(1,deleteId);
            
            s.executeUpdate();
            System.out.println("Delete Success");
        }
        catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("NullPointerException");
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQLException! - event");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("EXCEPTION - event");
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
    
    public ResultSet getSelect(int evId){
        cb.connecting();
        try{
            ps = cb.getConnect().prepareStatement("SELECT * FROM tb_event where evId = ?");
            ps.setInt(1,evId);
            rs = ps.executeQuery();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQLException at getSelect()");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception at getSelect()");
        }
        return rs;
    }
    
    public ResultSet getSelect(long stdId){
        cb.connecting();
        try{
            ps = cb.getConnect().prepareStatement("SELECT * FROM generatecode where stdId = ?");
            ps.setLong(1,stdId);
            rs = ps.executeQuery();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQLException at getSelect()");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception at getSelect()");
        }
        return rs;
    }
    
    public ResultSet updateCurrentMember(int updateMember,int evId){
        cb.connecting();
        try{
            ps = cb.getConnect().prepareStatement("UPDATE tb_event set currentMember='"+updateMember+"' where evId = "+evId);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("SQLException at getSelect()");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception at getSelect()");
        }
        return rs;
    }
}
