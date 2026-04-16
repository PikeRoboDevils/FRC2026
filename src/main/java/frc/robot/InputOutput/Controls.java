package frc.robot.InputOutput;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Systems.Drive;

public class Controls {
    private Drive drive;
    public XboxController driver = new XboxController(0);

    public Controls(Drive instatiatedDrive){
        this.drive = instatiatedDrive;
    }
 
    

    public void start(){}

    public void periodic(){

    if (driver.getBButtonPressed()){ drive.resetGyro(0);}

    // drive.runVelocity(new ChassisSpeeds());

    }
    
}
