// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.SimInstance;

import org.ironmaple.simulation.drivesims.SwerveModuleSimulation;
import org.ironmaple.simulation.motorsims.SimulatedMotorController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.DriveConstants;
import frc.robot.InputOutput.ModuleIO;

import static frc.robot.DriveConstants.*;

import java.util.Queue;

import frc.robot.Systems.SparkOdometryThread;

import static edu.wpi.first.units.Units.*;

/** Physics sim implementation of module IO. */
public class ModuleSim implements ModuleIO {
  // reference to module simulation
  private final SwerveModuleSimulation moduleSimulation;
  // reference to the simulated drive motor
  private final SimulatedMotorController.GenericMotorController driveMotor;
  // reference to the simulated turn motor
  private final SimulatedMotorController.GenericMotorController turnMotor;
  private boolean driveClosedLoop = false;
  private boolean turnClosedLoop = false;
  private PIDController driveController = new PIDController(driveSimP, 0, driveSimD);
  private PIDController turnController = new PIDController(turnSimP, 0, turnSimD);
  private double driveFFVolts = 0.0;
  private double driveAppliedVolts = 0.0;
  private double turnAppliedVolts = 0.0;

    // Queue inputs from odometry thread
  private final Queue<Double> timestampQueue;
  private final Queue<Double> drivePositionQueue;
  private final Queue<Double> turnPositionQueue;

  public ModuleSim(SwerveModuleSimulation moduleSimulation) {
    this.moduleSimulation = moduleSimulation;
    // configures a generic motor controller for drive motor
    // set a current limit of 60 amps
    this.driveMotor =
        moduleSimulation
            .useGenericMotorControllerForDrive()
            .withCurrentLimit(Amps.of(DriveConstants.driveMotorCurrentLimit));
    this.turnMotor =
        moduleSimulation
            .useGenericControllerForSteer()
            .withCurrentLimit(Amps.of(DriveConstants.turnMotorCurrentLimit));

    // Enable wrapping for turn PID
    turnController.enableContinuousInput(-Math.PI, Math.PI);

    // Create odometry queues
    timestampQueue = SparkOdometryThread.getInstance().makeTimestampQueue();
    drivePositionQueue =
        SparkOdometryThread.getInstance().registerSignal(()->moduleSimulation.getDriveWheelFinalPosition().in(Radians));
    turnPositionQueue =
        SparkOdometryThread.getInstance().registerSignal(()->moduleSimulation.getSteerAbsoluteAngle().in(Radians));
  }

  @Override
  public void updateInputs(ModuleIOInputs inputs) {
    // Run closed-loop control
    if (driveClosedLoop) {
      driveAppliedVolts =
          driveFFVolts
              + driveController.calculate(
                  moduleSimulation.getDriveWheelFinalSpeed().in(RadiansPerSecond));
    } else {
      driveController.reset();
    }
    if (turnClosedLoop) {
      turnAppliedVolts =
          turnController.calculate(
              moduleSimulation
                  .getSteerRelativeEncoderPosition()
                  .divide(DriveConstants.turnMotorReduction)
                  .in(Radian));
    } else {
      turnController.reset();
    }

    // Update simulation state

    this.driveMotor.requestVoltage(
        Voltage.ofBaseUnits(MathUtil.clamp(driveAppliedVolts, -12.0, 12.0), Volts));
    this.turnMotor.requestVoltage(
        Voltage.ofBaseUnits(MathUtil.clamp(turnAppliedVolts, -12.0, 12.0), Volts));

    // Update drive inputs
    inputs.driveConnected = true;
    inputs.drivePositionRad = moduleSimulation.getDriveWheelFinalPosition().in(Radian);
    inputs.driveVelocityRadPerSec = moduleSimulation.getDriveWheelFinalSpeed().in(RadiansPerSecond);
    inputs.driveAppliedVolts = driveAppliedVolts;
    inputs.driveCurrentAmps = Math.abs(moduleSimulation.getDriveMotorSupplyCurrent().in(Amps));

    // Update turn inputs
    inputs.turnConnected = true;
    inputs.turnPosition = new Rotation2d(moduleSimulation.getSteerRelativeEncoderPosition());
    inputs.turnVelocityRadPerSec =
        moduleSimulation.getSteerRelativeEncoderVelocity().in(RadiansPerSecond);
    inputs.turnAppliedVolts = turnAppliedVolts;
    inputs.turnCurrentAmps = Math.abs(moduleSimulation.getSteerMotorSupplyCurrent().in(Amps));

 // Update odometry inputs JUST LIKE in Real IO
    inputs.odometryTimestamps =
        timestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryDrivePositionsRad =
        drivePositionQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryTurnPositions =
        turnPositionQueue.stream()
            .map((Double value) -> new Rotation2d(value))
            .toArray(Rotation2d[]::new);
    timestampQueue.clear();
    drivePositionQueue.clear();
    turnPositionQueue.clear();
  }

  @Override
  public void setDriveOpenLoop(double output) {
    driveClosedLoop = false;
    driveAppliedVolts = output;
  }

  @Override
  public void setTurnOpenLoop(double output) {
    turnClosedLoop = false;
    turnAppliedVolts = output;
  }

  @Override
  public void setDriveVelocity(double velocityRadPerSec) {
    driveClosedLoop = true;
    driveFFVolts = driveSimKs * Math.signum(velocityRadPerSec) + driveSimKv * velocityRadPerSec;
    driveController.setSetpoint(velocityRadPerSec);
  }

  @Override
  public void setTurnPosition(Rotation2d rotation) {
    turnClosedLoop = true;
    turnController.setSetpoint(rotation.getRadians());
  }
}