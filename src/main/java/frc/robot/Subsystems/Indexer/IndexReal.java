package frc.robot.Subsystems.Indexer;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Constants.CanIds;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class IndexReal implements IndexIO {

    private final SparkMax mSparkMax = new SparkMax(CanIds.Indexer,MotorType.kBrushless);


    private SparkBaseConfig lMotorConfig = new SparkMaxConfig();

    public IndexReal() {

        lMotorConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12)
        .inverted(true);   

        mSparkMax.configure(lMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(IndexIOInputs inputs) {
    }
    
    @Override 
    public void run(double speed) {
        mSparkMax.set(speed);
    }

    @Override
    public void stop(){
        mSparkMax.stopMotor();
    }



}
