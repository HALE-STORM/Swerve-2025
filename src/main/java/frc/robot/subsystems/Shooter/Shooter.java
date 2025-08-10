package frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;


import com.ctre.phoenix6.hardware.TalonFX;


public class Shooter  extends SubsystemBase {
    private final TalonFX ShooterMotor = new TalonFX(10);
    public final DigitalInput beamBreak = new DigitalInput(0);

   public Command runShooter() {
    return Commands.run(
        () -> ShooterMotor.setVoltage(SmartDashboard.getNumber("Shoot Voltage", 4)),

        this
    );
  }

  public Command runShooterPath() {
    return Commands.run(
        () -> ShooterMotor.setVoltage(SmartDashboard.getNumber("Shoot Voltage", 4)),

        this
    );
  }

  public Command stopShooter(){
    return Commands.run(
    () -> ShooterMotor.setVoltage(0),

    this
    );

    

  }
}



