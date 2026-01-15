package frc.robot.Subsystems.Shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShootIO {

    @AutoLog
    public static class ShootIOInputs {
    
    }

    public default void updateInputs(ShootIOInputs inputs) {}
    public default void run(double speed){}
    public default void runVolts(double volts){}
    public default void stop(){}
}
