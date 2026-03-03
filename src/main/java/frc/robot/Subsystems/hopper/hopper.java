package frc.robot.Subsystems.hopper;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class hopper extends SubsystemBase{
    private hopperIO io;
        private hopperIOInputsAutoLogged inputs= new hopperIOInputsAutoLogged();
    public hopper(hopperIO hopperIO) {
        this.io = hopperIO;
    }


    public void run(double speed) {
        io.run(speed);
    }

    public Command down() {

        Commands.runEnd(()->run(0.1), ()->stop());
        return Commands.none();
    }
    public Command up() {
      return Commands.runEnd(()->run(-0.1), ()->stop());
    }
    public void stop() {
    io.stop();
    }
}
