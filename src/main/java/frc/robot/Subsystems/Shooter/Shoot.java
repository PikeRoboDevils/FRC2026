package frc.robot.Subsystems.Shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Shooter.ShootIO.ShootIOInputs;
import static frc.robot.Constants.ShooterConstants.*;

public class Shoot extends SubsystemBase{

    private ShootIO io;
    private ShootIOInputs inputs;
    private PIDController velocityPid = new PIDController(kP, 0, kD);

    public Shoot(ShootIO io) {
        this.io = io;
    }
    
    public void run(double speed){
        io.run(speed);
    }

    public void stop(){
        io.stop();
    }

    public void toSetpoint(double setpoint){
        if (velocityPid.atSetpoint()) {return;}

        var output = velocityPid.calculate(inputs.velocity, setpoint);
        run(MathUtil.clamp(output, 0, 1));
        
    }

    public Command runAt(double velocity) {
        return Commands.run(()->toSetpoint(velocity));
    }
}
