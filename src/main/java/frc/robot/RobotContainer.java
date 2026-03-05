// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import org.littletonrobotics.junction.Logger;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.KeyPoses;
import frc.robot.Constants.Mode;
import frc.robot.Constants.systems;
import frc.robot.Subsystems.Vision;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.IntakeIO;
import frc.robot.Subsystems.Intake.IntakeReal;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.Subsystems.drive.GyroIOPigeon2;
import frc.robot.Subsystems.drive.ModuleIOSpark;
import frc.robot.Subsystems.hopper.hopper;
import frc.robot.Subsystems.hopper.hopperIO;
import frc.robot.Subsystems.hopper.hopperReal;
import frc.robot.commands.DriveCommands;
import frc.robot.util.Aim;
import frc.robot.util.DriveTo;

public class RobotContainer {
  private CommandXboxController driver = new CommandXboxController(0);
  private CommandXboxController operater = new CommandXboxController(1);


  private final SendableChooser<Command> autoChooser;
  private final Drive drive;
  @SuppressWarnings("unused")
  private final Vision vision;


  private Intake intake;
  private hopper hopper;

  private Aim autoAim;
  private DriveTo autoDrive;

  public RobotContainer() {
    drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOSpark(0),
                new ModuleIOSpark(1),
                new ModuleIOSpark(2),
                new ModuleIOSpark(3));

    vision = 
      new Vision(drive::addVisionMeasurement);

      // Setup for new programmers
      if (Constants.currentMode == Mode.SIM){}

      // Select Subsystems
      if (systems.intake) {
        intake = new Intake(new IntakeReal());
      } else {
        intake = new Intake(new IntakeIO() {});
      }
      if (systems.hopper) {
        hopper = new hopper(new hopperReal());
      } else {
        hopper = new hopper(new hopperIO(){});
      }

      this.autoAim = new Aim(drive, AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded));
      this.autoDrive = new DriveTo(drive,AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded));

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser",autoChooser);
    configureBindings();

    // Not working :)
    NamedCommands.registerCommand("RunIntake", intake.runAuto());


  }

  private void configureBindings() {

      drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -driver.getLeftY(),
            () -> -driver.getLeftX(),
            () -> driver.getRightX()));

            //RESET GYRO
            driver.b().onTrue(Commands.runOnce(()->drive.resetGyro(0), drive));


//INTAKE CONTROLS
    driver.rightTrigger().whileTrue(intake.run());
        driver.leftTrigger().whileTrue(intake.out());

//HOPPER CONTROLS
    operater.rightBumper().whileTrue(hopper.down());
        operater.leftBumper().whileTrue(hopper.up());

// AUTOMATION

// Aim at Hub
    driver.y().whileTrue(
      autoAim.at("TOWARD 0,0",
            () -> -driver.getLeftY(),
            () -> -driver.getLeftX()));

  // Drive to Shooting positions
    driver.rightBumper().whileTrue(
      autoDrive.generateCommand(
         KeyPoses.RightShoot));

    driver.leftBumper().whileTrue(
      autoDrive.generateCommand(KeyPoses.LeftShoot));

  }

  public Command getAutonomousCommand() {
    Logger.recordOutput("Auto Chosen", autoChooser.getSelected().getName());
    return autoChooser.getSelected();
  }
}
