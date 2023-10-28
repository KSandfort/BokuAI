package agent.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * This class provides a tool to log times of a player's turn.
 * You can start and stop a certain log time and output the total logged time as a string.
 */
@Getter
@Setter
public class TurnTimer {

    private int minutes;
    private int seconds;
    private long lastStartMillis;
    private long totalMillis;

    /**
     * Starts to log the time.
     * @param millis current system milliseconds
     */
    public void startLogTime(long millis) {
        this.lastStartMillis = millis;
    }

    /**
     * Stops to log the time.
     * @param millis current system milliseconds
     */
    public void stopLogTime(long millis) {
        totalMillis += millis - lastStartMillis;
        int totalSeconds = (int) (totalMillis / 1000);
        this.minutes = totalSeconds / 60;
        this.seconds = totalSeconds % 60;
    }

    /**
     * Creates a string that displays the total logged time in MM:SS.
     * @return formatted time string
     */
    public String getTimeLabel() {
        // Format string properly
        String minutesStr;
        if (minutes < 10) {
            minutesStr = "0" + minutes;
        }
        else {
            minutesStr = Integer.toString(minutes);
        }
        String secondsStr;
        if (seconds < 10) {
            secondsStr = "0" + seconds;
        }
        else {
            secondsStr = Integer.toString(seconds);
        }
        return minutesStr + ":" + secondsStr;
    }

}
