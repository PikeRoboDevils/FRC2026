package frc.robot.InputOutput;

import org.littletonrobotics.junction.AutoLog;


public interface MechanismIO {

  @AutoLog
  public static class MechanismIOInputs {
    public String name = "";
    public double velocity;
    public double position;

    public double appliedOutput;

  }

  public default void updateInputs(MechanismIOInputs inputs) {}

    public default void run(double speed){}
    public default void runSecondary(double speed){}
    public default void stop(){}
    public default void stopSecondary(){}
}