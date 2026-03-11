// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import org.littletonrobotics.junction.Logger;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants.Mode;
import frc.robot.Subsystems.Vision;
import frc.robot.Subsystems.Climber.Climber;
import frc.robot.Subsystems.Climber.ClimberIO;
import frc.robot.Subsystems.Climber.ClimberReal;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.IntakeIO;
import frc.robot.Subsystems.Intake.IntakeReal;
import frc.robot.Subsystems.Shooter.Shoot;
import frc.robot.Subsystems.Shooter.ShootIO;
import frc.robot.Subsystems.Shooter.ShootReal;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.Subsystems.drive.GyroIOPigeon2;
import frc.robot.Subsystems.drive.ModuleIOSpark;
import frc.robot.Subsystems.hopper.hopper;
import frc.robot.Subsystems.hopper.hopperIO;
import frc.robot.Subsystems.hopper.hopperReal;
import frc.robot.commands.DriveCommands;
import frc.robot.util.Aim;
import frc.robot.util.DriveTo;

import static frc.robot.Constants.KeyPoses.*;
import static frc.robot.Constants.systems.*;

public class RobotContainer {
  private CommandXboxController driver = new CommandXboxController(0);
  private CommandXboxController operater = new CommandXboxController(1);


  private final SendableChooser<Command> autoChooser;
  private final Drive drive;
  @SuppressWarnings("unused")
  private final Vision vision;


  private Intake intake;
  private hopper hopper;
  private Shoot shooter;
  private Climber climber;

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
      if (intakeEnabled) {
        intake = new Intake(new IntakeReal());
      } else {
        intake = new Intake(new IntakeIO() {});
      }

      if (hopperEnabled) {
        hopper = new hopper(new hopperReal());
      } else {
        hopper = new hopper(new hopperIO(){});
      }

      if (shooterEnabled) {
        shooter = new Shoot(new ShootReal());
      } else {
        shooter = new Shoot(new ShootIO() {});
      }

      if (climberEnabled) {
        climber = new Climber(new ClimberReal());
      } else {
        climber = new Climber(new ClimberIO() {});
      }

      this.autoAim = new Aim(drive, AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded));
      this.autoDrive = new DriveTo(drive,AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded));


    autoChooser = AutoBuilder.buildAutoChooser();
    //SYS ID
    autoChooser.addOption("SysID", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption("SysIDQuasistatic", drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
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

// SHOOTER CONTROLS 
    operater.rightTrigger().whileTrue(shooter.runAt(10));


// CLIMBER CONTROLS 
    operater.povDown().whileTrue(climber.climb());

// AUTOMATION

// Aim at Hub
    driver.y().whileTrue(
      autoAim.at(HubPose,
            () -> -driver.getLeftY(),
            () -> -driver.getLeftX()));

  // Drive to Shooting positions
    driver.rightBumper().whileTrue(
      autoDrive.generateCommand(
         RightShootPose));

    driver.leftBumper().whileTrue(
      autoDrive.generateCommand(LeftShootPose));

  }

  public Command getAutonomousCommand() {
    Logger.recordOutput("Auto Chosen", autoChooser.getSelected().getName());
    return autoChooser.getSelected();
  }
}
