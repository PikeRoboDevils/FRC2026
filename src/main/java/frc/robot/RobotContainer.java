// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Subsystems.Intake.Intake;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.Subsystems.drive.GyroIOPigeon2;
import frc.robot.Subsystems.drive.ModuleIOSpark;
import frc.robot.commands.DriveCommands;

public class RobotContainer {
  private CommandXboxController driver = new CommandXboxController(0);
  private CommandXboxController operater = new CommandXboxController(1);

  private final Drive drive;


  private Intake intake= new Intake();

  public RobotContainer() {
    drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOSpark(0),
                new ModuleIOSpark(1),
                new ModuleIOSpark(2),
                new ModuleIOSpark(3));
    configureBindings();
  }

  private void configureBindings() {

      drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -driver.getLeftY(),
            () -> -driver.getLeftX(),
            () -> driver.getRightX()));


    driver.x().onTrue(Commands.runEnd(
      ()->intake.run(),
      ()->intake.stop(),
      intake));
     

  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
