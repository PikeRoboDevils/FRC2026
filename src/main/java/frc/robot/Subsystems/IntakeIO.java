package frc.robot.Subsystems;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {

    @AutoLog
    public static class IntakeIOInputs {
        
    }

    public default void updateInputs(IntakeIOInputs inputs) {}
    public default void run(double speed){}
    public default void runVolts(double volts){}
    public default void stop(){}
}
