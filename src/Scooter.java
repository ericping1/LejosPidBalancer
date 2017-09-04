import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

/**
 * Scooter class represents the two wheeled self balacing robot. The run()
 * method utilizes a pid controller to maintain balance. Forward and backwards
 * movement will be induced by unbalancing the robot to force it to lean
 * forwards or backwards
 *
 * @author Eric Ping
 */
public class Scooter extends Thread {
    // Two wheel motors and gyro sensor
    private UnregulatedMotor leftMotor = new UnregulatedMotor(MotorPort.A);
    private UnregulatedMotor rightMotor = new UnregulatedMotor(MotorPort.B);
    private EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);

    // Values for the pid controller
    private double pCoeff = 15;
    private double iCoeff = 0;
    private double dCoeff = .1;

    /**
     * Method continuously balances the scooter, taking data every 10
     * milliseconds and adjusting the motor accordingly
     */
    public void run() {
        rightMotor.resetTachoCount();
        leftMotor.resetTachoCount();

        // Calibrate gyroSensor (make sure sensor is pointed straight upwards
        gyroSensor.reset();

        // Notify user and set thread priority
        Sound.beepSequenceUp();
        Thread.currentThread().setPriority(MAX_PRIORITY);

        // Array for storing angle and velocity values
        float[] angleAndRates = new float[2];

        // Loop until button pressed
        while (!Button.ENTER.isDown()) {
            double pValue;
            double iValue = 0;
            double dValue;

            gyroSensor.getAngleAndRateMode().fetchSample(angleAndRates, 0);
            pValue = angleAndRates[0];
            iValue += pValue;
            dValue = angleAndRates[1];

            double power = pCoeff * pValue + iCoeff * iValue + dCoeff * dValue;
            rightMotor.setPower((int) power);
            leftMotor.setPower((int) power);

        }

        leftMotor.close();
        rightMotor.close();
        gyroSensor.close();
    }

    //private
}
