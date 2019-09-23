/*
 * Copyright (c) 2015 Titan Robotics Club (http://www.titanrobotics.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import ftclib.FtcGamepad;
import ftclib.FtcOpMode;
import hallib.HalDashboard;
import trclib.TrcGameController;
import trclib.TrcRobot;

@TeleOp(name="TeleOp: PID Elevator", group="FtcTeleOpSamples")
@Disabled
public class FtcTeleOpPidElevator extends FtcOpMode implements TrcGameController.ButtonHandler
{
    private HalDashboard dashboard;
    //
    // Gamepad.
    //
    private FtcGamepad gamepad;
    //
    // Elevator.
    //
    private Elevator elevator;

    //
    // Implements FtcOpMode abstract methods.
    //

    @Override
    public void initRobot()
    {
        hardwareMap.logDevices();
        dashboard = HalDashboard.getInstance();
        //
        // Initializing Gamepad.
        //
        gamepad = new FtcGamepad("gamepad", gamepad1, this);
        gamepad.setYInverted(true);
        //
        // Elevator subsystem.
        //
        elevator = new Elevator();
        elevator.actuator.zeroCalibrate();
    }   //initRobot

    //
    // Overrides TrcRobot.RobotMode methods.
    //

    @Override
    public void startMode(TrcRobot.RunMode prevMode, TrcRobot.RunMode nextMode)
    {
        dashboard.clearDisplay();
    }   //startMode

    @Override
    public void runPeriodic(double elapsedTime)
    {
        //
        // Elevator subsystem.
        //
        double elevatorPower = gamepad.getRightStickY(true);
        elevator.actuator.setPower(elevatorPower);
        dashboard.displayPrintf(1, "Elevator:power=%.2f,height=%.2f",
                                elevatorPower, elevator.actuator.getPosition());
    }   //runPeriodic

    //
    // Implements TrcGameController.ButtonHandler interface.
    //

    @Override
    public void buttonEvent(TrcGameController gamepad, int button, boolean pressed)
    {
        if (gamepad == this.gamepad)
        {
            switch (button)
            {
                case FtcGamepad.GAMEPAD_RBUMPER:
                    elevator.actuator.setManualOverride(pressed);
                    break;

                case FtcGamepad.GAMEPAD_START:
                    if (pressed)
                    {
                        elevator.actuator.zeroCalibrate();
                    }
                    break;
            }
        }
    }   //buttonEvent

}   //class FtcTeleOpPidElevator
