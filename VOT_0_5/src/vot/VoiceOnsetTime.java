/*
 * To change this license header, choose License Headers 
 * in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vot;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;


/**
 *
 * @author jjones
 */
public class VoiceOnsetTime {
      //<editor-fold defaultstate="collapsed" desc="class fields">
        
        private JButton newProjectBn;    
        private JButton openProjectBn;
        private JButton createProjectBn;
        private JButton nextBn;
        private JButton previousBn;
        private JButton updateBn;
        private JButton playBn;
        private JButton exportBn;
        
        private JTextField titleField;
        private JTextField descriptionField;
        
        private JScrollPane scrollPane;
        
        private JPanel startPl;
        private JPanel newProjectPl;
        private JPanel displayProjectsPl;
        private JPanel generalControlsPl;
        private JPanel processPl;
        private JPanel fileNavPl;
        private JPanel playbackControlsPl;
        private JPanel filesReviewPl;
        
        private DrawWaveform waveformPl;

        private JLabel workingProj;
        private JLabel workingProjContents;
        
        
        private static JPanel framePanel;
        private static JFrame frame;
        
        ArrayList<WavInfo> wiList = new ArrayList<>();
        private int tableCounter = 0;
        static JTable table;
        Object[][] data;
//        File conDir = new File("C:\\Users\\jjones\\Documents\\");
        String projectToOpen;
        Project proj;
        FileWriter out = null;
        NumberFormat DbleFormat = new DecimalFormat("#0.000");
        
        private String activeProjectName;
        private final String projectsColumnsInsert =  "INSERT INTO projects (PID, "
                                + "Title, "
                                + "Description, "
                                + "LastIndex) VALUES (?,?,?,?)";

        private final String activeProjectTableInsert = "INSERT INTO "; 
        private final String activeProjectTableColumns = 
                        " (filename, "
                        + "nSamples, "
                        + "samplingRate, "
                        + "maxSample, "
                        + "stThreshhold, "
                        + "threshhold, "
                        + "groupStart, "
                        + "groupEnd, "
                        + "algVOT, "
                        + "manVOT, "
                        + "processed, "
                        + "flag, "
                        + "remarks, "
                        + "fileIndex) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


//</editor-fold>
        
    public VoiceOnsetTime(){
        init();
    }
    
    private void init(){
        
//<editor-fold defaultstate="collapsed" desc="Variable Delcarations">
        proj = new Project();
        newProjectBn = new JButton("New Project");
        openProjectBn = new JButton("Open Project");
        createProjectBn = new JButton("Create Project");
        nextBn = new JButton("Next");
        previousBn = new JButton("Prev");
        updateBn = new JButton("Update");
        playBn = new JButton("Play");
        exportBn = new JButton("Export Report");
//        titleField = new JTextField("Enter a project title",20);
        titleField = new JTextField("TestProject",20);
        descriptionField = new JTextField("Enter a project description");
        workingProj = new JLabel("Current project: ");
        workingProjContents = new JLabel();
        
        
        
        
        
        
        
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="ActionListenerDeclarations">
//****************************************************************************
//   Action Listeners for Buttons
//****************************************************************************
        
        createProjectBn.addActionListener((ActionEvent evt) -> {
            try {
                createProjectBn(evt);
            } catch (SQLException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
        newProjectBn.addActionListener((ActionEvent evt) -> {
            try {          
                newProjectBn(evt);
            } catch (SQLException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        openProjectBn.addActionListener((ActionEvent evt) -> {
            try {
                openProjectBn(evt);
            } catch (SQLException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
        
        nextBn.addActionListener((ActionEvent evt) -> {
            nextBn(evt);
        });
        previousBn.addActionListener((ActionEvent evt) -> {
            previousBn(evt);
        });
        updateBn.addActionListener((ActionEvent evt) -> {
            updateBn(evt);
        });
        playBn.addActionListener((ActionEvent evt) -> {
            try {
                playBn(evt);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
        exportBn.addActionListener((ActionEvent evt) -> {
            try {
                exportBn(evt);
            } catch (IOException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
//</editor-fold>
       
//<editor-fold defaultstate="collapsed" desc="panels and Layout">
//****************************************************************************
//  panels
//****************************************************************************        
        
        
        startPl = new JPanel();
        startPl.setBackground(Color.blue);
        startPl.add(newProjectBn);
        frame.getRootPane().setDefaultButton(newProjectBn);
        newProjectBn.requestFocus();
        
//        Tabbing to openProjectBn does NOT switch keyboard focus
//        to that button. see https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html
        startPl.add(openProjectBn);
        openProjectBn.requestFocus();
        
        newProjectPl = new JPanel();
        newProjectPl.setBackground(Color.green);
        newProjectPl.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        newProjectPl.add(titleField,c);
        newProjectPl.add(descriptionField,c);
        newProjectPl.add(createProjectBn,c);
        
        displayProjectsPl = new JPanel();
        displayProjectsPl.setBackground(Color.DARK_GRAY);
        displayProjectsPl.setLayout(new GridBagLayout());
        
        waveformPl = new DrawWaveform();
//        File str = waveformPl.getWavInfo().getFilename();
        processPl = new JPanel();
        processPl.setLayout(new BoxLayout(processPl, BoxLayout.Y_AXIS));
        fileNavPl = new JPanel();
        playbackControlsPl = new JPanel();

        generalControlsPl = new JPanel();
//See JToolBar for possibility of separating buttons
       // waveformPl = new JPanel();
       
        //waveformPl = new JPanel();
        
        filesReviewPl = new JPanel();
        filesReviewPl.setBackground(Color.orange);
        filesReviewPl.setPreferredSize(new java.awt.Dimension(1000, 800));
//        fileNavPl.add(addFilesFileNavBn);
        fileNavPl.add(nextBn);
        fileNavPl.add(previousBn);
        fileNavPl.add(updateBn);

        playbackControlsPl.add(playBn);
//        playbackControlsPl.add(pauseBn);
//        playbackControlsPl.add(stopBn);

        generalControlsPl.add(fileNavPl);
        generalControlsPl.add(playbackControlsPl);
        generalControlsPl.add(exportBn);
        processPl.add(waveformPl);
        processPl.add(generalControlsPl);
        processPl.add(filesReviewPl);

        waveformPl.setBackground(Color.gray);
       // waveformPl.setPreferredSize(new java.awt.Dimension(1000, 200));

//        filesReviewPl.setBackground(Color.pink);

     
//</editor-fold>
   
         
    }
    



//<editor-fold defaultstate="collapsed" desc="ActionListenerDefinitions">
//****************************************************************************
//  Define button action listener behavior
//****************************************************************************
    private void createProjectBn(ActionEvent evt) throws SQLException{
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        proj = new Project(
                titleField.getText(),
                descriptionField.getText());
        String sql = "SELECT PID from projects WHERE PID = ?";
        DatabaseOperations dbo = new DatabaseOperations(sql,
                        proj.getProjectPath().toString());
        Map<Integer,Object> param = new HashMap<>();
        do{
            proj.setPID(proj.generatePID());
            param.put(1,proj.getPID());
            dbo.setParameters(param);
        }while(dbo.existsRow());
        
//        Insert project Info into vot.db Table: projects       
        
        dbo.setSql(projectsColumnsInsert);
        dbo.setParameters(dbo.mapBuilder(proj));
        dbo.insertRow();
        
//        Create new Table in vot.db for current project
        dbo.createProjectTable(proj.getPID());
//        add files
        SwingWorker sw = new SwingWorker<Void, Void>(){
            @Override
            public Void doInBackground() throws SQLException {
                proj.addFilesToProject("INSERT INTO " 
                + proj.getPID() + activeProjectTableColumns, proj);
                return null;
            }
            @Override
            public void done() {
                Toolkit.getDefaultToolkit().beep();
                openProjectBn.setEnabled(true);
                frame.setCursor(null);
            }
        };
        openProjectBn.setEnabled(false);
        sw.execute();
//        proj.addFilesToProject("INSERT INTO " 
//                + proj.getPID() + activeProjectTableColumns, proj);
//      show the 
        CardLayout cl = (CardLayout)(framePanel.getLayout());
        cl.show(framePanel, "startPl");
        frame.getRootPane().setDefaultButton(newProjectBn);
        newProjectBn.requestFocus();
    }
    
    private void newProjectBn(ActionEvent evt) throws SQLException{
        System.out.println("new project button pressed");    
        CardLayout cl = (CardLayout)(framePanel.getLayout());
        cl.show(framePanel, "newProjectPl");
        
        // possible improvement in future will prompt user for
        // project directory here if they don't want to use the default
//        Project proj = new Project(null, null);
        proj.createProjectDirectory();
        DatabaseOperations.setUpProjectDB(proj.getProjectPath());
        frame.getRootPane().setDefaultButton(createProjectBn);
        createProjectBn.requestFocus();
        descriptionField.selectAll();
        descriptionField.requestFocus();
        titleField.selectAll();
        titleField.requestFocus();
        
        

    }

    private void openProjectBn(ActionEvent evt) throws SQLException{
        System.out.println("open project button pressed");    
        CardLayout cl = (CardLayout)(framePanel.getLayout());
      
//        Project proj = new Project();
        System.out.println("projectPath: " + proj.getProjectPath().toString() );
        String sql = "SELECT * FROM projects";
        DatabaseOperations dbo = new DatabaseOperations(sql,
                        proj.getProjectPath().toString());
        dbo.setParameters(null);
        
        List listOfProjects = dbo.processProjectListQuery();
        
        JLabel label = new JLabel("Nothing selected");
        label.setForeground(Color.lightGray);
        JButton select = new JButton("Select");
        select.setEnabled(false);
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("fuckers: " + projectToOpen);
                String sql = "SELECT * FROM "  + projectToOpen;
//                Map<Integer,Object> param = new HashMap<>();
                dbo.setSql(sql);
//                ResultSet dbResults = null;
                int i=0;
                try {
                    wiList = dbo.processProjectQuery();
                    for(i = 0; i < wiList.size(); i++){
                        System.out.println("wilist[" + i + "]: " + wiList.get(i));
                        if(wiList.get(i).getProcessed() == 0){
                            System.out.println("get processed equals 0");
                            processWaveform(i);
                           sql = "UPDATE " + projectToOpen + " SET filename = ?, "
                            + "nSamples = ?, "
                            + "samplingRate = ?, "
                            + "maxSample = ?, "
                            + "stThreshhold = ?, "
                            + "threshhold = ?, "
                            + "groupStart = ?, "
                            + "groupEnd = ?, "
                            + "algVOT = ?,"
                            + "manVOT = ?, "
                            + "processed = ?, "
                            + "flag = ?, "
                            + "remarks = ? "
                            + "WHERE fileIndex = ?";
                           dbo.setSql(sql);
                           dbo.setParameters(dbo.mapBuilder(waveformPl.
                                     getWavInfo()));
                           dbo.updateRow();
//                           proj.setTitle()
                            
                            break;
                        }
                        
                    }
                    if(i == wiList.size()){
                            i=0;
                            System.out.println("i equals wiList.size()");
                            waveformPl.setWavInfo(wiList.get(i));
                            waveformPl.clearSamples();
                            waveformPl.getSamples();
                            waveformPl.processSamples();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(VoiceOnsetTime.class.getName()).log(Level.SEVERE, null, ex);
                }
                createDataTable(wiList);
                table.setRowSelectionInterval(i,i);
//                setTableCounter(0);
                tableCounter = i;//proj.getLastIndex();
//                displayProjectsPl.removeAll();
                scrollPane = new JScrollPane(table);
                    
        //Add the scroll pane to this panel.
                filesReviewPl.add(scrollPane);
//                generalControlsPl.add(workingProj);
//                workingProjContents.setText(proj.getPTitle());
//                generalControlsPl.add(workingProjContents);
                workingProj.setForeground(Color.lightGray);
                workingProjContents.setForeground(Color.lightGray);
                waveformPl.add(workingProj);
                workingProjContents.setText(proj.getPTitle());
                waveformPl.add(workingProjContents);
                cl.show(framePanel, "processPl");
            }
        });
 
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        listOfProjects.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    label.setVisible(true);
                    System.out.println("you selected: " + 
                            listOfProjects.getItem((int) e.getItem()));
                    label.setText(listOfProjects.getItem((int) e.getItem()));
                    String[] projInfo = 
                            listOfProjects.
                                    getItem((int) e.getItem()).split("    ");
                    projectToOpen = projInfo[Array.getLength(projInfo)-1];
//                    System.out.println("last index: ." + projInfo[1] + ".");
//                    System.out.println("class of last index: " + projInfo[1].getClass());
//                    int lastIndex = projInfo[1].toInt();
                    System.out.println("from openProjectBn, PID: " + projInfo[3]);
                    proj = new Project(projInfo[3],projInfo[0],projInfo[2], Integer.parseInt(projInfo[1]) );
                    select.setEnabled(true);
                    
//                    ...
                } else {
//                    label.setVisible(false);
                }
            }
        });
        displayProjectsPl.add(listOfProjects,c);
        displayProjectsPl.add(select,c);
        displayProjectsPl.add(label,c);
        
//        ArrayList<String> listOfProjects = dbo.processQuery();
//        for(int i = 0; i < listOfProjects.size(); i++){
//            
//            
//            
//        }
        
        cl.show(framePanel, "displayProjectsPl");
    }
       
    private void nextBn(ActionEvent evt){
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from next button  ***************************************");
        System.out.println("************************************************************************************");
        System.out.println("tableCounter: " + tableCounter);
        tableCounter++;
        if(tableCounter >= proj.getLastIndex() ) {
            nextBn.setEnabled(false);
            previousBn.setEnabled(true);
            tableCounter = proj.getLastIndex();
        }
        if(tableCounter > 0){
            previousBn.setEnabled(true);
        }
        if(wiList.get(tableCounter).getProcessed() == 0){
                            
                            System.out.println("from next, file: " +wiList.get(tableCounter).getFilename().toString());
                            System.out.println("from next, file: " + waveformPl.getWavInfo().getFilename().toString());
                            processWaveform(tableCounter);
                            updateTableRow();

        }else{
            waveformPl.setWavInfo(wiList.get(tableCounter));
            waveformPl.clearSamples();
            waveformPl.getSamples();
            waveformPl.processSamples();
             waveformPl.painted = false;
            waveformPl.repaint();
//            waveformPl.revalidate();
        }
        table.setRowSelectionInterval(tableCounter,tableCounter);


    }
     
     
    private void previousBn(ActionEvent evt){
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from previous button  ***************************************");
        System.out.println("************************************************************************************");
        System.out.println("tableCounter: " + tableCounter);
        tableCounter--;
        if(tableCounter <= 0 ) {
            previousBn.setEnabled(false);
            tableCounter = 0;
        }
        if(tableCounter < proj.getLastIndex()){
            nextBn.setEnabled(true);
        }
        waveformPl.setWavInfo(wiList.get(tableCounter));
        waveformPl.clearSamples();
        waveformPl.getSamples();
        waveformPl.processSamples();
        waveformPl.painted = false;
        waveformPl.repaint();
        table.setRowSelectionInterval(tableCounter,tableCounter);
//        waveformPl.revalidate();

    }
   
    private void updateBn(ActionEvent evt){
        table.setValueAt(waveformPl.getWavInfo().getManVOT(),getTableCounter(),2);
        updateTableRow();
//        table.setValueAt(waveformPl.getManVOT(),getTableCounter(),4);
    }
    private void playBn(ActionEvent evt) 
            throws LineUnavailableException, 
            UnsupportedAudioFileException, IOException{
        waveformPl.playWaveform();
   }
    private void exportBn(ActionEvent evt) throws IOException{
            JFileChooser saveFile = new JFileChooser(FileSystemView.getFileSystemView().
            getDefaultDirectory().getPath());
            saveFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        chooser.setAcceptAllFileFilterUsed(false);
//        chooser.showOpenDialog(null);
            saveFile.showSaveDialog(null);
            out = new FileWriter(saveFile.getSelectedFile() + "\\" + proj.getPTitle() + ".txt");
            String sql = "SELECT * FROM "  + projectToOpen;
            DatabaseOperations dbo = new DatabaseOperations(sql,
                        proj.getProjectPath().toString());
            dbo.setSql(sql);
//                ResultSet dbResults = null;
                
                try {
                    wiList = dbo.processProjectQuery();
                    for(int i = 0; i < wiList.size();i++){
                        out.write(wiList.get(i).returnWavInfoAsString());
                        out.write("\n");
                    }
      
                } catch (SQLException ex) {
                    Logger.getLogger(VoiceOnsetTime.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.close();
   }
    
//</editor-fold>
    
//    class Task extends SwingWorker<Void, Void>{
//        
//        @Override
//        public Void doInBackground() {
//           
//            return null;
//        }
// 
//        /*
//         * Executed in event dispatching thread
//         */
//        @Override
//        public void done() {
//            Toolkit.getDefaultToolkit().beep();
//            
//        }
//    }
    private void processWaveform(int counter){
            waveformPl.setWavInfo(wiList.get(counter));
            waveformPl.detectFeatures();
            wiList.set(counter,new WavInfo(
                    waveformPl.getWavInfo().getFilename(),
                    waveformPl.getWavInfo().getNSamples(),
                    (int) waveformPl.getWavInfo().getSamplingRate(),
                    waveformPl.getWavInfo().getMaxSample(),
                    waveformPl.getWavInfo().getStThreshhold(),
                    waveformPl.getWavInfo().getThreshhold(),
                    waveformPl.getWavInfo().getGroupStart(),
                    waveformPl.getWavInfo().getGroupEnd(),
                    waveformPl.getWavInfo().getAlgVOT(),
                    waveformPl.getWavInfo().getManVOT(),
                    1,
                    waveformPl.getWavInfo().getFlag(),
                    waveformPl.getWavInfo().getRemarks(),
                    waveformPl.getWavInfo().getFileIndex() ) ) ;
                    

    }
    private void updateTableRow(){
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from updateTableRow()  **********************************");
        System.out.println("************************************************************************************");
        System.out.println("tableCounter: " + getTableCounter());
        System.out.println("file: " + waveformPl.
                    getWavInfo().getFilename() );
        System.out.println("processed: " + waveformPl.
                    getWavInfo().getProcessed() );
//        table.setValueAt(waveformPl.
//                    getWavInfo().getThreshhold(),getTableCounter(),2);
       
        table.setValueAt(DbleFormat.format(waveformPl.getWavInfo().
                getAlgVOT()),getTableCounter() , 1) ;
        table.setValueAt(DbleFormat.format(waveformPl.getWavInfo().
                getManVOT()),getTableCounter(),2);
        table.setValueAt(waveformPl.
                    getWavInfo().getProcessed(),getTableCounter(),3);
        waveformPl.getWavInfo().setFlag((Boolean) table.getValueAt(getTableCounter(),4) );
        System.out.println("remarks: " + table.getValueAt(getTableCounter(),5));
        waveformPl.getWavInfo().
                setRemarks(table.getValueAt(getTableCounter(),5).toString() );
//        Project proj = new Project();
        System.out.println("PID: " + proj.getPID());
        String sql = "UPDATE " + projectToOpen + " SET filename = ?, "
                            + "nSamples = ?, "
                            + "samplingRate = ?, "
                            + "maxSample = ?, "
                            + "stThreshhold = ?, "
                            + "threshhold = ?, "
                            + "groupStart = ?, "
                            + "groupEnd = ?, "
                            + "algVOT = ?,"
                            + "manVOT = ?, "
                            + "processed = ?, "
                            + "flag = ?, "
                            + "remarks = ? "
                            + "WHERE fileIndex = ?";
        DatabaseOperations dbo = new DatabaseOperations(sql,
                        proj.getProjectPath().toString()); 
        dbo.setParameters(dbo.mapBuilder(waveformPl.
                    getWavInfo()));
            try {
                dbo.updateRow();
            } catch (SQLException ex) {
                Logger.getLogger(VoiceOnsetTime.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

                                    
    public void setTableCounter(int tableCounter){
        this.tableCounter = tableCounter;
    }
    
    public int getTableCounter(){
        return tableCounter;
    }
    public void createDataTable(ArrayList<WavInfo> wiList){
         data = new Object[wiList.size()][6];

        for(tableCounter = 0;tableCounter<wiList.size(); tableCounter++){
            String algVOT = String.format("%1.3f", 
                    wiList.get(tableCounter).getAlgVOT() );
            String manVOT = String.format("%1.3f", 
                    wiList.get(tableCounter).getManVOT() );
            String[] file = wiList.get(tableCounter).getFilename().
                    toString().split("\\\\");
            data[tableCounter][0] = file[Array.getLength(file) - 1];
//            data[tableCounter][1] = wiList.get(tableCounter).getNSamples();
//            data[tableCounter][2] = wiList.get(tableCounter).getThreshhold();
            data[tableCounter][1] = algVOT;
            data[tableCounter][2] = manVOT;
            data[tableCounter][3] = wiList.get(tableCounter).getProcessed();
            data[tableCounter][4] = wiList.get(tableCounter).getFlag();
            data[tableCounter][5] = wiList.get(tableCounter).getRemarks();
        }
            
        table = new JTable(new MyTableModel(data));
        table.setPreferredScrollableViewportSize(new Dimension(975, 500));
        table.setFillsViewportHeight(true);
        TableColumn column = null;
        column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(300);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(2);
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(3);
        column.setPreferredWidth(75);
        column = table.getColumnModel().getColumn(4);
        column.setPreferredWidth(25);
        column = table.getColumnModel().getColumn(5);
        column.setPreferredWidth(400);
  
        
//        return data;
    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Voice Onset Time");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new CardLayout());
        framePanel = new JPanel(new CardLayout());
        
        VoiceOnsetTime vot = new VoiceOnsetTime();
        
//        framePanel.setPreferredSize(new java.awt.Dimension(1000, 1100));
        framePanel.add(vot.startPl, "startPl");
        framePanel.add(vot.newProjectPl, "newProjectPl");
        framePanel.add(vot.displayProjectsPl, "displayProjectsPl");
        framePanel.add(vot.processPl, "processPl");
        frame.add(framePanel);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
}
