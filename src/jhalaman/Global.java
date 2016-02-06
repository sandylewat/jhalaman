/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Global {
    private static final String author = "Sandy Socrates";
    private static final String authorEmail = "sandy_socrates@live.com";
    
    /*test constants*/
    private static final String testImageAddress = "data\\Train Clean\\13.jpg";
    
        
    public static String getAuthor() {
        return author;
    }
    
    public static String getAuthorEmail() {
        return authorEmail;
    }
    
    public static String getTestImageAddress() {
        return testImageAddress;
    }
}
