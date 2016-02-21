package edu.rhhs.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{
	//MOTORS
	public static final int DRIVETRAIN_LEFT_MOTOR1 = 3;
	public static final int DRIVETRAIN_LEFT_MOTOR2 = 4;
	public static final int DRIVETRAIN_LEFT_MOTOR3 = 5;
	public static final int DRIVETRAIN_RIGHT_MOTOR1 = 16;
	public static final int DRIVETRAIN_RIGHT_MOTOR2 = 15;
	public static final int DRIVETRAIN_RIGHT_MOTOR3 = 14;
	
	public static final int INTAKE_OUTER_ROLLER_MOTOR_ID = 9;
	public static final int INTAKE_INNER_ROLLER_MOTOR_ID = 10;
	
	public static final int SHOOTER_WINCH_MOTOR_ID = 8;
	
	public static final int MANIPULATOR_LEFT_MOTOR_ID = 2;
	public static final int MANIPULATOR_RIGHT_MOTOR_ID = 17;
	
	//PNEUMATICS
	public static final int DRIVETRAIN_SPEEDSHIFT_MODULE_ID = 0;
	public static final int DRIVETRAIN_WINCH_ENGAGE_MODULE_ID = 5;
	public static final int DRIVETRAIN_WINCH_DISENGAGE_MODULE_ID = 6;
	
	public static final int SHOOTER_POSITION_MODULE_ID = 2;
    public static final int CARRIAGE_RELEASE_MODULE_ID = 1;
    
    public static final int INTAKE_OUTER_LIFT_MODULE_ID = 3;
    public static final int INTAKE_INNER_LIFT_MODULE_ID = 4;
    
    //DIGITAL INPUTS
    public static final int CARRIAGE_RETRACTED1_PORT_ID = 1;
    public static final int CARRIAGE_RETRACTED2_PORT_ID = 2;
}