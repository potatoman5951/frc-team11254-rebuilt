// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;
//Imports(figure it out Einstein)
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import static frc.robot.Constants.shooterConstants.*;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkMaxConfig;


public class Shooter extends SubsystemBase {
  //define this
  private SparkMax motorShooter;
  private SparkMaxConfig motorShooterConfig;
  private SparkClosedLoopController sparkControl;
  private RelativeEncoder encoder;
  /**builds the motor and defines its limits as well as type (/°W^)
   * PID is set up here it will modify running speed of the motor
   * configs designated here
   */
  public Shooter() {
    motorShooter = new SparkMax(SHOOTER, MotorType.kBrushless);
    motorShooterConfig = new SparkMaxConfig();
    sparkControl = motorShooter.getClosedLoopController();
    encoder = motorShooter.getEncoder();
//PID woot! woot!
    motorShooterConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(60)
      .closedLoop.positionWrappingEnabled(true)
      .pid(.005, 0, 0);
    motorShooterConfig.encoder.velocityConversionFactor(VELOCITY_CONVERT);
//config
    motorShooter.configure(motorShooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }
/**WE NEED TO PUT A STOP TO THIS.(/°@^)
 * creates the stop command to prevent continuou running
 */
public void stop(){
  motorShooter.stopMotor();
}
/**This command is largely empty
 * placed to prevent an ERROR (/°W^)
 */
public void spinShoot(){
  motorShooter.set(.4);
}
/**will be pulled from when firing
 * 
 */
public void PIDShoot(double fireSpeed){
  sparkControl.setSetpoint(fireSpeed, ControlType.kVelocity);
}
  @Override
  public void periodic() {
  }
}
