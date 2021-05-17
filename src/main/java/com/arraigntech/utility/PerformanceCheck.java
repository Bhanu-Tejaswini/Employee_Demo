/**
 * 
 */
package com.arraigntech.utility;




import static com.arraigntech.utility.LoggerUtil.log;

import org.apache.commons.lang3.time.StopWatch;
import static java.lang.Thread.sleep;



/**
 * @author Bhaskara S
 *
 */
public class PerformanceCheck {
	public static StopWatch stopWatch = new StopWatch();

	public static void delay(long delayMilliSeconds)  {
        try{
            sleep(delayMilliSeconds);
        }catch (Exception e){
            LoggerUtil.log("Exception is :" + e.getMessage());
        }

    }

	public static String transForm(String s) {
		PerformanceCheck.delay(500);
		return s.toUpperCase();
	}

	public static void startTimer() {
		stopWatch.start();
	}

	public static void timeTaken() {
		stopWatch.stop();
		log("Total Time Taken : " + stopWatch.getTime());
	}

	public static void stopWatchReset() {
		stopWatch.reset();
	}

	public static int noOfCores() {
		return Runtime.getRuntime().availableProcessors();
	}

}
