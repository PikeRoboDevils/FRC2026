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
private final SparkMax m_F1 = new SparkMax(CanIds.ShooterF1,MotorType.kBrushless);
private final SparkMax m_F2 = new SparkMax(CanIds.ShooterF2,MotorType.kBrushless);
private final SparkMax m_F3 = new SparkMax(CanIds.ShooterF3,MotorType.kBrushless);

private final SparkMax Indexer = new SparkMax(CanIds.ShootIndexer,MotorType.kBrushless);

private SparkBaseConfig MotorConfig = new SparkMaxConfig();

public ShootReal(){
    
    MotorConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12);   



        // These should all be no Persist because it is very annoying to reset once you mess up

    Lead.configure(MotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    m_F1.configure(MotorConfig.follow(Lead,false), ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    // F2 AND F3 ARE INVERTED 
    m_F2.configure(MotorConfig.follow(Lead,true), ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    m_F3.configure(MotorConfig.follow(Lead,true), ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
}

    @Override
    public void updateInputs(ShootIOInputs inputs) {
        inputs.velocity = velocityConversionFactor * Lead.getEncoder().getVelocity();
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
    public void runIndex(double speed) {
        Indexer.set(speed);
    }

    @Override
    public void stopIndex() {
        Indexer.stopMotor();
    }

    @Override
    public void stop() {
        Lead.stopMotor();
        // m_F1.stopMotor();
        // m_F2.stopMotor();
        // m_F3.stopMotor();
    }

}
