package frc.robot.Systems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.littletonrobotics.junction.Logger;

import static frc.robot.Constants.Mode;

import frc.robot.InputOutput.MechanismIO;
import frc.robot.InputOutput.MechanismIOInputsAutoLogged;

import frc.robot.SimInstance.IntakeSim;

public class Mechanisms {

    // To prevent Mistakes in spelling
    private String INTAKE = "INTAKE";
    private String SHOOTER = "SHOOTER";

    // Common class to group io and inputs 
    class System {
        MechanismIO io;
        MechanismIOInputsAutoLogged inputs;

        public System(MechanismIO io,MechanismIOInputsAutoLogged inputs) {
            this.io = io;
            this.inputs = inputs;
        }
    }

    // Holds all Mechanisms io and inputs
    Map<String , System> mechanismsMap = new HashMap<>();
    

    /* Constructer */
    public Mechanisms(Mode currentMode) {

        switch (currentMode) {

            case SIM:
                mechanismsMap.put(INTAKE,new System(
                    new IntakeSim() {},                    // Indepedent: Type Changes with mode
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

        // For every Component in Mechanisms, UpdateInputs() until hasNext() is false
        for (Iterator<String> i = mechanismsMap.keySet().iterator(); i.hasNext();) {
            var currentSystem = mechanismsMap.get(i.toString());

            currentSystem.io.updateInputs(currentSystem.inputs);
            Logger.processInputs(i.toString(),currentSystem.inputs);
        } // End of Loop


    }
}