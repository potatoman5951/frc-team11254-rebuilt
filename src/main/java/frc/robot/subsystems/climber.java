// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import static frc.robot.Constants.climbconstants.*;

public class climber extends SubsystemBase {
  private SparkMax climbMotorRight;
  private SparkMax climbMotorLeft;
  private SparkMaxConfig climbMotorRightConfig;
  private SparkMaxConfig climbMotorLeftConfig;
  private SparkClosedLoopController sparkControl;
  private RelativeEncoder climbEncoder;
  private SparkClosedLoopController climbPID;
  /** Creates a new climber. */
  public climber() {
    climbMotorRight = new SparkMax(RIGHTCLIMB, MotorType.kBrushed);
    climbMotorLeft = new SparkMax(LEFTCLIMB, MotorType.kBrushed);

    climbMotorLeftConfig = new SparkMaxConfig();
    climbMotorRightConfig = new SparkMaxConfig();
    climbPID = climbMotorRight.getClosedLoopController();

    climbEncoder = climbMotorRight.getEncoder();

    climbMotorLeftConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(60)
      .inverted(true)
      .follow(climbMotorRight);
    climbMotorRightConfig
      .idleMode(IdleMode.kBrake)
      .smartCurrentLimit(60);
    climbMotorRightConfig.closedLoop.pid(1,0,0).outputRange(-1, 1).allowedClosedLoopError(.1, ClosedLoopSlot.kSlot0);//range for output still needs to be found
    climbMotorRightConfig.encoder.positionConversionFactor(CLIMBERPOS);

    climbMotorRight.configure(climbMotorRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    climbMotorLeft.configure(climbMotorLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void climbPosition(double rightposition){
    sparkControl.setSetpoint(rightposition, ControlType.kPosition);
  }
  double getClimbPosition(){
    return climbEncoder.getPosition();
  }
  public void cliberSpeed(double climbSpeed){
    climbMotorRight.set(climbSpeed);
  }
  public void climbup(){
    climbPID.setSetpoint(CLIMBERUP, ControlType.kPosition);
  }
  public void climbdown(){
    climbPID.setSetpoint(CLIMBDOWN, ControlType.kPosition);
  }

  public boolean atPosition(double position) {
    return Math.abs(climbEncoder.getPosition() - position) <= 0.1;
  }

  public Command climbUp() {
    return Commands.run(() -> {climbMotorRight.set(-0.05);}, this);
  }

  public Command climbDown() {
    return Commands.run(() -> {climbMotorRight.set(0.2);}, this).until(() -> atPosition(CLIMBDOWN));
  }


  public void initSendable(SendableBuilder builder){
    super.initSendable(builder);
    builder.setSmartDashboardType("Climber");
    builder.addDoubleProperty("climbergoal", () -> climbPID.getSetpoint(), null);
    builder.addDoubleProperty("climberpos", ()-> getClimbPosition(), null);
    builder.addDoubleProperty("Climber Voltage", () -> climbMotorRight.getAppliedOutput() * climbMotorRight.getBusVoltage(), null);
    builder.addBooleanProperty("At Top", () -> atPosition(CLIMBERUP), null);
    builder.addBooleanProperty("At Bottom", () -> atPosition(CLIMBDOWN), null);
  }
}//redo code based on elavator removing the funlle code
