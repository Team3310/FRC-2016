package edu.rhhs.frc;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{
	public static final int DRIVETRAIN_LEFT_MOTOR1 = 1;
	public static final int DRIVETRAIN_LEFT_MOTOR2 = 2;
	public static final int DRIVETRAIN_LEFT_MOTOR3 = 3;
	public static final int DRIVETRAIN_RIGHT_MOTOR1 = 4;
	public static final int DRIVETRAIN_RIGHT_MOTOR2 = 5;
	public static final int DRIVETRAIN_RIGHT_MOTOR3 = 6;
	
	public static final int INTAKE_OUTER_ROLLER_MOTOR_ID = 7;
	public static final int INTAKE_INNER_ROLLER_MOTOR_ID = 8;
	
	public static final int SHOOTER_WINCH_MOTOR_ID = 9;
	
	public static final int MANIPULATOR_LEFT_MOTOR_ID = 10;
	public static final int MANIPULATOR_RIGHT_MOTOR_ID = 11;
	
	//PNEUMATICS
	public static final int SHOOTER_POSITION_MODULE_ID = 6;
    public static final int CARRIAGE_RELEASE_MODULE_ID = 7;
    
    public static final int INTAKE_OUTER_LIFT_MODULE_ID = 8;
    public static final int INTAKE_INNER_LIFT_MODULE_ID = 9;
}