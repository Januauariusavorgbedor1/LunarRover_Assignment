/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JANUARIUS
 */
public class LunarRover {
     // Enum for Movement Control States
    enum MovementState {
        IDLE,
        ACCELERATING_FORWARD,
        DECELERATING,
        CONSTANT_SPEED,
        REVERSING
    }
    
    // Enum for Camera & Drill States
    enum CameraDrillState {
        IDLE,
        COLOR_CAMERA_MODE,
        MM_CAMERA_MODE,
        DRILL_MODE
    }

    private MovementState currentMovementState;
    private CameraDrillState currentCameraDrillState;
    private long lastLeftPedalPressTime;
    private long lastRightPedalPressTime;
    private long button1PressStartTime;
    private boolean isButton1Pressed;
    private boolean isButton2Pressed;
    private boolean isDrillOn;

    public LunarRover() {
        // Initial states for both movement and camera/drill
        currentMovementState = MovementState.IDLE;
        currentCameraDrillState = CameraDrillState.IDLE;
        isButton1Pressed = false;
        isButton2Pressed = false;
        isDrillOn = false;
    }

    // --- Movement Control Methods ---
    
    public void pressLeftPedal() {
        long currentTime = System.currentTimeMillis();

        switch (currentMovementState) {
            case IDLE:
                currentMovementState = MovementState.ACCELERATING_FORWARD;
                System.out.println("Movement State: Accelerating Forward");
                break;
            case ACCELERATING_FORWARD:
                System.out.println("Movement State: Accelerating Forward");
                break;
            case CONSTANT_SPEED:
                break;
            case REVERSING:
                break;
        }
        lastLeftPedalPressTime = currentTime;
    }

    public void pressRightPedal() {
        long currentTime = System.currentTimeMillis();

        switch (currentMovementState) {
            case ACCELERATING_FORWARD:
                currentMovementState = MovementState.DECELERATING;
                System.out.println("Movement State: Decelerating");
                break;
            case DECELERATING:
                currentMovementState = MovementState.IDLE;
                System.out.println("Movement State: Idle");
                break;
            case CONSTANT_SPEED:
                break;
            case IDLE:
                break;
        }
        lastRightPedalPressTime = currentTime;
    }

    public void holdRightPedal() {
        if (currentMovementState == MovementState.ACCELERATING_FORWARD) {
            currentMovementState = MovementState.CONSTANT_SPEED;
            System.out.println("Movement State: Constant Speed");
        }
    }

    public void holdLeftPedal() {
        if (currentMovementState == MovementState.IDLE) {
            currentMovementState = MovementState.REVERSING;
            System.out.println("Movement State: Reversing");
        }
    }
    
    // --- Camera & Drill Control Methods ---

    public void pressButton1() {
        if (!isButton1Pressed) {
            button1PressStartTime = System.currentTimeMillis();
            isButton1Pressed = true;
        }
    }

    public void releaseButton1() {
        if (isButton1Pressed) {
            long pressDuration = System.currentTimeMillis() - button1PressStartTime;
            isButton1Pressed = false;
            handleButton1Release(pressDuration);
        }
    }

    public void pressButton2() {
        isButton2Pressed = true;
        handleButton2Press();
    }

    public void releaseButton2() {
        isButton2Pressed = false;
    }

    // Handle Button 1 release for camera/drill modes
    private void handleButton1Release(long pressDuration) {
        switch (currentCameraDrillState) {
            case IDLE:
                if (pressDuration >= 5000 && pressDuration < 10000) {
                    currentCameraDrillState = CameraDrillState.COLOR_CAMERA_MODE;
                    System.out.println("Camera State: Color Camera Mode (Press Button 1 for 5 seconds)");
                } else if (pressDuration >= 10000) {
                    currentCameraDrillState = CameraDrillState.MM_CAMERA_MODE;
                    System.out.println("Camera State: 16mm Camera Mode (Press Button 1 for 10 seconds)");
                }
                break;
            case COLOR_CAMERA_MODE:
            case MM_CAMERA_MODE:
                // Take picture or activate temporizer
                if (pressDuration >= 5000) {
                    System.out.println("Taking picture / Activating Temporizer (Moon Selfies)");
                } else {
                    System.out.println("Taking picture");
                }
                break;
            case DRILL_MODE:
                // Button 1 toggles the drill state
                toggleDrill();
                break;
        }
    }

    // Handle Button 2 press (exit modes)
    private void handleButton2Press() {
        switch (currentCameraDrillState) {
            case COLOR_CAMERA_MODE:
            case MM_CAMERA_MODE:
            case DRILL_MODE:
                currentCameraDrillState = CameraDrillState.IDLE;
                System.out.println("Camera State: Idle (Exiting current mode)");
                break;
            default:
                System.out.println("Already in Idle Camera State");
                break;
        }
    }

    // Toggle the drill on or off
    private void toggleDrill() {
        isDrillOn = !isDrillOn;
        if (isDrillOn) {
            System.out.println("Drill State: Drill ON");
        } else {
            System.out.println("Drill State: Drill OFF");
        }
    }

    // Debug: Display current states for both movement and camera/drill
    public void printCurrentState() {
        System.out.println("Movement State: " + currentMovementState);
        System.out.println("Camera/Drill State: " + currentCameraDrillState);
    }

    // Main method for testing
    public static void main(String[] args) throws InterruptedException {
        LunarRover rover = new LunarRover();

        // --- Movement Control Test ---
        System.out.println("Testing Movement Control...");
        rover.pressLeftPedal();
        Thread.sleep(2000);
        rover.pressRightPedal();
        Thread.sleep(2000);
        rover.holdRightPedal();
        Thread.sleep(3000);
        rover.holdLeftPedal();
        rover.printCurrentState();

        // --- Camera & Drill Control Test ---
        System.out.println("\nTesting Camera & Drill Control...");
        rover.pressButton1();  // Press for 5 seconds to go to Color Camera Mode
        Thread.sleep(6000);
        rover.releaseButton1();  // Release after 6 seconds
        rover.printCurrentState();

        rover.pressButton1();  // Press for 10 seconds to go to 16mm Camera Mode
        Thread.sleep(11000);
        rover.releaseButton1();  // Release after 11 seconds
        rover.printCurrentState();

        rover.pressButton1();  // Double press for Drill Mode
        rover.pressButton1();
        rover.releaseButton1();
        rover.printCurrentState();

        rover.pressButton2();  // Exit to Idle state
        rover.printCurrentState();
    }
}
    

