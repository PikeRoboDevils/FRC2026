package frc.robot.Subsystems.Intake;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class IntakeReal implements IntakeIO {

private final SparkMax Lead = new SparkMax(11,MotorType.kBrushless);
// private final SparkMax Follow = new SparkMax(200,MotorType.kBrushless);//doesnt exitst
private SparkBaseConfig lMotorConfig = new SparkMaxConfig();
// private SparkBaseConfig fMotorConfig;

public IntakeReal(){
    
    lMotorConfig
        .smartCurrentLimit(40,60)
        .idleMode(IdleMode.kCoast)
        .voltageCompensation(12)
        .inverted(true);   

    // Right follows Left
    // fMotorConfig= lMotorConfig.follow(Lead).inverted(false);

    Lead.configure(lMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    // Follow.configure(fMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
        // Follow.stopMotor();
    }
    
}
