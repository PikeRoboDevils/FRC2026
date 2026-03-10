package frc.robot.util;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static frc.robot.Subsystems.drive.DriveConstants.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.IdealStartingState;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Subsystems.drive.Drive;
import frc.robot.commands.DriveCommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  public Command OverideRotation(Pose2d Anchor,DoubleSupplier X,DoubleSupplier Y) {

    var dX = Anchor.getMeasureX().abs(Meters)-drive.getPose().getMeasureX().abs(Meters);

    
    var dY = Anchor.getMeasureY().abs(Meters)-drive.getPose().getMeasureY().abs(Meters);




    Rotation2d goalRotation = new Rotation2d(Math.tanh(dX/dY));

    return DriveCommands.joystickDriveAtAngle(drive, X, Y, ()->goalRotation);
  }

  /** Will Aim at a target by overiding rotation
   */
  public Command at(String target,DoubleSupplier X,DoubleSupplier Y) {

    Map<String, Pose2d> targets = new HashMap<>();

    // Add key-value pairs using the put() method
        targets.put("HUB", new Pose2d(4.6,4.0,new Rotation2d(0,0)));
        targets.put("TOWER", new Pose2d( ));
        targets.put("TOWARD 0,0", new Pose2d(0,0,new Rotation2d()));

        
    return Commands.defer(
        () -> {
        //   return OverideRotation(targets.get(target),X,Y);
        return DriveCommands.joystickDriveAtAngle(drive, X, Y, 
            ()->drive.getPose().relativeTo(new Pose2d(0,0,new Rotation2d())).getRotation()
            );

            
        },
        Set.of());
  }
}
