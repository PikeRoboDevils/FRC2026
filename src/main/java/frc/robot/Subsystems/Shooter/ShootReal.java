package frc.robot.Subsystems.Shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.CanIds;

import com.revrobotics.spark.config.SparkMaxConfig;
import static frc.robot.Constants.ShooterConstants.*;

public class ShootReal implements ShootIO{

private final SparkMax Lead = new SparkMax(CanIds.ShooterLead,MotorType.kBrushless);
private final SparkMax Follow = new SparkMax(CanIds.ShooterFollower,MotorType.kBrushless);
private SparkBaseConfig lMotorConfig = new SparkMaxConfig();
private SparkBaseConfig fMotorConfig;

public ShootReal(){
    
    lMotorConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12);   

    // Right follows Left
    fMotorConfig= lMotorConfig.follow(Lead).inverted(true);

    Lead.configure(lMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    Follow.configure(fMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
}

    @Override
    public void updateInputs(ShootIOInputs inputs) {
        inputs.velocity = velocityConversionFactor * (Lead.getEncoder().getVelocity() + Follow.getEncoder().getVelocity())/2;
    }

    @Override
    public void run(double speed) {
        Lead.set(speed);
    }
    
    @Override
    public void runVolts(double volts) {
        Lead.setVoltage(volts); 
    }

    @Override
    public void stop() {
        Lead.stopMotor();
        Follow.stopMotor();
    }

}
