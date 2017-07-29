/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package download.manager;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AKHIL
 */
public class StopWatch {
    Instant startTime, endTime;
    Duration duration;
    boolean isRunning = false;

    public void start() {
        if (isRunning) {
            throw new RuntimeException("Stopwatch is already running.");
        }
        this.isRunning = true;
        startTime = Instant.now();
    }

    public Duration stop() {
        this.endTime = Instant.now();
        if (!isRunning) {
            throw new RuntimeException("Stopwatch has not been started yet");
        }
        isRunning = false;
        Duration result = Duration.between(startTime, endTime);
        if (this.duration == null) {
            this.duration = result;
        } else {
            this.duration = duration.plus(result);
        }

        return this.getElapsedTime();
    }

    public Duration getElapsedTime() {
        return this.duration;
    }

    public void reset() {
        if (this.isRunning) {
            this.stop();
        }
        this.duration = null;
    }
    public static void main(String a[])
    {
        StopWatch sw=new StopWatch();
        sw.start();
      Thread t=  new Thread(new Runnable() {
            @Override
            public void run() {
                int c=0;
                System.out.println(c);
               while(c!=60)
               {
                   if(c==60)
                   { 
                        sw.stop();
    System.out.print(sw.getElapsedTime().toMinutes());
                       break;
                   
                   }
                   }
                    try { Thread.sleep(1000);
                        c++;
                        System.out.println(c);
                       
                    } catch (InterruptedException ex) {
                        Logger.getLogger(StopWatch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
               }
            
        });
      t.start();
    
   
    }
    
}
