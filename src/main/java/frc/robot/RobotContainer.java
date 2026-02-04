// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import org.littletonrobotics.junction.Logger;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.Intake.IntakeReal;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.Subsystems.drive.GyroIOPigeon2;
import frc.robot.Subsystems.drive.ModuleIOSpark;
import frc.robot.commands.DriveCommands;

public class RobotContainer {
  private CommandXboxController driver = new CommandXboxController(0);
  private CommandXboxController operater = new CommandXboxController(1);


  private final SendableChooser<Command> autoChooser;
  private final Drive drive;


  private Intake intake= new Intake(new IntakeReal());

  public RobotContainer() {
    drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOSpark(0),
                new ModuleIOSpark(1),
                new ModuleIOSpark(2),
                new ModuleIOSpark(3));

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

    driver.rightTrigger().whileTrue(intake.run());
        driver.leftTrigger().whileTrue(intake.out());

  }

  public Command getAutonomousCommand() {
    Logger.recordOutput("Auto Chosen", autoChooser.getSelected().getName());
    return autoChooser.getSelected();
  }
}
