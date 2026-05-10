package frc.robot.SimInstance;


import static edu.wpi.first.units.Units.Meters;

import org.ironmaple.simulation.IntakeSimulation;
import org.ironmaple.simulation.drivesims.AbstractDriveTrainSimulation;

import frc.robot.InputOutput.MechanismIO;

public class IntakeSim implements MechanismIO {


  private final IntakeSimulation intakeSimulation;

    public IntakeSim(AbstractDriveTrainSimulation driveTrain) {
      
      this.intakeSimulation = IntakeSimulation.OverTheBumperIntake(
        // Specify the type of game pieces that the intake can collect
        "Fuel",
        // Specify the drivetrain to which this intake is attached
        driveTrain,
        // Width of the intake
        Meters.of(0.7),
        // The extension length of the intake beyond the robot's frame (when activated)
        Meters.of(0.2),
        // The intake is mounted on the back side of the chassis
        IntakeSimulation.IntakeSide.BACK,
        // The intake can hold up to 1 note
        80); // Estimate of Max Fuel
}

  @Override
  public void updateInputs(MechanismIOInputs inputs) {
    inputs.simulationBoolean = (intakeSimulation.getGamePiecesAmount()>0);
  }

  @Override
  public void run(double speed){

    if (speed>0){
    intakeSimulation.startIntake();
    } else {
      intakeSimulation.stopIntake();
    } 
  }
  
  @Override
  public void stop(){
      intakeSimulation.stopIntake();
    
  }
  
  
}
