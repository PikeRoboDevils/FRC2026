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

package frc.robot.Subsystems.drive;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static frc.robot.Subsystems.drive.DriveConstants.*;

import edu.wpi.first.math.geometry.Rotation2d;
// import org.ironmaple.simulation.drivesims.GyroSimulation;

/** IO implementation for Pigeon 2. */
public class GyroIOSim implements GyroIO {
  // private final Pigeon2 pigeon = new Pigeon2(pigeonCanId);
  // private final GyroSimulation gyroSimulation;

  // public GyroIOSim(GyroSimulation gyroSimulation) {
  //   this.gyroSimulation = gyroSimulation;

  //   gyroSimulation.setRotation(new Rotation2d());
  // }

  // @Override
  // public void updateInputs(GyroIOInputs inputs) {
  //   inputs.connected = true;
  //   inputs.yawPosition = gyroSimulation.getGyroReading();
  //   inputs.yawVelocityRadPerSec = gyroSimulation.getMeasuredAngularVelocity().in(RadiansPerSecond);

  //   // inputs.odometryYawTimestamps =
  //   //     gyroSimulation.
  //   inputs.odometryYawPositions = gyroSimulation.getCachedGyroReadings();
  //   // yawTimestampQueue.clear();
  //   // yawPositionQueue.clear();
  // }
}
