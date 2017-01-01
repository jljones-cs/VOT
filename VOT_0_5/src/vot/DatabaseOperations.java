
package vot;

import java.awt.HeadlessException;
import java.awt.List;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjones
 */
public class DatabaseOperations {
//    
//<editor-fold defaultstate="collapsed" desc="class fields">
//    public File file;
//    public FileWriter fileWriter;
    private String sql;
    private String projectPath;
    Connection projectDBConn = null;
    Statement projectDBStmnt = null;
    ResultSet projectDBResults = null;
    private Map<Integer,Object> parameters;
    
    
//    private final String projectsSelect = 
// 
//</editor-fold>
//   
    public DatabaseOperations(String sql, String projectPath){
        this.sql = sql;
        this.projectPath = projectPath;
        
    }
    public void setSql(String sql){
        this.sql = sql;
    }
    public void setProjectPath(String projectPath){
        this.projectPath = projectPath;
    }
    public void setParameters(Map<Integer,Object> param){
        parameters = param;
    }
    
    public void shutdownDB(){
         try {
                if(projectDBConn.isClosed()){
                    System.out.println("Student DB is closed.");
                }
                if(!projectDBConn.isClosed()){
                    System.out.println("Student DB is NOT closed.");
                }
        
             } catch (SQLException ex) {
                     Logger.getLogger(DatabaseOperations.class.getName()).
                             log(Level.SEVERE, null, ex);
                 }
            
        }
    
    public Map<Integer,Object> mapBuilder(WavInfo wi){
        Map<Integer,Object> param = new HashMap<>();
        param.put(1,wi.getFilename());
        param.put(2,wi.getNSamples());
        param.put(3,wi.getSamplingRate());
        param.put(4,wi.getMaxSample());
        param.put(5,wi.getStThreshhold());
        param.put(6,wi.getThreshhold());
        param.put(7,wi.getGroupStart());
        param.put(8,wi.getGroupEnd());
        param.put(9,wi.getAlgVOT());
        param.put(10,wi.getManVOT());
        param.put(11,wi.getProcessed());
        param.put(12,wi.getFlag());
        param.put(13,wi.getRemarks());
        param.put(14,wi.getFileIndex());

        return param;
    }
    public Map<Integer,Object> mapBuilder(Project proj){
        Map<Integer,Object> param = new HashMap<>();
        param.put(1,proj.getPID());
        param.put(2,proj.getPTitle());
        param.put(3,proj.getPDescription());
        param.put(4,proj.getLastIndex());

        return param;
    }
    
    static void setUpProjectDB(File pPath) throws SQLException{
        String user = "john";
        String pass = "pass";
        Connection projectDBConn = null;
        ResultSet projectDBResults = null;
        Statement projectDBStmnt = null;
        try {
            String dir = "jdbc:sqlite:" + pPath.toString() + "\\vot.db";
//            System.out.println("dir = " + dir);

            projectDBConn = DriverManager.getConnection(dir);
//            projectDBResults = projectDBConn.getMetaData().getCatalogs();
//            if(!projectDBResults.next()){
                projectDBConn.
                createStatement().
                execute("create table projects(PID varchar(8) not null primary key," +
                            "Title varchar(8) not null," +
                            "Description varchar(256) not null," +
                            "LastIndex int not null)");
//            }
//            System.out.println("table created .. .. ..");
            
        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("setUpProjectDB");
            System.out.println("message: " + exc.getMessage());
            System.out.println(exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {
//           System.out.println("Fipally!!!");
            if (projectDBResults != null) {
                projectDBResults.close();
            }
            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }
            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }
    }
    
    public Boolean existsRow() throws SQLException{
//        public ResultSet rowOps() throws SQLException{
        System.out.println("Enter existsRow()");
        
        try{
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");
            PreparedStatement ps =  projectDBConn.prepareStatement(sql);
            
            if( parameters != null){
                for(Integer key : parameters.keySet()){
                    ps.setObject(key, parameters.get(key));
                    System.out.println("key: " + key + "  value: " + parameters.get(key));
                }
                projectDBResults = ps.executeQuery();
            }
            
        }
        catch (SQLException|HeadlessException exc){
                System.out.println("existsRow");
                System.out.println("message: " + exc.getMessage());
                System.out.println("cause: " + exc.getCause());
                Throwable t = exc.getCause();
                    while(t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
            }
            finally {

                if (projectDBResults != null) {
                    projectDBResults.close();
                }

                if (projectDBStmnt != null) {
                    projectDBStmnt.close();
                }

                if (projectDBConn != null) {
                    projectDBConn.close();
                }
            }
        
            if(projectDBResults.next()){
                return true;
            }else{
                return false;
            }
            
//            return projectDBResults;
        
    } 
    
    public void insertRow()throws SQLException {
//        System.out.println("Enter insertRow()");
        try{
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");
            PreparedStatement ps =  projectDBConn.prepareStatement(sql);
            
            if( parameters != null){
                for(Integer key : parameters.keySet()){
                    ps.setObject(key, parameters.get(key));
//                    System.out.println("key: " + key + "  value: " + parameters.get(key));
                }
                ps.execute();
            }

        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("InsertRow");
            System.out.println("message: " + exc.getMessage());
            System.out.println("cause: " + exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {

            if (projectDBResults != null) {
                projectDBResults.close();
            }

            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }

            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }

    }
    
    public void updateRow()throws SQLException{
        System.out.println("Enter updateRow()");
        try{
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");
            PreparedStatement ps =  projectDBConn.prepareStatement(sql);
            
            if( parameters != null){
                System.out.println("param size: " + parameters.size());
                for(Integer key : parameters.keySet()){
                    System.out.println("key: " + key + "  value: " + parameters.get(key));
                    ps.setObject(key, parameters.get(key));
                    
                }
                ps.execute();
            }

        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("UpdateRow");
            System.out.println("message: " + exc.getMessage());
            System.out.println("cause: " + exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {

            if (projectDBResults != null) {
                projectDBResults.close();
            }

            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }

            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }
    }
    public void deleteRow()throws SQLException{
        
    }
    
    public List processProjectListQuery() throws SQLException{
//    public ResultSet processQuery() throws SQLException{
        System.out.println("*******************  hello from processQuiery  *************************************");
        System.out.println("************************************************************************************");
        System.out.println("sql stmnt: "  + sql);
        List results = new List(10, false);
//        ArrayList<String> results = new ArrayList<>();
        try{
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");
            PreparedStatement ps =  projectDBConn.prepareStatement(sql);

//                System.out.println("coming at you from else params...");
                projectDBResults = ps.executeQuery();
                while(projectDBResults.next()){
                    results.add(projectDBResults.getString("Title") + "    "
                                  + projectDBResults.getInt("LastIndex") + "    "
                                  + projectDBResults.getString("Description") + "    "
                                   +   projectDBResults.getString("PID") );
//                    System.out.println("coming at you from next...");
                }

            
        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("processProjectListQuery");
            System.out.println("message: " + exc.getMessage());
            System.out.println("cause: " + exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {

            if (projectDBResults != null) {
                projectDBResults.close();
            }

            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }

            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }
        return results;
    }
    public ArrayList<WavInfo> processProjectQuery() throws SQLException{
//    public ResultSet processQuery() throws SQLException{
        System.out.println("Enter processProjectQuery()");
        System.out.println("sql stmnt: "  + sql);
//        WavInfo wi;
        ArrayList<WavInfo> wiList = new ArrayList<>();
        try{
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");
            PreparedStatement ps =  projectDBConn.prepareStatement(sql);

//                System.out.println("coming at you from else params...");
                projectDBResults = ps.executeQuery();
                while(projectDBResults.next()){
                    wiList.add(new WavInfo(
                        new File(projectDBResults.getString("filename")),
                        projectDBResults.getInt("nSamples"),
                        projectDBResults.getInt("samplingRate"),
                        projectDBResults.getInt("maxSample"),
                        projectDBResults.getDouble("stThreshhold"),
                        projectDBResults.getInt("threshhold"),
                        projectDBResults.getDouble("groupStart"),
                        projectDBResults.getDouble("groupEnd"),
                        projectDBResults.getDouble("algVot"),
                        projectDBResults.getDouble("manVOT"),
                        projectDBResults.getInt("processed"),
                        projectDBResults.getBoolean("flag"),
                        projectDBResults.getString("remarks"),
                        projectDBResults.getInt("fileIndex")));
                }
                
                
            
        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("processProjectListQuery");
            System.out.println("message: " + exc.getMessage());
            System.out.println("cause: " + exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {

            if (projectDBResults != null) {
                projectDBResults.close();
            }

            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }

            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }
        return wiList;
    }
    public void createProjectTable(String PID) throws SQLException{
        System.out.println("Enter createProjectTable()");
        try {
            projectDBConn = DriverManager.
                    getConnection("jdbc:sqlite:" + projectPath + "\\vot.db");

            projectDBConn.
            createStatement().
            execute("CREATE TABLE " +  PID + "(filename varchar(256) not null," +
                                "nSamples int not null," +
                                "samplingRate int not null," +
                                "maxSample int not null," +
                                "stThreshhold real not null," +
                                "threshhold int not null," +
                                "groupStart real not null," +
                                "groupEnd real not null," +
                                "algVOT real not null," +
                                "manVOT real not null," +
                                "processed int not null," +
                                "flag boolean not null," +
                                "remarks varchar(256) not null," +
                                "fileIndex int not null primary key)");
//            }
//            System.out.println("table created .. .. ..");
            
        } 
        catch (SQLException|HeadlessException exc){
            System.out.println("setUpProjectDB");
            System.out.println("message: " + exc.getMessage());
            System.out.println(exc.getCause());
            Throwable t = exc.getCause();
                while(t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
        }
        finally {
//           System.out.println("Fipally!!!");
            if (projectDBResults != null) {
                projectDBResults.close();
            }
            if (projectDBStmnt != null) {
                projectDBStmnt.close();
            }
            if (projectDBConn != null) {
                projectDBConn.close();
            }
        }
        
    }

    
 
}
