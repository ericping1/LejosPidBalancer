import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Scooter class represents the two wheeled self balacing robot. The run()
 * method utilizes a pid controller to maintain balance. Forward and backwards
 * movement will be induced by unbalancing the robot to force it to lean
 * forwards or backwards
 *
 * @author Eric Ping
 */
public class Scooter extends Thread {
    private static final double THREE_QUARTERS_TURN = 270;
    private static final double NEGATIVE_THREE_QUARTERS_TURN = -270;
    private static final double FULL_CIRCLE = 360;
    private static final double POWER_THRESHHOLD = 70;
    private static final double MAX_POWER = 100;

    // Two wheel motors and gyro sensor
    private UnregulatedMotor leftMotor = new UnregulatedMotor(MotorPort.A);
    private UnregulatedMotor rightMotor = new UnregulatedMotor(MotorPort.B);
    private EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);

    // Values for the pid controller
    private double pCoeff = 2.2;
    private double iCoeff = .07;
    private double dCoeff = 1.3;

    /**
     * Method continuously balances the scooter, taking data every 10
     * milliseconds and adjusting the motor accordingly
     */
    public void run() {

        // Calibrate gyroSensor (make sure sensor is pointed straight upwards
        gyroSensor.reset();

        // Notify user and set thread priority
        Sound.beepSequenceUp();
        Thread.currentThread().setPriority(MAX_PRIORITY);

        // Array for storing angle and velocity values
        float[] angle = new float[1];
        float[] speed = new float[1];

        double power = 0;
        double pValue;
        double iValue;
        double dValue;

        // Loop until button pressed
        while (!Button.ENTER.isDown()) {
            // Note: move these variables to the outside of while loop
            gyroSensor.getRateMode().fetchSample(speed, 0);
            gyroSensor.getAngleMode().fetchSample(angle, 0);

            // Set p value
            pValue = angle[0];

            // Convert reflex angles to acute angles
            if (pValue > THREE_QUARTERS_TURN) {
                pValue = pValue - FULL_CIRCLE;
            }
            if (pValue < NEGATIVE_THREE_QUARTERS_TURN) {
                pValue = FULL_CIRCLE + pValue;
            }

            // Set i and d values of PID controller
            iValue = power;
            dValue = speed[0];

            // Counter-intuitive, add by integral to change the angle
            power = power + -1.0 * pCoeff * pValue + 1 * iCoeff * iValue + -1.0 * dCoeff * dValue;

            // Prevent drift from getting out of control
            if (power > POWER_THRESHHOLD) {
                power = MAX_POWER;
            }
            if (power < -1.0 * POWER_THRESHHOLD) {
                power = -1.0 * MAX_POWER;
            }

            // Set power and delay
            rightMotor.setPower((int) power);
            leftMotor.setPower((int) power);
            Delay.msDelay(10);
        }

        leftMotor.close();
        rightMotor.close();
        gyroSensor.close();
    }
}
