// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.Constants.intakeConstants;
import frc.robot.Constants.intakeConstants.*;

import static edu.wpi.first.units.Units.Volts;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;
import static frc.robot.Constants.shooterConstants.*;


public class Intake extends SubsystemBase {
  private SparkMax intake;
  private SparkMaxConfig intakeConfig;
  private SparkMax feeder;
  private SparkMaxConfig feederConfig;
  private SparkClosedLoopController sparkControl;
  private RelativeEncoder encoder;
  private SysIdRoutine shooterSysID;

  /** Creates a new Intake. */
  public Intake() { 
    intake = new SparkMax(intakeConstants.INTAKE_ID, MotorType.kBrushless);
    feeder = new SparkMax(intakeConstants.FEEDER_ID, MotorType.kBrushed);

    intakeConfig = new SparkMaxConfig();
    feederConfig = new SparkMaxConfig();
    
    sparkControl = intake.getClosedLoopController();
    encoder = intake.getEncoder();
    
    feederConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(60);
    
//PID woot! woot!
    intakeConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(60)
      .closedLoop.positionWrappingEnabled(true)
      .pid(.0005, 0, 0);
    
    intakeConfig.encoder.velocityConversionFactor(VELOCITY_CONVERT);
    
//config
    intake.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    feeder.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    shooterSysID = new SysIdRoutine(
      new SysIdRoutine.Config(
        Volts.of(1).per(Second),
        Volts.of(3),
        Seconds.of(10)
      ), 
      new SysIdRoutine.Mechanism(
        (volts) -> intake.setVoltage(volts.in(Volts)), 
        null,
        this));
  }

  /** spins the intake */
  public void spinIntake(double spinSpeed, double feederSpeed){
    intake.set(spinSpeed);
    feeder.set(feederSpeed);
  }

  /** spins the intake with joystick */
  public Command intakeWithJoystick(double speed){
    return Commands.run(() ->  {intake.set(speed);}, this);
  }

  /**WE NEED TO PUT A STOP TO THIS.(/°@^)
 * creates the stop command to prevent continuou running
 */
  public void stop(){
    intake.stopMotor();
    feeder.stopMotor();
  }

  /**This command is largely empty
   * placed to prevent an ERROR (/°W^)
   */
  public void spinShoot(){
    intake.set(.4);
  }

  /**will be pulled from when firing
   * 
   */
  public void PIDShoot(double fireSpeed){
    feeder.set(.6);
    intake.set(0.8);
    ///sparkControl.setSetpoint(fireSpeed, ControlType.kVelocity);
  }

  public void intakeMotorShooter(){
    feeder.set(.4);
  }

  public Command runSysID(){
    return Commands.sequence(
      shooterSysID.quasistatic(Direction.kForward).withTimeout(2),
      shooterSysID.quasistatic(Direction.kReverse).withTimeout(2),
      shooterSysID.dynamic(Direction.kForward).withTimeout(2),
      shooterSysID.dynamic(Direction.kReverse).withTimeout(2));
  }

  public double getPosition(){
    return encoder.getPosition();
  }

  public double getVelocity(){
    return encoder.getVelocity();
  }

  public double getVoltage(){
    return intake.getAppliedOutput()*intake.getBusVoltage();
  }

  public void intakeMotorShooterStop(){
    feeder.stopMotor();
  }

  public void intakeWithPID(double speed){
    sparkControl.setSetpoint(speed, ControlType.kVelocity);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void intitSendable(SendableBuilder builder){
    super.initSendable(builder);
    builder.addDoubleProperty("shooter velocity", () -> encoder.getVelocity(), null);
  }
}
