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

package frc.robot.Subsystems.drive;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;
// import org.ironmaple.simulation.drivesims.COTS;
// import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;
// import org.ironmaple.simulation.drivesims.configs.SwerveModuleSimulationConfig;

public class DriveConstants {
  public static final double maxSpeedMetersPerSec = Units.feetToMeters(16);
  public static final double odometryFrequency = 100.0; // Hz
  public static final double trackWidth = Units.inchesToMeters(27);
  public static final double wheelBase = Units.inchesToMeters(27);
  public static final double driveBaseRadius = Math.hypot(trackWidth / 2.0, wheelBase / 2.0);
  public static final Translation2d[] moduleTranslations =
      new Translation2d[] {
        new Translation2d(trackWidth / 2.0, wheelBase / 2.0),
        new Translation2d(trackWidth / 2.0, -wheelBase / 2.0),
        new Translation2d(-trackWidth / 2.0, wheelBase / 2.0),
        new Translation2d(-trackWidth / 2.0, -wheelBase / 2.0)
      };


  // TODO: Zeroed rotation values for each module, see setup instructions
  public static final Rotation2d frontLeftZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d frontRightZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d backLeftZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d backRightZeroRotation = new Rotation2d(0.0);

  // Device CAN IDs
  public static final int pigeonCanId = 11;

  public static final int frontLeftDriveCanId = 8;
  public static final int backLeftDriveCanId = 6;
  public static final int frontRightDriveCanId = 3;
  public static final int backRightDriveCanId = 5;

  public static final int frontLeftTurnCanId = 9;
  public static final int backLeftTurnCanId = 7;
  public static final int frontRightTurnCanId = 2;
  public static final int backRightTurnCanId = 4;

  // Drive motor configuration
  public static final int driveMotorCurrentLimit = 40;
  public static final double wheelRadiusMeters = Units.inchesToMeters(2);
  public static final double driveMotorReduction = 6.75; // SDS Mk4i L2
  public static final DCMotor driveGearbox = DCMotor.getNEO(1);

  // Drive encoder configration
  public static final double driveEncoderPositionFactor =
      // rot -> Rad -> wheel rad; 2pi / gear ratio
      (2 * Math.PI) / 6.75; // Rotor Rotations -> Wheel Radians
  public static final double driveEncoderVelocityFactor =
      // rot -> Rad -> wheel rad; 2pi / gear ratio / seconds
      (2 * Math.PI) / 6.75 / 60.0; // Rotor RPM -> Wheel Rad/Sec

  // Drive PID configuration
  public static final double driveKp = 0.0;
  public static final double driveKd = 0.0;
  public static final double driveKs = 0.0;
  public static final double driveKv = 0.16;
  public static final double driveSimP = 1.05;
  public static final double driveSimD = 0.0;
  public static final double driveSimKs = 0.0;
  public static final double driveSimKv = 0.789;

  // Turn motor configuration
  public static final boolean turnInverted = true;
  public static final int turnMotorCurrentLimit = 30;
  public static final double turnMotorReduction = 150.0 / 7; // SDS Mk4i steer reduction
  public static final DCMotor turnGearbox = DCMotor.getNEO(1);

  // Turn encoder configuration
  public static final boolean turnEncoderInverted = true;
  public static final double turnEncoderPositionFactor =
      2 * Math.PI / turnMotorReduction; // Rotations -> WHEEL Radians
  public static final double turnEncoderVelocityFactor =
      (2 * Math.PI) / 60.0 / turnMotorReduction; // RPM -> WHEEL Rad/Sec

  // Turn PID configuration
  public static final double turnKp = 0.8;
  public static final double turnKd = 0;
  public static final double turnSimP = 0.01;
  public static final double turnSimD = 0.0;
  public static final double turnPIDMinInput = 0; // Radians
  public static final double turnPIDMaxInput = 2 * Math.PI; // Radians

  // PathPlanner configuration
  public static final double robotMassKg =
      Units.lbsToKilograms(115 + 19 + 5); // dry weight + bumper + battery
  public static final double robotMOI = 6.883;
  public static final double wheelCOF = 1.1; // COF for sds billet
  public static final RobotConfig ppConfig =
      new RobotConfig(
          robotMassKg,
          robotMOI,
          new ModuleConfig(
              wheelRadiusMeters,
              maxSpeedMetersPerSec,
              wheelCOF,
              driveGearbox.withReduction(driveMotorReduction),
              driveMotorCurrentLimit,
              1),
          moduleTranslations);

  // Path On The Fly Constraints
  public static PathConstraints kTeleopPathConstraints =
      new PathConstraints(
          Units.feetToMeters(15 / 4),
          4.3 / 4,
          Units.degreesToRadians(540 / 4),
          Units.degreesToRadians(720 / 4));
  // All divided by four in case

  public static PathConstraints kAutoPathConstraints =
      new PathConstraints(
          Units.feetToMeters(6), 1.0, Units.degreesToRadians(540), Units.degreesToRadians(540));

  // AUTO Align
  public static Time kEndTriggerDebounce = Time.ofBaseUnits(1, Seconds);

  public static Time kTeleopAlignAdjustTimeout = Time.ofBaseUnits(10, Seconds);
  public static Time kAutoAlignAdjustTimeout = Time.ofBaseUnits(3, Seconds);

  public static Distance kPositionTolerance = Distance.ofBaseUnits(1, Inches);
  public static Distance kSpeedTolerance = Distance.ofBaseUnits(1, Inches);

  public static Rotation2d kRotationTolerance = Rotation2d.fromDegrees(1);

//   public static DriveTrainSimulationConfig mapleSimConfig =
//       DriveTrainSimulationConfig.Default()
//           // Specify gyro type (for realistic gyro drifting and error simulation)
//           .withGyro(COTS.ofPigeon2())
//           // Specify swerve module (for realistic swerve dynamics)
//           .withSwerveModule(
//               new SwerveModuleSimulationConfig(
//                   DCMotor.getNEO(1), // Drive motor is a NEO
//                   DCMotor.getNEO(1), // Steer motor is a NEO
//                   driveMotorReduction, // Drive motor gear ratio.
//                   turnMotorReduction, // Steer motor gear ratio.
//                   Voltage.ofBaseUnits(0.1, Volts), // Drive friction voltage.
//                   Voltage.ofBaseUnits(0.1, Volts), // Steer friction voltage
//                   Inches.of(2), // Wheel radius
//                   KilogramSquareMeters.of(0.03), // Steer MOI
//                   1.2)) // Wheel COF
//           // Configures the track length and track width (spacing between swerve modules)
//           .withTrackLengthTrackWidth(Inches.of(24), Inches.of(24))
//           // Configures the bumper size (dimensions of the robot bumper)
//           .withBumperSize(Inches.of(30), Inches.of(30));
  public static Pose2d startingPose = new Pose2d(3, 3, new Rotation2d());
}
