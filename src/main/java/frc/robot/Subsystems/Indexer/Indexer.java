package frc.robot.Subsystems.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class Indexer extends SubsystemBase {
    
    private IndexIO io;
    private IndexIOInputsAutoLogged inputs = new IndexIOInputsAutoLogged();
    
    @Override
    public void periodic() {
        io.updateInputs(inputs);
    }
    

    public Indexer(IndexIO io) {
        this.io = io;
    }

    
    public void run(double speed) {
        io.run(speed);
    }

    public void stop() {
        io.stop();
    }

    public Command runCommand(double speed) {
        return Commands.run(()->run(speed)).finallyDo(()->stop());
    }

}
