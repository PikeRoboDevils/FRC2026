package frc.robot.Subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.IntakeIO.IntakeIOInputs;

public class Intake extends SubsystemBase {
    private IntakeIO io;
    // private IntakeIOInputsAutoLogged inputs= new IntakeIOInputsAutoLogged();


public void run() {
    io.run(0.5);
}
public void stop() {
    io.stop();
}

    
}
