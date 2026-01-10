# SmartStudyApp – System Architecture

## Architecture Style
The application follows a modular layered architecture.

## Layers
- UI Layer: Activities and Views
- Core Layer: Central coordination and contracts
- Module Layer: Reader, Notes, Sensors, Analytics
- Data Layer: Local storage and database

## Design Principle
Modules do not communicate directly.
All interaction is handled via interfaces defined in the Core layer.

## Benefits
- Loose coupling
- Easy testing
- Clear responsibility separation
