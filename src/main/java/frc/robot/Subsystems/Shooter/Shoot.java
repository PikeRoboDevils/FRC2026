package frc.robot.Subsystems.Shooter;

public class Shoot {
    //PID controller later
    private ShootIO io;

    public void run(){
        io.run(1);
    }

    public void stop(){
        io.stop();
    }
}
