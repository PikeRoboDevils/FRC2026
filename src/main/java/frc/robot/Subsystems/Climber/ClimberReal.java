package frc.robot.Subsystems.Climber;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.CanIds;

import static frc.robot.Constants.ClimberConstants.*;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ClimberReal implements ClimberIO {

    private final SparkMax Lead = new SparkMax(CanIds.ClimbLead,MotorType.kBrushless);
    private final SparkMax Follower = new SparkMax(CanIds.ClimbFollower,MotorType.kBrushless);

    private SparkBaseConfig lMotorConfig = new SparkMaxConfig();

    public ClimberReal() {
        
        Lead.getEncoder().setPosition(0);
        Follower.getEncoder().setPosition(0);

        lMotorConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12)
        .inverted(true);   

        Lead.configure(lMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        Follower.configure(lMotorConfig.inverted(true).follow(CanIds.ClimbLead), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(ClimberIOInputs inputs) {
        inputs.position = (Lead.getEncoder().getPosition() + Follower.getEncoder().getPosition())/2;
    }
    
    @Override 
    public void run(double speed) {
        Lead.set(speed);
    }

    @Override
    public void stop(){
        Lead.stopMotor();
    }



}
