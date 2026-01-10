# Sensor Integration Strategy

Sensors provide raw data only.
They do not make decisions.

| Sensor | Data | Generated Event |
|------|------|----------------|
| Light Sensor | Lux value | LOW_LIGHT |
| Proximity | Near/Far | DEVICE_COVERED |
| Accelerometer | Movement | POSSIBLE_DISTRACTION |

All decisions are handled by the Context Rule Engine.
