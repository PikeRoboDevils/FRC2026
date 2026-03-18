package frc.robot.Subsystems.hopper;

import static frc.robot.Constants.HopperConstants.positionConversionFactor;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.CanIds;

import com.revrobotics.spark.config.SparkMaxConfig;

public class hopperReal implements hopperIO {
    private final SparkMax storage = new SparkMax(CanIds.Hopper,MotorType.kBrushless);
    private SparkBaseConfig storageConfig = new SparkMaxConfig();




    public hopperReal(){
    storageConfig
        .smartCurrentLimit(60,60)
        .idleMode(IdleMode.kBrake)
        .voltageCompensation(12); 


        storage.configure(storageConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(hopperIOInputs inputs) {

        inputs.position = storage.getEncoder().getPosition() * positionConversionFactor;
    }
    @Override
    public void run(double speed) {
        storage.set(speed);
    }
    
    @Override
    public void runVolts(double volts) {
        storage.setVoltage(volts); 
    }

    @Override
    public void stop() {
        storage.stopMotor();
    }



}
