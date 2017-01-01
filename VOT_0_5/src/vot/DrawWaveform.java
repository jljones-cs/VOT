
package vot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author jjones
 */
public class DrawWaveform extends JPanel implements ActionListener, MouseListener {
    private final int PREF_W = 1000;
    private final int PREF_H = 200;
//    int refreshRate = 0;
//    int delay = 0;
    private int x = 0;
    Color color = Color.RED;
//    File wi.getFilename() = null;
    private ArrayList<Integer> samples = null; 
    private ArrayList<Integer> absSamples = new ArrayList<>();
    private ArrayList<Double> stSamples = new ArrayList<>();
    private ArrayList<Double> drawSamples = new ArrayList<>();
//    private int wi.getMaxSample() = 0;
//    private double wi.getStThreshhold() = 0;
//    int threshhold = 0;
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    double y = 0;
    private BufferedImage img;
//    private int this.getWidth() = 0;
//    private Double wi.getAlgVOT() = 0.0;
//    private long samplingRate = 0;
//    private Double wi.getManVOT() = 0.0;
     boolean painted = false;
     Timer tm = null;
     Clip clip;
     
     private WavInfo wi;
    
   
    public DrawWaveform(){
        samples = new ArrayList<>();
        tm = new Timer(16,this);
        wi = new WavInfo();
        addMouseListener(this);
    }
                
        
      
    public void setWavInfo(WavInfo wi){
        this.wi = wi;
    }
    public WavInfo getWavInfo(){
        return this.wi;
    }
//     Double getManVOT(){
//        return wi.getManVOT();
//    }
     private void doDrawing()    {
        img = new BufferedImage(getWidth(), 
                                getHeight(), 
                                BufferedImage.TYPE_INT_RGB);
        x1 = 0;
        x2 = 0;
        y1 = 0;
        y2 = 0;
        Graphics2D g = img.createGraphics();
        g.setColor(Color.white);
        for(int i = 0;i<drawSamples.size();i++){
            if(i % (drawSamples.size()/1000) == 0 & i > 0){
                x2++;
            }
            if(i % ((drawSamples.size()/1000)/4) == 0 & i > 0){
            y = drawSamples.get(i)*-100+100;
                y2 = (int) y;
                g.drawLine(x1,y1,x2,y2);
                x1 = x2;
                y1 = y2;
                
            }
        }
        System.out.println("*******************  hello from doDrawing()  ***************************************");
        System.out.println("************************************************************************************");
        System.out.println("from doDrawing, drawSamples.size(): " + drawSamples.size());
        System.out.println("from doDrawing, samplingRate: " + wi.getSamplingRate() );
        System.out.println("from doDrawing, filename: " + wi.getFilename() );
        g.setColor(Color.green);
//        System.out.println("wi.getStThreshhold(): " + (int) (wi.getStThreshhold()*100) );
        g.drawLine(0,100- (int) (wi.getStThreshhold()*100),1000,100-(int) (wi.getStThreshhold()*100) );
        g.setColor(Color.red);
        g.setStroke(new BasicStroke(2));
        g.drawLine( (int) (wi.getAlgVOT()*this.getWidth()/(drawSamples.size()/wi.getSamplingRate()) ),0,
              (int)  (  wi.getAlgVOT()*this.getWidth()/(drawSamples.size()/wi.getSamplingRate()) ),200);
        painted = true;
        
        
        
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }      
  
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!painted){        
            doDrawing();
            
        }
         Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, 0, 0, this);
        g2.setColor(Color.red);
        g2.drawLine(x,0,x,200);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.cyan);
        g2.drawLine( (int) (wi.getManVOT()*this.getWidth()/(drawSamples.size()/wi.getSamplingRate()) ),0,
              (int)  (  wi.getManVOT()*this.getWidth()/(drawSamples.size()/wi.getSamplingRate()) ),200);
//      
    }
    
   @Override
    public void actionPerformed(ActionEvent e){
       if(clip.isActive()){
           x = (int)clip.getLongFramePosition()/(drawSamples.size()/this.getWidth());
           repaint();
       }else{
           samples.clear();
           tm.stop();
       }
    }

    public void playWaveform() throws LineUnavailableException, 
            UnsupportedAudioFileException, IOException{
      
       JLabel jl = new JLabel();
       JLabel jl2 = new JLabel();

//       getSamples(wi.getFilename());
       tm.start();
       clip = AudioSystem.getClip();
       AudioInputStream ais = AudioSystem.getAudioInputStream( wi.getFilename() );
       clip.open(ais);
       clip.start();

        
         
     }
    public void detectFeatures(){
        clearSamples();
        getSamples();
        processSamples();
        calculateThreshhold();
        calculateVOT();
        wi.setProcessed(1);
        painted = false;
        repaint();
    }
    public void clearSamples(){
        samples.clear();
        absSamples.clear();
        stSamples.clear();
        drawSamples.clear();
    }
    public void getSamples(){
//        ArrayList<Integer> samples = new ArrayList<>();
       try{
           // Open the wav wi.getFilename() specified as the first argument
           WavFile wavFile = WavFile.openWavFile(wi.getFilename());
           wi.setSamplingRate(wavFile.getSampleRate() );
           // Display information about the wav wi.getFilename()
           //wavFile.display();

           // Get the number of audio channels in the wav wi.getFilename()
           int numChannels = wavFile.getNumChannels();

           // Create a buffer of 100 frames
           // double[] buffer = new double[100 * numChannels];
           int[] buffer = new int[100 * numChannels];
           int framesRead;
           // double min = Double.MAX_VALUE;
           // double max = Double.MIN_VALUE;
           int min = Integer.MAX_VALUE;
           int max = Integer.MIN_VALUE;

           do
           {
                   // Read frames into buffer
                   framesRead = wavFile.readFrames(buffer, 100);

                   // Loop through frames and look for minimum
//                    and maximum value
                   for (int s=0 ; s<framesRead * numChannels ; s++)
                   {
                           if (buffer[s] > max) max = buffer[s];
                           if (buffer[s] < min) min = buffer[s];
                           samples.add(buffer[s]);
                   }
           }
           while (framesRead != 0);
           wavFile.close();

       }
       catch (Exception e)
       {
               System.err.println(e);
       }

//        return(samples);
   }

    public void processSamples(){
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from processSamples  ************************************");
        System.out.println("************************************************************************************");
        System.out.println("File: " + wi.getFilename());
        System.out.println("samples.size(): " + samples.size());
//        System.out.println("absSamples.size(): " + absSamples.size());
        wi.setNSamples(samples.size());
        for(int i = 0; i<samples.size();i++){
           absSamples.add(Math.abs(samples.get(i)));
           if(Math.abs(samples.get(i)) > wi.getMaxSample()) {
               wi.setMaxSample((Math.abs(samples.get(i)) ) );
           }
        }
        System.out.println("absSamples.size(): " + absSamples.size());
        System.out.println("maxSample: " + wi.getMaxSample());
        for(int i = 0; i<absSamples.size();i++){
            stSamples.add((double)absSamples.get(i)/wi.getMaxSample());
           drawSamples.add((double)samples.get(i)/wi.getMaxSample() );
        }
//        System.out.println("stSamples.size(): " + stSamples.size());
    }
    
    public void calculateThreshhold(){
        int minMean = 0;
        int groupSum = 0;
//        ArrayList<Integer> sortedSamples =  samples;
//        Collections.sort(sortedSamples);
//      threshhold = ( sortedSamples.get((int) 9*sortedSamples.size()/10)) *2;
        for(int i = 0;i<5;i++){
            for(int j = i*100;j<i*100+100;j++){
                groupSum += samples.get(j);
            }
            if(minMean == 0){minMean = groupSum;}
            if(groupSum < minMean){minMean = groupSum;}
        }

        wi.setThreshhold( (minMean / 100) );
        wi.setStThreshhold(( Math.abs(((double) 
               wi.getThreshhold()*2 / wi.getMaxSample() ) ) ) );
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from calculateThreshhold  *******************************");
        System.out.println("************************************************************************************");
        System.out.println("threshhold: " + wi.getThreshhold() );
        System.out.println("Stthreshhold: " + wi.getStThreshhold() );
        System.out.println("maxSample: " + wi.getMaxSample());
    }
    
    
    public void calculateVOT(){
        System.out.println();
        System.out.println();
        System.out.println("*******************  hello from calculateVOT  **************************************");
        System.out.println("************************************************************************************");
        int nStop = 200;
        int nZero = 0;
        int groupMin = 4000;
        int groupStart = 0;
        int groupEnd = 0;
        
        int cntI = 0;
        int cntJ = 0;
//        int J = 0;
        
//        System.out.println("groupStart: " + 
//                groupStart + " groupEnd: " + groupEnd);
//        System.out.println("wi.getMaxSample(): " + wi.getMaxSample() );
        boolean bool = true;
        System.out.println("size of stSamples: " + stSamples.size());
        System.out.println("wi.getStThreshhold(): " + wi.getStThreshhold());
//        System.out.println("wi.getFilename()name class: " + 
//                wi.getFilename().getClass());
           if(!wi.getFilename().
                   toString().
                   equals("C:\\Users\\jjones\\Documents\\"
                           + "Research\\waveFiles\\an_l.wav")){
               System.out.println("working on file: " + wi.getFilename().
                   toString() );

        do{
            Boolean bools = false;
            if(cntI < cntJ){cntI++;continue;}
            if(cntI >= samples.size()){bool = false;continue;}
            if( stSamples.get(cntI)>  wi.getStThreshhold() ){
                if(bools)System.out.println("1");
                groupStart = cntI;
                nZero = 0;
//      2
                for(cntJ = groupStart; cntJ< stSamples.size();cntJ++){
                    if(bools)System.out.println("2");
//      3
                    if( stSamples.get(cntJ) < wi.getStThreshhold() 
                            & nZero == 0){
                       if(bools) System.out.println("3");
                        nZero++;
                        groupEnd = cntJ;
//      4
                    }else if( stSamples.get(cntJ) <  wi.getStThreshhold() ){
                       if(bools) System.out.println("4");
                        nZero++;
//      5
                    }else if( stSamples.get(cntJ) >  wi.getStThreshhold() ){
                       if(bools) System.out.println("5");
                        nZero = 0;
                    }
                 //   System.out.println("nZero = " + 
//                          nZero + " nStop = " + nStop);
//      6
                    if(nZero == nStop){
                        if(bools)System.out.println("6");
                       //  System.out.println("nZero = " + 
//                              nZero + " nStop = " + nStop); 
//      7
                        if(groupEnd - groupStart > groupMin){
                            if(bools)System.out.println("7");
//                            System.out.println("groupStart: " + 
//                                    (double)groupStart/samplingRate() +
//                                    " groupEnd: " + 
//                                    (double)groupEnd/samplingRate());
                            wi.setGroupStart((double)groupStart);
                            wi.setGroupEnd((double)groupEnd);
//                            VOT.add(wi.getStThreshhold());
                            wi.setAlgVOT(((double)groupStart/wi.getSamplingRate()) );
                            bool = false;
                        }
                        break;
                    }
                }
                if(cntI >= stSamples.size()){bool = false; break;}
            }
            
            cntI++;
          //  if(cntI%10000 == 0){
           //     System.out.println(cntI + " " + J);
         //   }
        }while(bool);
    }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("mouse clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("mouse pressed");
        wi.setManVOT((double) e.getX()*
            (drawSamples.size()/wi.getSamplingRate())/this.getWidth() );
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        System.out.println("mouse released");    
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("mouse entered");    
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("mouse exited");    
    }
            
    
     
     
    
}
