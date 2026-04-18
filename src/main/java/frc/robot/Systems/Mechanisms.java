package frc.robot.Systems;

import java.util.HashMap;
import java.util.Map;

import org.littletonrobotics.junction.Logger;

import frc.robot.Constants.Mode;
import frc.robot.InputOutput.MechanismIO;
import frc.robot.InputOutput.MechanismIOInputsAutoLogged;

import frc.robot.SimInstance.IntakeSim;

public class Mechanisms {

    // To prevent Mistakes in spelling
    private String INTAKE = "INTAKE";
    private String SHOOTER = "SHOOTER";

    // Holds all Mechanisms io and inputs
    private Map<String , System> mechanismsMap = new HashMap<>();

    // Common class to group io and inputs 
    class System {
        MechanismIO io;
        MechanismIOInputsAutoLogged inputs;

        public System(MechanismIO io,MechanismIOInputsAutoLogged inputs) {
            this.io = io;
            this.inputs = inputs;
        }
    }


    

    /* Constructer */
    public Mechanisms(Mode currentMode) {

        switch (currentMode) {

            case SIM:
                mechanismsMap.put(INTAKE,new System(
                    new IntakeSim(),                    // Indepedent: Type Changes with mode
                    new MechanismIOInputsAutoLogged() ) ); // Dependent: Type Doesnt Change with Mode
            break; // End of Sim Case

            default: // Replay Case
                mechanismsMap.put(INTAKE,new System(
                    new MechanismIO() {},
                    new MechanismIOInputsAutoLogged() ) );

                mechanismsMap.put(SHOOTER,new System(
                    new MechanismIO() {},
                    new MechanismIOInputsAutoLogged() ) );
            break; // End of Default Case

        }


    }

    public void periodic() {

        // For every Component in Mechanisms, UpdateInputs()
        for (String key : mechanismsMap.keySet()) {
            var currentSystem = mechanismsMap.get(key);

            currentSystem.io.updateInputs(currentSystem.inputs);
            Logger.processInputs(key,currentSystem.inputs);
        } // End of Loop


    }
}