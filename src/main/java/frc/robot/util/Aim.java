package frc.robot.util;

import static edu.wpi.first.units.Units.Meters;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.commands.DriveCommands;

import java.util.Set;
import java.util.function.DoubleSupplier;

/**
 * A Util Class for making path on the fly for all auto movement with vision or object detection in
 * the future
 */
public class Aim {

  private final Drive drive;

  public Aim(Drive drive, AprilTagFieldLayout field) {
    this.drive = drive;
  }

  /**
   * Actual Path On-the-fly Command Start from here
   *
   * @param waypoint
   * @return
   */
  public Command OverideRotation(Rotation2d angle,DoubleSupplier X,DoubleSupplier Y) {


    return DriveCommands.joystickDriveAtAngle(drive, X, Y, ()->angle);
  }

  /** Will Aim at a target by overiding rotation
   */
  public Command at(Pose2d target,DoubleSupplier X,DoubleSupplier Y) {
      var dX = target.getMeasureX().abs(Meters)-drive.getPose().getMeasureX().abs(Meters);

    
    var dY = target.getMeasureY().abs(Meters)-drive.getPose().getMeasureY().abs(Meters);


    Rotation2d goalRotation = new Rotation2d(Math.atan(dX/dY));
   
    return Commands.defer(
        () -> {
          return OverideRotation(goalRotation,X,Y);  
        },
        Set.of());
  }

   /** Will Aim at a target by overiding rotation given an angle
   */
  public Command at(double angle,DoubleSupplier X,DoubleSupplier Y) {

  
        
    return Commands.defer(
        () -> {
          return OverideRotation(new Rotation2d(angle),X,Y);

            
        },
        Set.of());
  }
}
