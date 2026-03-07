package frc.robot.Subsystems.hopper;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.HopperConstants.*;


public class hopper extends SubsystemBase{
    private hopperIO io;
        private hopperIOInputsAutoLogged inputs= new hopperIOInputsAutoLogged();

        private PIDController extendPid = new PIDController(kP, 0, kD);
    public hopper(hopperIO hopperIO) {
        this.io = hopperIO;
    }


    public void run(double speed) {
        io.run(speed);
    }

    public void toSetpoint(double angle) {
        if (extendPid.atSetpoint()) {return;}

        var speed = extendPid.calculate(inputs.position, angle);
        run(speed);
    }

    public Command up() {
        
      return Commands.run(()->
        toSetpoint(upSetpoint))
        .finallyDo(()->run(0));
    }

    public Command mid() {
        
      return Commands.run(()->
        toSetpoint(midSetpoint))
        .finallyDo(()->run(0));
    }

    public Command down() {
        
      return Commands.run(()->
        toSetpoint(downSetpoint))
        .finallyDo(()->run(0));
    }
    public void stop() {
    io.stop();
    }
}
