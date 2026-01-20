// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.intakeConstants;
import frc.robot.Constants.intakeConstants.*;


public class Intake extends SubsystemBase {
  private SparkMax intake;
  private SparkMaxConfig intakeConfig;
  /** Creates a new Intake. */
  public Intake() { 
    intake = new SparkMax(intakeConstants.INTAKE_ID, MotorType.kBrushless);
    intakeConfig = new SparkMaxConfig();
    intakeConfig.idleMode(IdleMode.kBrake);
    intake.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }
  /** spins the intake */
  public void spinIntake(double spinSpeed){
    intake.set(spinSpeed);
  }
  /** spins the intake with joystick */
  public Command intakeWithJoystick(double speed){
    return Commands.run(() ->  {intake.set(speed);}, this);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
