package frc.robot.Subsystems.Shooter;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

public class Shoot extends SubsystemBase {

    private ShootIO io;
    private ShootIOInputsAutoLogged inputs = new ShootIOInputsAutoLogged();

    public double currentShootvoltage = 0.2;

    private double minShootVelocity = 3000;

    public Shoot(ShootIO io) {
        this.io = io;
    }

    private void run(double speed) {
        io.run(speed);
    }

    private void stop() {
        io.stop();
    }

    public Command runTransferCommand(double speed) {
        return Commands.run(() -> runTransfer(speed),this).finallyDo(()->stopTransfer());
    }

    private void runTransfer(double speed) {
        if (inputs.velocity > minShootVelocity) {
            io.runIndex(speed);
        } else {
            io.runIndex(0);
        }
    }

    private void stopTransfer() {
        io.stopIndex();
    }

    public Command runAt(double percent) {
        return Commands.run(
                () -> run(percent)).alongWith(runTransferCommand(-0.5))
                .finallyDo(() -> stop());
    }

    /**
     * Runs at the set velocity 
     * velocity is set with setVelocity()
     */
    public void run() {
         run(currentShootvoltage);
         runTransfer(-0.5);
    }

    public void setVelocity(double voltage){
        currentShootvoltage = voltage;
    }


    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);
        // Logger.recordOutput("setVelocity", currentShootVelocity);
        Logger.recordOutput("setVoltage", currentShootvoltage);
        
    }
}
