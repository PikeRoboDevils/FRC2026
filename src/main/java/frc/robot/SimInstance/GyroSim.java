package frc.robot.SimInstance;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import java.util.Queue;
import org.ironmaple.simulation.drivesims.GyroSimulation;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.InputOutput.GyroIO;

import frc.robot.Systems.SparkOdometryThread;

public class GyroSim implements GyroIO {

  private final GyroSimulation gyroSimulation;

  // private final StatusSignal<Angle> yaw = 
  private final Queue<Double> yawPositionQueue;
  private final Queue<Double> yawTimestampQueue;
  // private final StatusSignal<AngularVelocity> yawVelocity = gyroSimulation.getMeasuredAngularVelocity();

    public GyroSim(GyroSimulation gyroSimulation) {
        this.gyroSimulation = gyroSimulation;

      yawTimestampQueue = SparkOdometryThread.getInstance().makeTimestampQueue();
      yawPositionQueue = SparkOdometryThread.getInstance().registerSignal(()->gyroSimulation.getGyroReading().getDegrees());

        
    }


  @Override
  public void updateInputs(GyroIOInputs inputs) {
    inputs.connected = true;

    inputs.yawPosition = gyroSimulation.getGyroReading();
      inputs.yawVelocityRadPerSec = gyroSimulation.getMeasuredAngularVelocity().in(RadiansPerSecond);


    inputs.odometryYawTimestamps =
        yawTimestampQueue.stream().mapToDouble((Double value) -> value).toArray();
    inputs.odometryYawPositions =
        yawPositionQueue.stream()
            .map((Double value) -> Rotation2d.fromDegrees(value))
            .toArray(Rotation2d[]::new);
    yawTimestampQueue.clear();
    yawPositionQueue.clear();
  }

}