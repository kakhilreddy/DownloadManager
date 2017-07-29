/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package download.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AKHIL
 */
public class DownloadManager implements StatusChanged {
URL url;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String []url;
       Object []x=url;
        Integer i=3;
        String a="a";
        String b="a";
        if((a==b))
             System.out.println("I look good! and nice!".trim());
        else   if(i<3)
            url=null;
        
        Scanner s=new Scanner(System.in);
        url=s.next();
    try {
        URL u=new URL(url);
           new DownloadManager().start(u);
    } catch (MalformedURLException ex) {
        Logger.getLogger(DownloadManager.class.getName()).log(Level.SEVERE, null, ex);
    }
     
       
    }
 void start(URL url)
{
    Download d= new Download(url,null,DownloadManager.this,null);
    d.startDownload();
}
    @Override
    public void setStatus(int status, int percentage,int d,int s,int sp) {
System.out.println("Status:"+status +" Downloaded : "+percentage);
    }
    
}
