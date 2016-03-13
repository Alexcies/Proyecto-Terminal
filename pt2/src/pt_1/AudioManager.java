package pt_1;


import javax.sound.sampled.AudioInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ale
 */
public class AudioManager {
    AudioPlayer player= new AudioPlayer();
    AudioRecorder recorder = new AudioRecorder();
    AudioInputStream audioinputStream;
    
    
   void Grabar(boolean status){
    if (status){
        recorder.start();
    }
    else{
        recorder.stop();
    }
    
    
        
    }
    
   String Getname(){
       System.out.println("este es el nombre" + recorder.getname());
       return recorder.getname();
   }
   
    void Guardar(String name, AudioInputStream audioInputStream){
        recorder.GuardarRecord(name, audioInputStream);
        
    }
    
    /*
    void Reproducir(AudioInputStream audioInputStream){
    player.setAudioPlayer(audioInputStream);
    player.start();
    
    }
    */
    
    void Reproducir(String name){
    player.setAudioPlayer(name);
    player.start();
    
    }
    
    
      void BuscarAudio(String nombre){
    
    }
      
  
}
    