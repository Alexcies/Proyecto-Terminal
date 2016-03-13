package pt_1;


import SoundApi.JavaSound;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ale
 */



 class AudioRecorder implements Runnable {

        TargetDataLine line;
        Thread thread;
         String errStr; 
        double duration, seconds;
      public AudioInputStream audioInputStream,aud;
           AudioFormat audioFormat;
           File file;
String name = "salado.wav";
           
            public  String getname(){
                 
        
        return name;
    
    
    }
        
      void GuardarRecord(String name, AudioInputStream audioInputStream ){
                 
        
        if (audioInputStream == null) {
            reportStatus("No loaded audio to save");
            return;
        } 

        // reset to the beginnning of the captured data
        try {
            audioInputStream.reset();
        } catch (Exception e) { 
            reportStatus("Unable to reset stream " + e);
            return;
        }

       file = new File(name);
        try {
            if (AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) { reportStatus(ex.toString()); }     
          
      }        

       private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
          //  samplingGraph.repaint();
        }
    }
      
        public void start() {
            errStr = null;
            thread = new Thread(this);
            thread.setName("Capture");
            thread.start();
        }

        public void stop() {
            thread = null;
            System.out.println("sale de guardar");
                   if (audioInputStream!=null){
System.out.println("se ejecuta esta parte en el get ");
            }

        }
        
        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
            //    samplingGraph.stop();             
                System.err.println(errStr);
          //      samplingGraph.repaint();
            }
        }

        public void run() {
System.out.println("entra a guardar");
            duration = 0;
            audioInputStream = null;
            
            // define the required attributes for our line, 
            // and make sure a compatible line is supported.
AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
            AudioFormat format = new AudioFormat(encoding, 44100, 16, 2, (16/8)*2, 44100, true);
          //********  AudioFormat format = formatControls.getFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                        
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for capture.

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) { 
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) { 
                shutDown(ex.toString());
                JavaSound.showInfoDialog();
                return;
            } catch (Exception ex) { 
                shutDown(ex.toString());
                return;
            }

            // play back the captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;
            
            line.start();

            while (thread != null) {
                if((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);
            }

            // we reached the end of the stream.  stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
            }

            // load bytes into the audio input stream for playback

            byte audioBytes[] = out.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);
         
            long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;

            try {
                audioInputStream.reset();
                         GuardarRecord(name,audioInputStream);  

            } catch (Exception ex) { 
              //  return audioInputStream;
            }
        }
    }