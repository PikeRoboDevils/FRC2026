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
import java.util.List;
import java.util.Set;

/**
 * A Util Class for making path on the fly for all auto movement with vision or object detection in
 * the future
 */
public class DriveTo {

  public boolean isPIDLoopRunning = false;

  private final Drive mSwerve;

  public DriveTo(Drive mSwerve, AprilTagFieldLayout field) {
    this.mSwerve = mSwerve;
  }

  /**
   * Actual Path On-the-fly Command Start from here
   *
   * @param waypoint
   * @return
   */
  public Command getPathFromWaypoint(Pose2d waypoint) {
    List<Waypoint> waypoints =
        PathPlannerPath.waypointsFromPoses(
            new Pose2d(
                mSwerve.getPose().getTranslation(),
                new Rotation2d(
                    mSwerve.getChassisSpeeds().vxMetersPerSecond,
                    mSwerve.getChassisSpeeds().vyMetersPerSecond)));

    if (waypoints.get(0).anchor().getDistance(waypoints.get(1).anchor()) < 0.01) {
      return Commands.sequence(
          Commands.print("start position PID loop"),
          PositionPIDCommand.generateCommand(mSwerve, waypoint, kAutoAlignAdjustTimeout, true),
          Commands.print("end position PID loop"));
    }

    PathPlannerPath path =
        new PathPlannerPath(
            waypoints,
            DriverStation.isAutonomous() ? kAutoPathConstraints : kTeleopPathConstraints,
            new IdealStartingState(
                MetersPerSecond.of(
                    new Translation2d(
                            mSwerve.getChassisSpeeds().vxMetersPerSecond,
                            mSwerve.getChassisSpeeds().vyMetersPerSecond)
                        .getNorm()),
                mSwerve.getRotation()),
            new GoalEndState(0.0, waypoint.getRotation()));

    path.preventFlipping = true;

    return (AutoBuilder.followPath(path)
            .andThen(
                Commands.print("start position PID loop"),
                PositionPIDCommand.generateCommand(
                        mSwerve,
                        waypoint,
                        (DriverStation.isAutonomous()
                            ? kAutoAlignAdjustTimeout
                            : kTeleopAlignAdjustTimeout),
                        DriverStation.isAutonomous() ? true : false) // for teleop at least
                    .beforeStarting(
                        Commands.runOnce(
                            () -> {
                              isPIDLoopRunning = true;
                            }))
                    .finallyDo(
                        () -> {
                          isPIDLoopRunning = false;
                        })))
        .finallyDo(
            (interupt) -> {
              if (interupt) { // if this is false then the position pid would've X braked & called
                // the same method
                Commands.none(); // does nothing so we can keep driving
              }
            });
  }

  /** Will go straight to the given pose */
  public Command generateCommand(Pose2d pose) {
    return Commands.defer(
        () -> {
          return getPathFromWaypoint(pose);
        },
        Set.of());
  }
}
