package frc.robot.Subsystems.hopper;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class hopperReal implements hopperIO {
    private final SparkMax storage = new SparkMax(1,MotorType.kBrushless);
    private SparkBaseConfig storageConfig = new SparkMaxConfig();




    public hopperReal(){
    storageConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12); 



        storage.configure(storageConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
