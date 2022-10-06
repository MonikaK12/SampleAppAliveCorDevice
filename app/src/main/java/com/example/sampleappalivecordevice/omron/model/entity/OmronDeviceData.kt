package com.example.sampleappalivecordevice.omron.model.entity

import jp.co.ohq.ble.enumerate.OHQBloodPressureMeasurementStatus
import java.math.BigDecimal
import java.util.*

data class OmronDeviceData(

    /**
     * com.example.sampleappalivecordevice.data.model.fcm.User Index (Type of value : BigDecimal)
     */
    var userIndex: BigDecimal? = null,

    /**
     * Time Stamp (Type of value : String)
     */
    var timeStamp: String? = null,

    /**
     * Sequence Number (Type of value : BigDecimal)
     */
    var sequenceNumber: BigDecimal? = null,

    /**
     * Blood Pressure Unit (Type of value : String, Unit is ["mmHg" or "kPa"])
     */
    var bloodPressureUnit: String? = null,

    /**
     * Systolic Blood Pressure (Type of value : BigDecimal)
     */
    var systolic: BigDecimal? = null,

    /**
     * Diastolic Blood Pressure (Type of value : BigDecimal)
     */
    var diastolic: BigDecimal? = null,

    /**
     * Mean Arterial Pressure (Type of value : BigDecimal)
     */
    var meanArterialPressure: BigDecimal? = null,

    /**
     * Pulse Rate (Type of value : BigDecimal)
     */
    var pulseRate: BigDecimal? = null,

    /**
     * Blood Pressure Measurement status (Type of value : EnumSet<BloodPressureMeasurementStatus>)
     * </BloodPressureMeasurementStatus>
     */
    var bloodPressureMeasurementStatus: EnumSet<OHQBloodPressureMeasurementStatus>? = null,

    /**
     * Weight Unit (Type of value : String, Value is ["kg" or "lb"])
     */
    var weightUnit: String? = null,

    /**
     * Height Unit (Type of value : String, Value is ["m" of "in"])
     */
    var heightUnit: String? = null,

    /**
     * Weight (Type of value : BigDecimal)
     */
    var weight: BigDecimal? = null,

    /**
     * Height (Type of value : BigDecimal)
     */
    var height: BigDecimal? = null,

    /**
     * BMI (Type of value : BigDecimal)
     */
    var bmi: BigDecimal? = null,

    /**
     * Body Fat Percentage (Type of value : BigDecimal)
     */
    var bodyFatPercentage: BigDecimal? = null,

    /**
     * Basal Metabolism (Type of value : BigDecimal, Unit is ["kJ"])
     */
    var basalMetabolism: BigDecimal? = null,

    /**
     * Muscle Percentage (Type of value : BigDecimal)
     */
    var musclePercentage: BigDecimal? = null,

    /**
     * Muscle Mass (Type of value : BigDecimal, Unit is ["kg" or "lb"])
     */
    var muscleMass: BigDecimal? = null,

    /**
     * Fat Free Mass (Type of value : BigDecimal, Unit is ["kg" or "lb"])
     */
    var fatFreeMass: BigDecimal? = null,

    /**
     * Soft Lean Mass (Type of value : BigDecimal, Unit is ["kg" or "lb"])
     */
    var softLeanMass: BigDecimal? = null,

    /**
     * Body Water Mass (Type of value : BigDecimal, Unit is ["kg" or "lb"])
     */
    var bodyWaterMass: BigDecimal? = null,

    /**
     * Impedance (Type of value : BigDecimal, Unit is ["Ω"])
     */
    var impedance: BigDecimal? = null,

    /**
     * Skeletal Muscle Percentage (Type of value : BigDecimal)
     */
    var skeletalMusclePercentage: BigDecimal? = null,

    /**
     * Visceral Fat Level (Type of value : BigDecimal)
     */
    var visceralFatLevel: BigDecimal? = null,

    /**
     * Body Age (Type of value : BigDecimal)
     */
    var bodyAge: BigDecimal? = null,

    /**
     * Body Fat Percentage Stage Evaluation (Type of value : BigDecimal)
     */
    var bodyFatPercentageStageEvaluation: BigDecimal? = null,

    /**
     * Skeletal Muscle Percentage Stage Evaluation (Type of value : BigDecimal)
     */
    var skeletalMusclePercentageStageEvaluation: BigDecimal? = null,

    /**
     * Visceral Fat Level Stage Evaluation (Type of value : BigDecimal)
     */
    var visceralFatLevelStageEvaluation: BigDecimal? = null

)