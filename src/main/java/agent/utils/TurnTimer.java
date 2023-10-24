package agent.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnTimer {

    private int minutes;
    private int seconds;
    private long lastStartMillis;
    private long totalMillis;

    public void startLogTime(long millis) {
        this.lastStartMillis = millis;
    }

    public void stopLogTime(long millis) {
        totalMillis += millis - lastStartMillis;
        int totalSeconds = (int) (totalMillis / 1000);
        this.minutes = totalSeconds / 60;
        this.seconds = totalSeconds % 60;
    }

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
