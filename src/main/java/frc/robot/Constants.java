package frc.robot;

public final class Constants {
  public static final Mode simMode = Mode.REAL;
  public static final Mode currentMode = Robot.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }
}