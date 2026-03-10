package frc.robot.Subsystems.Climber;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Climber.ClimberIO.ClimberIOInputs;

import static frc.robot.Constants.ClimberConstants.*;

public class Climber extends SubsystemBase {
    
    private ClimberIO io;
    private ClimberIOInputs inputs = new ClimberIOInputsAutoLogged();
    
    private PIDController pid = new PIDController(kP,0, 0);
    @Override
    public void periodic() {
        io.updateInputs(inputs);
    }
    

    public Climber(ClimberIO io) {
        this.io = io;
    }

    
    public void run(double speed) {
        io.run(speed);
    }

    public void stop() {
        io.stop();
    }

    public void toPosition(double position) {
        if (pid.atSetpoint()) {return;}

        var output = MathUtil.clamp(pid.calculate(inputs.position, position), .5, .5);
        run(output);

    }

    public Command climb(){
        return Commands.run(()->toPosition(climbPosition)).finallyDo(()->toPosition(0));
    }


}
