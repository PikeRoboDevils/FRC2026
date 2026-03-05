package frc.robot.util;

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

  public boolean isPIDLoopRunning = false;

  private final Drive mSwerve;

  public Aim(Drive mSwerve, AprilTagFieldLayout field) {
    this.mSwerve = mSwerve;
  }

  /**
   * Actual Path On-the-fly Command Start from here
   *
   * @param waypoint
   * @return
   */
  public Command OverideRotation(Pose2d Anchor,DoubleSupplier X,DoubleSupplier Y) {

    Rotation2d goalRotation = mSwerve.getPose().relativeTo(Anchor).getRotation();

    // PositionPIDCommand.generateCommand(mSwerve, Anchor, kAutoAlignAdjustTimeout, false);
    
    DriveCommands.joystickDriveAtAngle(mSwerve, X, Y, ()->goalRotation);

    return Commands.none();
  }

  /** Will Aim at a target by overiding rotation
   */
  public Command at(String target,DoubleSupplier X,DoubleSupplier Y) {

    Map<String, Pose2d> targets = new HashMap<>();

    // Add key-value pairs using the put() method
        targets.put("HUB", new Pose2d());
        targets.put("TOWER", new Pose2d());
        targets.put("TOWARD 0,0", new Pose2d(0,0,new Rotation2d()));

        
    return Commands.defer(
        () -> {
          return OverideRotation(targets.get(target),X,Y);
        },
        Set.of());
  }
}
