// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import javax.naming.Name;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator.Elevator;
import frc.robot.subsystems.Shooter.Shooter;


public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    public final Elevator m_Elevator = new Elevator();
    public final Shooter m_Shooter = new Shooter();

    



    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);
//joystick is driver, and operator is co driver.
    private final CommandPS5Controller joystick = new CommandPS5Controller(0);
    //private final CommandPS5Controller operator = new CommandPS5Controller(1);
    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();



    public RobotContainer() {
        NamedCommands.registerCommand("Shoot", m_Shooter.runShooter());
        NamedCommands.registerCommand("eject shooter", m_Shooter.runEjectShooter());
        //NamedCommands.registerCommand("Elevator level 1", m_Elevator.());
        configureBindings();

    }



    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        joystick.cross().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.square().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
      //  joystick.povUp().and(joystick.triangle()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
       // joystick.povUp().and(joystick.square()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
       // joystick.povDown().and(joystick.triangle()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        //joystick.povDown().and(joystick.square()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));

        // reset the field-centric heading on left bumper press
        joystick.PS().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);

       // joystick.create().whileTrue(m_Elevator.slot0Configs());
        joystick.R1().whileTrue(m_Shooter.runShooter());
        joystick.povLeft().whileTrue(m_Shooter.runEjectShooter());



        m_Shooter.setDefaultCommand(m_Shooter.stopShooter()); 
        

    }

    
    

  public Command getAutonomousCommand() {
    // This method loads the auto when it is called, however, it is recommended
    // to first load your paths/autos when code starts, then return the
    // pre-loaded auto/path
    return new PathPlannerAuto("Example Auto");
  }
}
    
     

