// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import org.littletonrobotics.junction.Logger;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants.KeyPoses;
import frc.robot.Constants.Mode;
import frc.robot.Subsystems.Vision;
import frc.robot.Subsystems.Climber.Climber;
import frc.robot.Subsystems.Climber.ClimberIO;
import frc.robot.Subsystems.Climber.ClimberReal;
import frc.robot.Subsystems.Indexer.IndexIO;
import frc.robot.Subsystems.Indexer.IndexReal;
import frc.robot.Subsystems.Indexer.Indexer;
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
    private Indexer indexer;
  private hopper hopper;
  private Shoot shooter;
  private Climber climber;

  private Aim autoAim;
  private DriveTo autoDrive;
  private Trigger nearBump;
  private Trigger nearShoot;

  private double currentShootVelocity = 1.0; 
  
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

      if (indexerEnabled) {
        indexer = new Indexer(new IndexReal());
      } else {
        indexer = new Indexer(new IndexIO() {});
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

       /* Automation Triggers */
    
    //
    nearBump = new Trigger(()->
    !driver.povDown().getAsBoolean() && // TRIGGER OVERIDE 
    MathUtil.isNear(Units.inchesToMeters(182.11), drive.getPose().getX(), 0.75)
     || // Or
    MathUtil.isNear(Units.inchesToMeters(469), drive.getPose().getX(),0.75));

    nearShoot = new Trigger(()->
      !driver.povDown().getAsBoolean() && // TRIGGER OVERIDE 
      //If pose is 1 meter away or on our side we will strt spining up
      drive.getPose().getX() + 1 < LeftShootPose.getX()
    );

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
if (intakeEnabled) {
    driver.rightTrigger().whileTrue(intake.run());
        driver.leftTrigger().whileTrue(intake.out());
      }


// INDEXER CONTROLS
if (indexerEnabled) {
  operater.rightTrigger().whileTrue(indexer.runCommand(-0.5));
}

//HOPPER CONTROLS
if (hopperEnabled) {
    operater.rightBumper().whileTrue(hopper.down());
        operater.leftBumper().whileTrue(hopper.up());
    operater.leftTrigger().whileTrue(Commands.run(()->hopper.run(operater.getLeftY()/10),hopper));
}

// SHOOTER CONTROLS 
if (shooterEnabled) {
    driver.rightBumper().whileTrue(Commands.run(()->shooter.run(1), shooter));
    driver.leftBumper().whileTrue(Commands.runEnd(()->shooter.runTransfer(-0.5),()->shooter.stopTransfer(), shooter));

    operater.povUp().onTrue(
      Commands.runOnce(()->currentShootVelocity += 1)
    );
        operater.povDown().onTrue(

      Commands.runOnce(()->currentShootVelocity -= 1)
    );
}

// CLIMBER CONTROLS 
if (climberEnabled) {
    operater.povDown().whileTrue(climber.climb());
}

// AUTOMATION
if (automation) {
// Auto turn near bump
nearBump.whileTrue(autoAim.at(Math.toRadians(45), null, null));

// Auto Spin-Up near shooting positions
nearShoot.whileTrue(shooter.runAt(currentShootVelocity));
}

// Aim at Hub
    driver.y().whileTrue(
      autoAim.at(HubPose,
            () -> -driver.getLeftY(),
            () -> -driver.getLeftX()));

  // Drive to Shooting positions
    // driver.rightBumper().whileTrue(
    //   autoDrive.generateCommand(
    //      RightShootPose));

    // driver.leftBumper().whileTrue(
    //   autoDrive.generateCommand(LeftShootPose));

  }

  public Command getAutonomousCommand() {
    Logger.recordOutput("Auto Chosen", autoChooser.getSelected().getName());
    // return autoChooser.getSelected();
    return testAuto();
  }

  public Command testAuto() {
    /* 
     * Sets Pose to 0,0 
     * then drives to 0,.33
     * ~1 foot forwrd
     * hopefully
     */
    return Commands
    .runOnce(()->drive.setPose(
        new Pose2d(0,0,new Rotation2d())
      ),drive)
    .andThen(autoDrive.generateCommand(new Pose2d(0,0.33,new Rotation2d())));
  }

  
}
