package frc.robot.Subsystems.Shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ShootReal implements ShootIO{

private final SparkMax Lead = new SparkMax(1,MotorType.kBrushless);
private final SparkMax Follow = new SparkMax(2,MotorType.kBrushless);
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
