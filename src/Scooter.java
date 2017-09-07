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
    // Two wheel motors and gyro sensor
    private UnregulatedMotor leftMotor = new UnregulatedMotor(MotorPort.A);
    private UnregulatedMotor rightMotor = new UnregulatedMotor(MotorPort.B);
    private EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S1);

    // Values for the pid controller
    private double pCoeff = 70;  //60
    private double iCoeff = 0;
    private double dCoeff = .10;  //.15

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
        float[] angle = new float[1];
        float[] speed = new float[1];

        // Loop until button pressed
        while (!Button.ENTER.isDown()) {
            double pValue;
            double iValue = 0;
            double dValue;

            gyroSensor.getRateMode().fetchSample(speed, 0);
            gyroSensor.getAngleMode().fetchSample(angle, 0);

            pValue = angle[0];

            if (pValue > 270) {
                pValue = pValue - 360;
            }
            if (pValue < -270) {
                pValue = 360 + pValue;
            }

            iValue += pValue;
            dValue = speed[0];

            //LCD.drawString("" + pValue, 0, 0);


            double power = -1.0 * pCoeff * pValue + iCoeff * iValue + -1.0 * dCoeff * dValue;

            LCD.drawString("" + power, 0, 0);

            rightMotor.setPower((int) power);
            leftMotor.setPower((int) power);

            Delay.msDelay(10);

        }

        leftMotor.close();
        rightMotor.close();
        gyroSensor.close();
    }

    //private
}
