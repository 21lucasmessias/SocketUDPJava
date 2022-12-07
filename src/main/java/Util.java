import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Util {

    static void randomSleepMilli(int max){
        int min = 1000;
        int range = max - min;
        try {
            Thread.sleep((int)(Math.random() * range) + min);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static String getNow() {
        final var now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm:ss:SSSS"));
    }
}
