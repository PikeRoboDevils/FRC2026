package frc.robot.InputOutput;

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
        drive.joystickDrive(driver.getLeftX(),driver.getLeftY(), driver.getRightX());
        if (driver.getBButtonPressed()){ drive.resetGyro(0);}

        

    }
    
}
