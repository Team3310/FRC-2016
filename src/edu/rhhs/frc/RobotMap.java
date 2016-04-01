package edu.rhhs.frc;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap
{
	// USB Port IDs
	public static final int DRIVER_JOYSTICK_1_USB_ID = 0;
	public static final int DRIVER_JOYSTICK_2_USB_ID = 1;
	public static final int OPERATOR_XBOX_USB_ID = 2;
	
	// MOTORS (Software CAN IDs are the PDP ID + 2)
	public static final int DRIVETRAIN_LEFT_MOTOR1_CAN_ID = 3;
	public static final int DRIVETRAIN_LEFT_MOTOR2_CAN_ID = 2;
	public static final int DRIVETRAIN_LEFT_MOTOR3_CAN_ID = 1;
	public static final int DRIVETRAIN_RIGHT_MOTOR1_CAN_ID = 12;
	public static final int DRIVETRAIN_RIGHT_MOTOR2_CAN_ID = 13;
	public static final int DRIVETRAIN_RIGHT_MOTOR3_CAN_ID = 14;
	
	public static final int INTAKE_OUTER_ROLLER_MOTOR_CAN_ID = 11;
	public static final int INTAKE_INNER_ROLLER_MOTOR_CAN_ID = 4;
	
	public static final int SHOOTER_WINCH_MOTOR_CAN_ID = 0;
	
	public static final int MANIPULATOR_LEFT_MOTOR_CAN_ID = 7;
	public static final int MANIPULATOR_RIGHT_MOTOR_CAN_ID = 8;
	
	// PNEUMATICS
	public static final int DRIVETRAIN_SPEEDSHIFT_PCM_ID = 1; 
	
    public static final int CARRIAGE_RELEASE_PCM_ID = 0; 
	public static final int SHOOTER_POSITION_PCM_ID = 2; 
    
    public static final int INTAKE_OUTER_LIFT_PCM_ID = 4; 
    public static final int INTAKE_INNER_LIFT_PCM_ID = 3; 
    
	public static final int DRIVETRAIN_CLIMBER_DEPLOY_PCM_ID = 5; 
	public static final int DRIVETRAIN_CLIMBER_RETRACT_PCM_ID = 6; 

	// DIGITAL INPUTS
    public static final int CARRIAGE_RETRACTED_DIO_PORT_ID = 0;
    public static final int CDF_SENSOR_DIO_PORT_ID = 3;
    public static final int CALIBRATE_GYRO_BUTTON_DIO_PORT_ID = 2;
    public static final int LASER_SENSOR_DIO_PORT_ID = 1;
}