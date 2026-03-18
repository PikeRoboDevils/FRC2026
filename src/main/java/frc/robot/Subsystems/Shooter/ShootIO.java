package frc.robot.Subsystems.Shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShootIO {

    @AutoLog
    public static class ShootIOInputs {
        public double velocity = 0;
    }

    public default void updateInputs(ShootIOInputs inputs) {}
    public default void run(double speed){}
        public default void runIndex(double speed){}
            public default void stopIndex(){}
    public default void runVolts(double volts){}
    public default void stop(){}
}
