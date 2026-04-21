// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import frc.robot.InputOutput.Controls;
import frc.robot.SimInstance.ModuleSim;
import frc.robot.SimInstance.GyroSim;
import frc.robot.Systems.*;
import frc.robot.Constants.*;

import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.drivesims.configs.DriveTrainSimulationConfig;
import org.ironmaple.simulation.seasonspecific.rebuilt2026.Arena2026Rebuilt;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.littletonrobotics.urcl.URCL;

import edu.wpi.first.math.geometry.Pose2d;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends LoggedRobot {

private Drive drive;
private Controls controls;
private Mechanisms mechanisms;

  public Robot() {

    // Record metadata
    Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
    Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
    Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
    Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
    Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);

    // Set up data receivers & replay source
    switch (Constants.currentMode) {
      case REAL:
        // Running on a real robot, log to a USB stick ("/U/logs")
        Logger.addDataReceiver(new WPILOGWriter());
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case SIM:
        // Running a simulation, only need network tables
        Logger.addDataReceiver(new NT4Publisher());
        break;

      case REPLAY:
        // Replaying a log, set up replay source
        setUseTiming(false); // Run as fast as possible
        String logPath = LogFileUtil.findReplayLog();
        Logger.setReplaySource(new WPILOGReader(logPath));
        Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
        break;
    }

    // Initialize URCL
    Logger.registerURCL(URCL.startExternal());

    // Start AdvantageKit logger
    Logger.start();



     var swerveDriveSimulation = new SwerveDriveSimulation(
  DriveTrainSimulationConfig.Default(), // Replace Soon
  new Pose2d());
  
  
  drive= new Drive(
    new GyroSim(swerveDriveSimulation.getGyroSimulation()),
    new ModuleSim(swerveDriveSimulation.getModules()[0]),
    new ModuleSim(swerveDriveSimulation.getModules()[1]),
    new ModuleSim(swerveDriveSimulation.getModules()[2]),
    new ModuleSim(swerveDriveSimulation.getModules()[3]));
  
    controls = new Controls(drive);

    mechanisms = new Mechanisms(Constants.currentMode);

    /* Simulation */
    if (Constants.currentMode == Mode.SIM) {
  // Obtains the default instance 
  SimulatedArena.getInstance();
  // Overrides the default simulation
  SimulatedArena.overrideInstance(new Arena2026Rebuilt(true)); 
  // Add a field elements
  SimulatedArena.getInstance().placeGamePiecesOnField();

  SimulatedArena.getInstance().addDriveTrainSimulation(swerveDriveSimulation);

  Logger.recordOutput("Field", SimulatedArena.getInstance().getGamePiecesArrayByType("Fuel"));
    }


  }
  
@Override
public void robotPeriodic() {
    drive.periodic();
    mechanisms.periodic();

}

@Override
public void teleopInit() {
  controls.start();
}

@Override
public void teleopPeriodic() {
  controls.periodic();
}


// Only on Desktop Simulation
@Override
public void simulationPeriodic() {
    SimulatedArena.getInstance().simulationPeriodic();
}


}




