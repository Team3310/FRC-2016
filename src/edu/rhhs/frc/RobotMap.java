package edu.rhhs.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{
	// MOTORS (Software CAN IDs are the PDP ID + 2)
	public static final int DRIVETRAIN_LEFT_MOTOR1_CAN_ID = 3;
	public static final int DRIVETRAIN_LEFT_MOTOR2_CAN_ID = 4;
	public static final int DRIVETRAIN_LEFT_MOTOR3_CAN_ID = 5;
	public static final int DRIVETRAIN_RIGHT_MOTOR1_CAN_ID = 16;
	public static final int DRIVETRAIN_RIGHT_MOTOR2_CAN_ID = 15;
	public static final int DRIVETRAIN_RIGHT_MOTOR3_CAN_ID = 14;
	
	public static final int INTAKE_OUTER_ROLLER_MOTOR_CAN_ID = 9;
	public static final int INTAKE_INNER_ROLLER_MOTOR_CAN_ID = 10;
	
	public static final int SHOOTER_WINCH_MOTOR_CAN_ID = 8;
	
	public static final int MANIPULATOR_LEFT_MOTOR_CAN_ID = 2;
	public static final int MANIPULATOR_RIGHT_MOTOR_CAN_ID = 17;
	
	// PNEUMATICS
	public static final int DRIVETRAIN_SPEEDSHIFT_PCM_ID = 1; //0
	
    public static final int CARRIAGE_RELEASE_PCM_ID = 0; //1
	public static final int SHOOTER_POSITION_PCM_ID = 2; //2
    
    public static final int INTAKE_OUTER_LIFT_PCM_ID = 4; //3
    public static final int INTAKE_INNER_LIFT_PCM_ID = 3; //4
    
	public static final int DRIVETRAIN_WINCH_ENGAGE_PCM_ID = 5; //5
	public static final int DRIVETRAIN_WINCH_DISENGAGE_PCM_ID = 6; //6

	// DIGITAL INPUTS
    public static final int CARRIAGE_RETRACTED_DIO_PORT_ID = 0;
}