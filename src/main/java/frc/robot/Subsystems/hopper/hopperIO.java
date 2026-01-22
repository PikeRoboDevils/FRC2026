package frc.robot.Subsystems.hopper;

import org.littletonrobotics.junction.AutoLog;

public interface hopperIO {
   
    @AutoLog
    public static class hopperIOInputs {
        
    }
    public default void updateInputs(hopperIOInputs inputs) {}
    public default void run(double speed){}
    public default void runVolts(double volts){}
    public default void stop(){}

}
