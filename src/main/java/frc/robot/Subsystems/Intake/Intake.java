package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Intake.IntakeIO.IntakeIOInputs;

public class Intake extends SubsystemBase {
    private IntakeIO io;
    private IntakeIOInputsAutoLogged inputs= new IntakeIOInputsAutoLogged();
    public Intake(IntakeIO intakeIO) {
        this.io = intakeIO;
    }


public Command run() {
    return Commands.runEnd(()->run(0.75), ()->stop());
}
public Command runAuto() {
    return Commands.run(()->run(0.75));
}
public Command out() {
    return Commands.runEnd(()->run(-0.75), ()->stop());
}

public void run(double speed) {
    io.run(speed);
}
public void stop() {
    io.stop();
}

    
}
