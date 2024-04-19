package jade;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;

public class JoystickListener {
    private static JoystickListener instance;
    private Boolean present;
    private ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
    private FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
    private ByteBuffer hats = glfwGetJoystickHats(GLFW_JOYSTICK_1);
    private int[] buttonsValues = convertToIntArray(buttons);
    private Float[] axisValues = convertToFloatArray(axes);
    private int[] hatValues = convertToIntArray(hats);
    private String joystickName;
    private int[] convertToIntArray(ByteBuffer arr){
        int size = arr.limit();
        int[] res = new int[size/4];
        arr.flip();
        for (int i = 0; i < res.length; i++) {
            res[i] = arr.getInt(); // Read an integer from the buffer and put it into the array
        }
        return res;
    }
    private Float[] convertToFloatArray(FloatBuffer arr){
        int size = arr.limit();
        Float[] res = new Float[size/4];
        arr.flip();
        for (int i = 0; i < res.length; i++) {
            res[i] = arr.get(); // Read a float from the buffer and put it into the array
        }
        return res;
    }
    public static void updateJoystickStates(){
        get().buttonsValues = get().convertToIntArray(glfwGetJoystickButtons(GLFW_JOYSTICK_1));
        get().axisValues = get().convertToFloatArray(glfwGetJoystickAxes(GLFW_JOYSTICK_1));
        get().hatValues = get().convertToIntArray(glfwGetJoystickHats(GLFW_JOYSTICK_1));
        get().joystickName = glfwGetJoystickName(GLFW_JOYSTICK_1);
    }
    public JoystickListener(){
        get().joystickName = glfwGetJoystickName(GLFW_JOYSTICK_1);
        if(glfwJoystickPresent(GLFW_JOYSTICK_1)){
            get().present = true;
        }
        else{
            get().present = false;
        }
    }
    public static JoystickListener get(){
        if (instance == null){
            instance = new JoystickListener();
        }
        return instance;
    }
    public static void JoystickCallback(int GLFW_JOYSTICK_1, int event){
        if(event != GLFW_DISCONNECTED) {
            System.out.println("Joystick "+ get().joystickName +" Present.");
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            get().updateJoystickStates();
                        }
                    },
                    0,      // run first occurrence immediately
                    0001    // run every one millisecond
            ); }else if (event == GLFW_DISCONNECTED) {
            System.out.println("Joystick "+ get().joystickName +" Disconnected.");
        }
    }
}
