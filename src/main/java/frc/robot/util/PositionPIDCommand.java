/*Taken straight from 4915
 * https://github.com/Spartronics4915/2025-Reefscape/blob/unstable/src/main/java/com/spartronics4915/frc2025/commands/autos/PositionPIDCommand.java#L30
 */
package frc.robot.util;

import static edu.wpi.first.units.Units.Centimeter;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Seconds;
import static frc.robot.Subsystems.drive.DriveConstants.*;

import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.util.MsvcRuntimeException;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Subsystems.drive.Drive;
import java.util.function.BooleanSupplier;

public class PositionPIDCommand extends Command {

  public Drive mSwerve;
  public final Pose2d goalPose;
    public final Rotation2d target;
  

  private final Trigger endTrigger;
  private final Trigger endTriggerDebounced;

  private final Timer timer = new Timer();

  private final BooleanPublisher endTriggerLogger =
      NetworkTableInstance.getDefault()
          .getTable("logging")
          .getBooleanTopic("PositionPIDEndTrigger")
          .publish();
  private final DoublePublisher xErrLogger =
      NetworkTableInstance.getDefault().getTable("logging").getDoubleTopic("X Error").publish();
  private final DoublePublisher yErrLogger =
      NetworkTableInstance.getDefault().getTable("logging").getDoubleTopic("Y Error").publish();

  private final BooleanSupplier booleanSupplier;

  private PositionPIDCommand(Drive mSwerve, Pose2d goalPose) {
    this.mSwerve = mSwerve;
    this.goalPose = goalPose;
    this.target = goalPose.getRotation();

    booleanSupplier =
        () -> {
          Pose2d diff = mSwerve.getPose().relativeTo(goalPose);

          var rotation =
              MathUtil.isNear(
                  0.0,
                  diff.getRotation().getRotations(),
                  kRotationTolerance.getRotations(),
                  0.0,
                  1.0);

          var position = diff.getTranslation().getNorm() < kPositionTolerance.in(Meters);

          var speed = mSwerve.getSpeedMeters() < kSpeedTolerance.in(Meters);

          // System.out.println("end trigger conditions R: "+ rotation + "\tP: " + position + "\tS:" + speed);

          return rotation && position; // && speed;
        };

    endTrigger = new Trigger(booleanSupplier);

    endTriggerDebounced = endTrigger.debounce(kEndTriggerDebounce.in(Seconds));
  }


  // AIM PID COMMAND
  private PositionPIDCommand(Drive mSwerve, Rotation2d target) {
    this.mSwerve = mSwerve;
    this.goalPose = mSwerve.getPose();
    this.target = target;

    booleanSupplier =
        () -> {
          Rotation2d diff = mSwerve.getPose().getRotation().minus(target);

          var rotation =
              MathUtil.isNear(
                  0.0,
                  diff.getRotations(),
                  kRotationTolerance.getRotations(),
                  0.0,
                  1.0);
          
          return rotation;// Might not want it to end?
        };

    endTrigger = new Trigger(booleanSupplier);

    endTriggerDebounced = endTrigger.debounce(kEndTriggerDebounce.in(Seconds));
  }

  // Self Calling Commands 

  public static Command generateCommand(
      Drive swerve, Pose2d goalPose, Time timeout, Boolean freeze) {
    return new PositionPIDCommand(swerve, goalPose)
        .withTimeout(timeout)
        .finallyDo(
            () -> {
              if (freeze) {
                swerve.stopWithX();
              }
            });
  }

    public static Command overideDrive(
      Drive swerve, Rotation2d target) {
    return new PositionPIDCommand(swerve, target);
  }

  // WPILIB COMMAND FUNCTIONS

  @Override
  public void initialize() {
    endTriggerLogger.accept(endTrigger.getAsBoolean());
    timer.restart();
  }

  @Override
  public void execute() {
    PathPlannerTrajectoryState goalState = new PathPlannerTrajectoryState();
    goalState.pose = goalPose;

    endTriggerLogger.accept(endTrigger.getAsBoolean());
    ChassisSpeeds speeds =
    Constants.CameraConstants.mDriveController
        .calculateRobotRelativeSpeeds(mSwerve.getPose(), goalState);

    mSwerve.runVelocity(speeds);
    
    // mSwerve.OverideRotation(target);

    xErrLogger.accept(mSwerve.getPose().getX() - goalPose.getX());
    yErrLogger.accept(mSwerve.getPose().getY() - goalPose.getY());
  }

  @Override
  public void end(boolean interrupted) {
    endTriggerLogger.accept(endTrigger.getAsBoolean());
    timer.stop();

    Pose2d diff = mSwerve.getPose().relativeTo(goalPose);

    System.out.println(
        "Adjustments to alginment took: "
            + timer.get()
            + " seconds and interrupted = "
            + interrupted
            + "\nPosition offset: "
            + Centimeter.convertFrom(diff.getTranslation().getNorm(), Meters)
            + " cm"
            + "\nRotation offset: "
            + diff.getRotation().getMeasure().in(Degrees)
            + " deg"
            + "\nVelocity value: "
            + mSwerve.getSpeedMeters() + "m/s"

        );
  }

  @Override
  public boolean isFinished() {
    return endTriggerDebounced.getAsBoolean();
  }
}
