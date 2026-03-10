package frc.robot.Subsystems.Climber;

import org.littletonrobotics.junction.AutoLog;


public interface  ClimberIO {

    @AutoLog 
    public static class ClimberIOInputs{
        public double position;
    };

    public default void updateInputs(ClimberIOInputs inputs) {}
    public default void run(double speed){}
    public default void stop(){}


}
