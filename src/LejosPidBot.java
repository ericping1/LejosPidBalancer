import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * LejosPidBot is the main application for the self balancing lego robot.
 * Much of the code setting up the lejos environment taken from
 * BjornChristenson's gyroboy (https://github.com/BjornChristensen/Gyroboy)
 *
 * Implementation of pid balancing and forward movement is different, allowing
 * for more severe speeds and tilt angles (ideally)
 *
 * @author Eric Ping
 */
public class LejosPidBot {

    /**
     * Main method of program that creates an scooter object which drive around
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        GraphicsLCD lcd = BrickFinder.getDefault().getGraphicsLCD();
        int screenWidth = lcd.getWidth();
        int screenHeight = lcd.getHeight();
        lcd.setFont(Font.getDefaultFont());
        lcd.drawString("Pid balancer", screenWidth / 2, 40, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);

        Scooter scooter = new Scooter();
        scooter.start();
    }
}
