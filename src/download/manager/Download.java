/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package download.manager;

import java.net.URL;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AKHIL
 */
public class Download implements Runnable {

    ErrorListener el;
    public static int DOWNLOADING=0;
    public static int PAUSE=1;
    public static int RESUME=2;
    public static int CANCELLED=4;
    public static int COMPLETE=3;
    int status=-1;
    String saveLocation=null;
    URL url;
    int size;
    int downloaded=0;
   public static String fileName;
    StatusChanged c;
    private int MAX_BUFFER_SIZE=1024;
    
   public Download(URL url,String loc,StatusChanged c,ErrorListener el)
    {
        saveLocation=loc;
        this.url=url;
           size=-1;  
           downloaded=0;
           this.c=c;
           this.el=el;
    }
    private void download()
    {
        Thread thread=new Thread(this);
        thread.start();
    }
    
   public void startDownload()
    {
        status=DOWNLOADING;
        size=0;
        statusChanged();
        download();
    }
   public void pause()
    {
        status=PAUSE;
        statusChanged();
    }
   public void resume()
    {
        status=DOWNLOADING;
        statusChanged();
        System.out.println(downloaded);
        download();
    }
   public void stop()
   {
       status=CANCELLED;
       statusChanged();
       
   }
   public void setDownloaded(int i)
   {
       this.downloaded=i;
   }
   int noOfBytes=0;
   public static int prevSpeed=0;
   public static int speed=0;
    private void statusChanged()
    {  
        if(sw!=null && noOfBytes==1024)
    {sw.stop();
       float time= (float)sw.getElapsedTime().toMillis()/1000;
         float s=0;
       if(time!=0)
     s=1024/time;
       prevSpeed=speed;
       speed=(int)s;
       noOfBytes=0;
       //System.out.println(speed);
         c.setStatus(status, (int)getProgress(),downloaded,size,speed);
    }else
        {
              c.setStatus(status, (int)getProgress(),downloaded,size,prevSpeed);
        }
    
      
    }
    StopWatch sw=null;
    @Override
    public void run() {
          InputStream stream=null;
            HttpURLConnection conn=null;
           File dir =null;
           File actualFile=null;
            RandomAccessFile file=null;
        try {
           
             stream=null;
             conn=(HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Range", "bytes="+downloaded+"-");
   
            conn.connect();
            if(conn.getResponseCode()/100!=2)
            {
               System.out.print("rc");
            }
            int length=conn.getContentLength();
            if(length<1)
            {
                error(2);
            }
            if(size==0)
            {
                size=length;
                statusChanged();
            }
            System.out.println(size+"bytes");
         
 dir = new File (saveLocation);
 Download.fileName=getFilename(url);
 actualFile = new File (dir, getFilename(url));
            file=new RandomAccessFile(actualFile,"rw");
            
            file.seek(downloaded);
            stream=conn.getInputStream();
            while(status==DOWNLOADING)
            {
                
                byte buffer[];
                if(size-downloaded >MAX_BUFFER_SIZE)
                {
                    buffer=new byte[MAX_BUFFER_SIZE];
                }else
                {
                    buffer=new byte[size-downloaded];
                }
                if(noOfBytes==0)
                {sw=new StopWatch();
                sw.start();}
                
                int read=stream.read(buffer);
                if(read==-1)
                    break;
                noOfBytes++;
                file.write(buffer,0,read);
                downloaded+=read;
                
                statusChanged();
            }
            
            if(status==DOWNLOADING)
            {
                status=COMPLETE;
                   statusChanged();
            }
        }catch (MalformedURLException e) {
  error(0);
    } catch (IOException ex) {
  error(1);
        }finally{
        
              try {
                  if(stream!=null)
                      try {
                          stream.close();
                      } catch (IOException ex) {
                          Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
                      }
                  if(conn!=null)
                      conn.disconnect();
                  if(file!=null)
                      file.close();
                  actualFile=null;
                  dir=null;
                 
                      
              } catch (IOException ex) {
                  Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
              }
    }
        
        
    }

    private float getProgress() {
return size>0? ((float)downloaded/size)*100: 0;
    }

   

    private String getFilename(URL url) {
       String file=url.getFile();
     //  System.out.print(url);
       String sub= file.substring(file.lastIndexOf('/')+1);
      //  System.out.print(sub);
        return sub;
    }

    private void error(int i) {
     if(i==0)
         el.error("Invalid Url");
     else if(i==1)
         el.error("Please check your internet connection");
     else
         el.error("Unable to Download File");
    }
    
}
