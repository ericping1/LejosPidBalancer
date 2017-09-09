# LejosPidBalancer: A simple framework for a self balancing 2 wheeled bot

A small program that uses a basic PID controller to balance a two-wheeled robot made from a lego mindstorms kit. It utilizes solely a gyroscope sensor and two motors for balance. This project's still not finished and very unpolished; currently all it does is mostly stand still (it can drift a little). Tuning of the PID controller is also not yet complete and remote control functionality hasn't been added yet.

I built my robot based off the BALANC3R robot found in this link (http://robotsquare.com/2014/07/01/tutorial-ev3-self-balancing-robot/). I wanted to create a foundation for further projects. The original code for the self balancing robot is written in RCX Code, which I find difficult to read and parse through. In addition, I found other repositories to be needlessly complicated and difficult to read.

# PID controller

The proportional term directly related to angle of the robot's orientation. The robot must nearly be perfectly balanced the moment the program is launched, as that's when the gyro sensor is calibrated. Any slight forwards or backwards tilt during initial calibration will cause the robot to continually drift in that direction. I've found no reliable ways to prevent this so far.

The derivative term is directly taken from the gyroscope's measurements for angular velocity.

The integral term is taken directly from the power applied to the motors. Ideally it would resist any continual drifting or slanting in one direction; I've found it incredibly difficult to tune this term, however. The purpose of the integral term is to control the speed of the robot.

Counterintuitively, the integral component in my controller is additive, and not balancing. That is, if the robot is drifting in one direction, the integral component of my controller would increase the wheels' speed (as opposed to slowing down the wheels to directly counter such drifting).

This is because if the acceleration is the robot is correlated with the tilt of the robot. If this acceleration was reduced, the robot's tilt would become more extreme until it falls over. On the other hand, further accelerating the robot (hopefully) rebalances the orientation of the robot and reduces its velocity in the long run.

The P, I, and D terms are plugged into a conventional PID mathematical function, and the result influences the acceleration of the motors.

At times, the robot would still occasionally accelerate uncontrollably in one direction (due to minute but consistent calibration errors building up and cascading). To prevent this from happening, I've employed a temporary fix.

If the power applied to the motors exceeds a certain threshold (this usually happens when the robot is leaning copiously in one direction), the motors suddenly burst up to max power in that direction. This usually pushes the robot to the opposite of its previous slant, rebalancing the robot.

# Minor notes

I've noticed that the bot balances much better on carpet than on hardwood floors. Unless your carpet is rather unruly, it's almost certainly a good idea to begin any tuning of the controller on carpet.
