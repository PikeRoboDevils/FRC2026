package frc.robot.Subsystems.Shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import org.littletonrobotics.junction.Logger;

public class Shoot extends SubsystemBase{

    private ShootIO io;
    private ShootIOInputsAutoLogged inputs = new ShootIOInputsAutoLogged();
    private BangBangController bangControl = new BangBangController(1);

      public double currentShootVelocity = 3000; 

    public Shoot(ShootIO io) {
        this.io = io;
    }
    
    public void run(double speed){
        io.run(speed);
    }

    public void stop(){
        io.stop();
    }

        
    public Command runTransferCommand(double speed){
        return Commands.run(()->runTransfer(speed));
    }

    public void runTransfer(double speed){
        io.runIndex(speed);
    }

    public Command stopTransferCommand(){
        return Commands.run(()->stopTransfer());
    }

    public void stopTransfer(){
        io.stopIndex();
    }


    public Command runAt(double velocity) {
        return Commands.run(
        ()->run(
            bangControl.calculate(inputs.velocity, velocity))
            )
        .finallyDo(()->stop());
    }

    public Command stopCommand() {
        return Commands.run(()->stop());
    }

    
    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);
        Logger.recordOutput("setVelocity", currentShootVelocity);
    }
}
