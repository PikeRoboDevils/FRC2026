// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {
  public static final Mode simMode = Mode.REAL;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public class systems{
    public static final boolean hopper = false;
    public static final boolean intake = false;
  }

  public static class HopperConstants {
      public static final double kP = 0; 
      public static final double kD = 0;
      
      public static final double upSetpoint = 0; 
      public static final double midSetpoint = 0.5; 
      public static final double downSetpoint = 1;

      public static final double positionConversionFactor = 1;
  }

  public static class ShooterConstants {
    public static final double kP = 1;
    public static final double kD = 0;

    public static final double velocityConversionFactor = 1;
    
  } 

  public class KeyPoses{
    // not set yet
    public static final Pose2d LeftShoot = new Pose2d(3.15,5.3,new Rotation2d());
    public static final Pose2d RightShoot = new Pose2d(3.1,2.1,new Rotation2d());
  }


  /* Vision Test Bot  */
  public static class TestBot {
    // Cam 1 is roughly on top of the lower mount for elevator lokking in towards the reef tags.
    public static String CAM1N = "Test cam";

    public static Rotation3d CAM1R =
        new Rotation3d(0, 0, 0);

    public static Translation3d CAM1T =
        new Translation3d(Units.inchesToMeters(13.5),0,Units.inchesToMeters(7));
}


/* Vision 2025 Poses */
  public static class CameraConstants {
    // Cam 1 is roughly on top of the lower mount for elevator lokking in towards the reef tags.
    public static String CAM1N = "LEFT_CAM";

    public static Rotation3d CAM1R =
        new Rotation3d(0, Units.degreesToRadians(25), Units.degreesToRadians(40));

    public static Translation3d CAM1T =
        new Translation3d(
            Units.inchesToMeters(-4), // transform of camera (dont forget forward+ left+ up+)
            Units.inchesToMeters(13),
            Units.inchesToMeters(8));

    // public static String CAM3N = "ELEV_CAM";

    // public static Rotation3d CAM3R =
    //     new Rotation3d(Math.PI, 0, 0);

    // public static Translation3d CAM3T =
    //     new Translation3d(
    //         Units.inchesToMeters(4.5), // transform of camera (dont forget forward+ left+ up+)
    //         Units.inchesToMeters(14), // to right
    //         Units.inchesToMeters(14));

    public static String CAM2N = "RIGHT_CAM";

    public static Rotation3d CAM2R =
        new Rotation3d(0, Units.degreesToRadians(25), Units.degreesToRadians(180));

    public static Translation3d CAM2T =
        new Translation3d(
            Units.inchesToMeters(-4), // transform of camera (dont forget forward+ left+ up+)
            Units.inchesToMeters(-13),
            Units.inchesToMeters(8));


      public static PPHolonomicDriveController mDriveController =
      new PPHolonomicDriveController(new PIDConstants(1), new PIDConstants(1)); // NOT TUNED
  // PPHolonomicController is the built in path following controller for holonomic drive trains;
  }
  
}
