/*
 * To change this license header, 
choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vot;

import java.io.File;

/**
 *
 * @author jjones
 */
public class WavInfo{
    
//<editor-fold defaultstate="collapsed" desc="class fields">
    private File FILE;
    private int nSamples;
    private long samplingRate;
    private int maxSample;
    private double stThreshhold;
    private int threshhold;
    private int fileIndex;
    private double groupStart;
    private double groupEnd;
    private double algVOT;
//    private ArrayList<Double> algVOT;
    
    private Double manVOT;
    private int processed;
    private Boolean flag;
    private String remarks;
    
    
    
//</editor-fold>
    public WavInfo(){
        this.FILE = null;
        nSamples = 0;
        samplingRate = 0;
        maxSample = 0;
        stThreshhold = 0;
        threshhold = 0;
        groupStart = 0.0;
        groupEnd = 0.0;
        algVOT = 0.0;
        manVOT = 0.0;
        processed = 0;
        fileIndex = 0;
        flag = false;
        remarks = "none";
    }
    public WavInfo(File file){
        this.FILE = file;
        nSamples = 0;
        samplingRate = 0;
        maxSample = 0;
        stThreshhold = 0;
        threshhold = 0;
        groupStart = 0.0;
        groupEnd = 0.0;
        algVOT = 0.0;
        manVOT = 0.0;
        processed = 0;
        fileIndex = 0;
        flag = false;
        remarks = "none";
    }
    public WavInfo(File file,
            int nSamples,
            int samplingRate,
            int maxSample,
            Double stThreshhold,
            int threshhold,
            Double groupStart,
            Double groupEnd,
            Double algVOT,
            Double manVOT,
            int processed,
            Boolean flag,
            String remarks,
            int fileIndex){
        this.FILE = file;
        this.nSamples = nSamples;
        this.samplingRate = samplingRate;
        this.maxSample = maxSample;
        this.stThreshhold = stThreshhold;
        this.threshhold = threshhold;
        this.groupStart = groupStart;
        this.groupEnd = groupEnd;
        this.algVOT = algVOT;
        this.manVOT = manVOT;
        this.processed = processed;
       this.fileIndex = fileIndex;
       this.flag = flag;
       this.remarks = remarks;
    }
    
//****************************************************************************
//  setter methods
//****************************************************************************
    
    protected void setFilename(File file){
        this.FILE = file;
    }
    protected void setNSamples(int nSamples){
        this.nSamples = nSamples;
    }
    protected void setSamplingRate(long samplingRate){
        this.samplingRate = samplingRate;
    }
    protected void setMaxSample(int maxSample){
        this.maxSample = maxSample;
    }
    protected void setStThreshhold(double stThresh){
        this.stThreshhold = stThresh;
    }
    protected void setThreshhold(int thresh){
        this.threshhold = thresh;
    }
    protected void setGroupStart(Double groupStart){
        this.groupStart = groupStart;
    }
    protected void setGroupEnd(Double groupEnd){
        this.groupEnd = groupEnd;
    }
    protected void setAlgVOT(Double algVOT){
        this.algVOT = algVOT;
    }
    protected void setManVOT(Double manVOT){
        this.manVOT = manVOT;
    }
    protected void setProcessed(int processed) {
        this.processed = processed;
    }
    protected void setFlag(Boolean flag){
        this.flag = flag;
    }
    protected void setRemarks(String remarks){
        this.remarks = remarks;
    }
    protected void setFileIndex(int fileIndex){
        this.fileIndex = fileIndex;
    }
    
    

//****************************************************************************
//  getter methods
//****************************************************************************
    protected File getFilename(){
        return FILE;
    }
    protected int getNSamples(){
        return nSamples;
    }
    protected long getSamplingRate(){
         return samplingRate;
    }
    protected int getMaxSample(){
        return maxSample;
    }
    protected double getStThreshhold(){
        return stThreshhold;
    }
    protected int getThreshhold(){
        return threshhold;
    }
    protected WavInfo getWaveInfo(){
        return this;
    }
    protected Double getGroupStart(){
        return groupStart;
    }
    protected Double getGroupEnd(){
        return groupEnd;
    }
    protected Double getAlgVOT(){
        return algVOT;
    }
    protected Double getManVOT(){
        return manVOT;
    }
    protected int getProcessed(){
        return processed;
    }
    protected Boolean getFlag(){
        return flag;
    }
    protected String getRemarks(){
        return remarks;
    }
    protected int getFileIndex(){
        return fileIndex;
    }
    
    protected String returnWavInfoAsString(){
        String wi;
        wi = FILE.toString() + "\t" +
                algVOT + "\t" +
                manVOT + "\t" +
                flag + "\t" +
                remarks;

        return(wi);
    }
    
//END OF CLASS WAVINFO    
}
