package frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.configs.Slot0Configs;

public class Elevator  extends SubsystemBase {
    final TalonFX m_talonFX = new TalonFX(4);




    
    public Elevator() {
        // in init function, set slot 0 gains
        var slot0Configs = new Slot0Configs();
        slot0Configs.kP = 0.8; // An error of 1 rotation results in 2.4 V output
        slot0Configs.kI = 0.002; // no output for integrated error
        slot0Configs.kD = 0; // A velocity of 1 rps results in 0.1 V output
        slot0Configs.kG = 0.7; // A velocity of 1 rps results in 0.1 V output

        m_talonFX.getConfigurator().apply(slot0Configs);
    }
    final PositionVoltage m_request = new PositionVoltage(0).withSlot(0);

    public Command goToHeight(double heightRotations) {
        // create a position closed-loop request, voltage output, slot 0 configs
        // set position to 10 rotations
        return run(() -> m_talonFX.setControl(m_request.withPosition(heightRotations)));
    }
}
