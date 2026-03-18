package frc.robot.Subsystems.Indexer;

import org.littletonrobotics.junction.AutoLog;


public interface  IndexIO {

    @AutoLog 
    public static class IndexIOInputs{

    };

    public default void updateInputs(IndexIOInputs inputs) {}
    public default void run(double speed){}
    public default void stop(){}


}
