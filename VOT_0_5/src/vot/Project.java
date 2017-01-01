/*
 * To change this license header, 
choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vot;

import java.io.File;
//import java.io.FileWriter;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author jjones
 */
public class Project{
    
//<editor-fold defaultstate="collapsed" desc="class fields">
    private String pid;
    private String pTitle;
    private String pDescription;
    private int lastIndex;
    private ArrayList<File> wavList;
//    private final File pPath = new File("C:\\Users\\jjones\\Documents\\VOT");
//    private final File pPath = new File(System.getProperty("user.home")+ "\\VOT");
    private final File pPath = new File(FileSystemView.getFileSystemView().
            getDefaultDirectory().getPath()+ "\\VOT");
//    FileWriter out = null;
    static final String AB = 
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final SecureRandom rnd = new SecureRandom();
//</editor-fold>

//****************************************************************************
//  constructor for new project
//****************************************************************************
    public Project(){
        pTitle = null;
        pDescription = null;
        this.wavList = null;
        lastIndex = 0;
    }
    public Project(String title, 
            String description,
            ArrayList<File> wavList){
        pTitle = title;
        pDescription = description;
        this.wavList = wavList;
        lastIndex = 0;
    }
    public Project(String title, 
            String description){
        pTitle = title;
        pDescription = description;
        this.wavList = new ArrayList<>();
        lastIndex = 0;
           
    }
    public Project(String PID, String title, 
            String description,
            int lastIndex){
        this.pid = PID;
        pTitle = title;
        pDescription = description;
        this.wavList = new ArrayList<>();
        this.lastIndex = lastIndex;
           
    }

//****************************************************************************
//  getter  methods
//****************************************************************************    
    public String getPID(){
        return pid;
    }
    public String getPTitle(){
        return pTitle;
    }
    public String getPDescription(){
        return pDescription;
    }
    public int getLastIndex(){
        return lastIndex;
    }
    public ArrayList<File> getWavList(){
        return wavList;
    }
    public File getProjectPath(){
        return pPath;
    }
//****************************************************************************
//  setter methods
//****************************************************************************
    
    public void setPID(String pid){
        this.pid = pid;
    }

    public void setLastIndex(int index){
        this.lastIndex = index;
    }
    public void setWavList(ArrayList<File> wavList){
        this.wavList = wavList;
    }
 
    
//****************************************************************************
//  miscellaneous methods
//****************************************************************************
    
    public String generatePID(){
        int len = 8;
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) {
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
            while(i == 0 && Character.isDigit(sb.charAt(0)) ){
                sb.deleteCharAt(0);
                sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
            }
        }
           
        return sb.toString();
    }
    public void createProjectDirectory(){
        new File(pPath.toString()).mkdir();
    }
    
    public void generateReport(){
        
    }
    
    public void addFilesToProject(String query, Project proj) throws SQLException{
        System.out.println("hello from addFilesToProject");
        JFileChooser chooser = 
                new JFileChooser(FileSystemView.getFileSystemView().
            getDefaultDirectory().getPath());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.showOpenDialog(null);
        File selectedDirectory = chooser.getSelectedFile(); 
//        File selectedDirectory = new File("C:\\Users\\jjones\\Documents"
//                + "\\Research\\waveFiles\\"); 
        MyFileNameFilter filter = new MyFileNameFilter();
        File[] wavList = selectedDirectory.listFiles(filter);
        DatabaseOperations dbo = new DatabaseOperations(query,
                        pPath.toString());
        
        
        int i;
        for(i = proj.getLastIndex();i < proj.getLastIndex() + Array.getLength(wavList);
                i++){
            WavInfo wi = new WavInfo(wavList[i]);
            wi.setFileIndex(i);
//            param = dbo.mapBuilder(wi);
            dbo.setParameters(dbo.mapBuilder(wi));
            dbo.setSql(query);
            dbo.insertRow();
        }
        proj.setLastIndex(i-1); // subtract one b/c final loop in for adds 
//        one to the counter, i

//        update the last index in projects Table
        Map<Integer,Object> param = new HashMap<>();
        String sql = "UPDATE projects "  + "SET lastIndex = ? WHERE PID = ?";
        param.clear();
        param.put(1,proj.getLastIndex());
        param.put(2,proj.getPID());
        dbo.setSql(sql);
        dbo.setParameters(param);
        dbo.updateRow();
        
    }
    
    public static class MyFileNameFilter implements FilenameFilter{
         
        private String ext = "wav";
         
        public MyFileNameFilter(){
            this.ext = ext.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
         
    }
}  
