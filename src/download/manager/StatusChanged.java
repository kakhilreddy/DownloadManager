/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package download.manager;

/**
 *
 * @author AKHIL
 */
public interface StatusChanged {
    
    public void setStatus(int status,int percentage,int d,int s,int speed);
    
}
