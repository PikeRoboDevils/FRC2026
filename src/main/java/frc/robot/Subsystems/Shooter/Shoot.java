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
    private BangBangController bangControl = new BangBangController();

      public double currentShootVelocity = 0.1; 

    public Shoot(ShootIO io) {
        this.io = io;
    }
    
    public void run(double speed){
        io.run(speed);
    }

    public void stop(){
        io.stop();
    }

        
    public void runTransfer(double speed){
        io.runIndex(speed);
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

    
    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Shooter", inputs);
        Logger.recordOutput("setVelocity", currentShootVelocity);
    }
}
