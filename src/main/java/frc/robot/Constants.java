// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot;


public final class Constants {
  
  public static class DriveConstants{
    // Setting all the motors to an integer
    public static final int FRONT_LEFT_MOTOR = 1;
    public static final int FRONT_RIGHT_MOTOR = 2;
    public static final int BACK_LEFT_MOTOR = 3;
    public static final int BACK_RIGHT_MOTOR = 4;
  }
  
  public static class OperatorConstants {
    // Setting the controller port
    public static final int kDriverControllerPort = 0;
  }   

  public static class shooterConstants{
    public static final int SHOOTER = 5;
    public static final double GEAR_RATIO = 1.0;
    public static final double POS_CONVERT = (2.0*Math.PI)/GEAR_RATIO;
    public static final double VELOCITY_CONVERT = POS_CONVERT/GEAR_RATIO;
    public static final int SHOOTERINTAKE = 6;
  }
  
  public static class intakeConstants {
    public static final int INTAKE_ID = 5;
    public static final int FEEDER_ID = 6;
  }
}
