package frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.hardware.TalonFX;


public class Shooter  extends SubsystemBase {
    private final TalonFX ShooterMotor = new TalonFX(10);
    public final DigitalInput beamBreak = new DigitalInput(0);

private void setVoltage(double voltage){
  SmartDashboard.putBoolean("beam break", beamBreak.get());
  SmartDashboard.putBoolean("beam broken", beamBroken.getAsBoolean());
  
}

   
    public Command runShooter() {
      return Commands.run(
          () -> ShooterMotor.setVoltage(-7),
  
          this
      );
    }
  
  
    public Command runEjectShooter() {
      return Commands.run(
          () -> ShooterMotor.setVoltage(2),
  
          this
      );
    }
  
    public Command stopShooter(){
      return Commands.run(
        () -> ShooterMotor.setVoltage(0),
        this
      );
    }
  
  
    
    public Command smartShooter(){
      return runShooter().until(beamBroken)
      .andThen(runEjectShooter().until(beamNotBroken))
      .andThen(stopShooter());
    }




  public BooleanSupplier beamBroken = () -> !beamBreak.get();
  public BooleanSupplier beamNotBroken = () -> beamBreak.get();


  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}




