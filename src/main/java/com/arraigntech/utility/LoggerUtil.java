/**
 * 
 */
package com.arraigntech.utility;

/**
 * @author Bhaskara S
 *
 */
public class LoggerUtil {

    public static void log(String message){

        System.out.println("[" + Thread.currentThread().getName() +"] - " + message);

    }
}
