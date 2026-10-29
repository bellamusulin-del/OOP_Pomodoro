# OOP_Pomodoro
Pomodoro Timer (Pomodoro_bónuszházik)A Desktop Pomodoro timer application built in Java using Swing and the MVC (Model-View-Controller) pattern with the Observer design pattern. It features a customized pink UI theme, cute image mascots, daily stats tracking, and audio/tray notifications.   Key FeaturesClassic Pomodoro Technique: 25-minute work intervals, 5-minute short breaks, and long breaks after 4 cycles.   Clean Architecture: Decoupled domain logic (pomodoro.model) with zero Swing dependencies.   Custom Pink UI: Hot pink theme with custom styled buttons (RozsaGomb) and mascot PNG assets.   Daily Statistics: Tracks completed sessions in statisztika.txt and renders a bar chart (StatisztikaPanel).   System Integration: Tray icon displaying remaining time and sound notifications (Toolkit.beep()) on state transitions.   Project Structuresrc/main/java/pomodoro/
├── Fo.java                     # Main entry point
├── model/                      # Core logic & state management (PomodoroModel, NapiStatisztika, etc.)
├── view/                       # Swing UI components (PomodoroView, StatisztikaDialog, RozsaGomb, etc.)
└── controller/                 # Action listeners & javax.swing.Timer logic

src/main/resources/
└── kepek/                      # UI icons and mascot images

docs/
├── osztalydiagram.puml         # PlantUML class diagram
└── screenshots/                # Application screenshots (work, break, settings, stats)
```[cite: 3]

---

## Requirements

* **Java**: JDK 17 or higher[cite: 2, 3]
* **Build Tool**: Apache Maven[cite: 2, 3]

---

## Getting Started

### 1. Build and Run
Execute the application using Maven[cite: 3]:

```bash
mvn compile exec:java
```[cite: 3]

> **NetBeans**: Open Project $\rightarrow$ Navigate to `Fo.java` $\rightarrow$ Select **Run File**[cite: 3].

### 2. Run Tests
Run the JUnit 5 test suite covering the model and statistical calculations (UI independent)[cite: 2, 3]:

```bash
mvn test
```[cite: 3]
