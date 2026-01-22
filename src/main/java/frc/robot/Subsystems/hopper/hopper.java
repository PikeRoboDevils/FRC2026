package frc.robot.Subsystems.hopper;

public class hopper {
    private hopperIO io;


    public void run() {
    io.run(0.5);
    }
    public void stop() {
    io.stop();
    }
}
