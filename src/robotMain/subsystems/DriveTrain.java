// RobotBuilder Version: 1.5
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package robotMain.subsystems;

import robotMain.Robot;
import robotMain.RobotMap;
import robotMain.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class DriveTrain extends Subsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    SpeedController leftMotor1 = RobotMap.driveTrainleftMotor1;
    SpeedController leftMotor2 = RobotMap.driveTrainleftMotor2;
    SpeedController leftMotor3 = RobotMap.driveTrainleftMotor3;
    SpeedController rightMotor4 = RobotMap.driveTrainrightMotor4;
    SpeedController rightMotor5 = RobotMap.driveTrainrightMotor5;
    SpeedController rightMotor6 = RobotMap.driveTrainrightMotor6;
    DoubleSolenoid shifter = RobotMap.driveTrainshifter;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    private final double cubicConstant = Robot.cubicScale;
    private final double deadZone = Robot.deadZone;
    private byte leftSyncGroup = 1;
    private byte rightSyncGroup = 0;
    
    // All of the following are purely for testing information and mostly copies of values to be printed and not modified
    private double testLeftY;
    private double testRightY;
    private double testCubedLeftY;
    private double testCubedRightY;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new Drive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    // Constructs DriveTrain
    public DriveTrain(){
    	shifter.set(Value.kOff); // Initializes in low gear
    }
    
    /***************************************
     * 
     * Method to scale a joystick value.
     *    @param arg is the joystick value
     *
     ***************************************/
    private double cubicScale(double arg){
    	return (cubicConstant*arg + (1 - cubicConstant) * Math.pow((double)arg, 3));
    }
    
    /*************************************************
     * 
     * Method to move the robot. Uses a tank drive
     * style.
     * 	  @param leftY is the left joystick Y axis
     *    @param rightY is the right joystick Y axis
     *    
     *************************************************/
    public void move(double leftY, double rightY){
    	
    	leftY *= Robot.scale;
    	rightY *= Robot.scale;
    	
    	//testing stuff
    	testLeftY = leftY;
    	testCubedLeftY = cubicScale(leftY);
    	testRightY = rightY;
    	testCubedRightY = cubicScale(rightY);
    	
    	// dead zone check and set
    	if(leftY <= deadZone && leftY >= -deadZone) leftY = 0.0;
    	if(rightY <= deadZone && rightY >= -deadZone) rightY = 0.0;
    	
    	setMotors(side.LEFT, cubicScale(leftY));
    	setMotors(side.RIGHT, cubicScale(rightY));
    }
    
    private enum side { LEFT, RIGHT }; // Sides of robot
    
    /***********************************************
     * 
     * Method to set all of one side with a value.
     * @param side which side LEFT or RIGHT
     * @param val is the x value from the joystick
     ***********************************************/
    public void setMotors(side side, double val){
    	switch(side){
    	 case LEFT:
    		 if(Robot.invertLeft) val *= -1; // Inverts the direction by making it negative
    		 leftMotor3.set(val, leftSyncGroup);
    		 leftMotor2.set(val, leftSyncGroup);
    		 leftMotor1.set(val, leftSyncGroup);
    	 break;
    	 case RIGHT:
    		 if(Robot.invertRight) val *= -1; // Inverts the direction by making it negative
    		 rightMotor4.set(val, rightSyncGroup);
    		 rightMotor5.set(val, rightSyncGroup);
    		 rightMotor6.set(val, rightSyncGroup);
    	 break;
    	}
    }
    
    // Method to toggle shifting between high and low gear
    public void shiftToggle(){
    	// the case of the shifter being kOff should not occur, but accounting for it to be kOff so the robot will still shift
    	if(shifter.get() == Value.kReverse || shifter.get() == Value.kForward){
    		shifter.set(Value.kOff);
    	}
    	else{
    		shifter.set(Value.kReverse);
    	}
    }
    
    public void testSolenoid(int val) {
    	switch(val) {
    	case 0:
    		shifter.set(Value.kOff);
    	break;
    	case 1:
    		shifter.set(Value.kForward);
    	break;
    	case 2:
    		shifter.set(Value.kReverse);
    	break;
    	default: 
    	}
    }
    
    // Uses test values and current values to print information to the dashboard while testing
    public void printTestInfo(){
    	// Joystick values
    	SmartDashboard.putNumber("left joystick Y: ", testLeftY);
    	SmartDashboard.putNumber("right joystick Y: ", testRightY);
    	SmartDashboard.putNumber("left joystick cubed Y: ", testCubedLeftY);
    	SmartDashboard.putNumber("right joystick cubed Y: ", testCubedRightY);
    	
    	// Motor values
    	SmartDashboard.putNumber("current leftMotor1 val: ", leftMotor1.get());
    	SmartDashboard.putNumber("current leftMotor2 val: ", leftMotor2.get());
    	SmartDashboard.putNumber("current leftMotor3 val: ", leftMotor3.get());
    	SmartDashboard.putNumber("current rightMotor4 val: ", rightMotor4.get());
    	SmartDashboard.putNumber("current rightMotor5 val: ", rightMotor5.get());
    	SmartDashboard.putNumber("current rightMotor6 val: ", rightMotor6.get());
    	
    	//Shifter
    	if(shifter.get() == Value.kReverse) SmartDashboard.putString("current gear: ", "HIGH");
    	else if(shifter.get() == Value.kOff) SmartDashboard.putString("current gear: ", "LOW");
    	else SmartDashboard.putString("current gear: ", "LOW");
    }
    
}

