package jade;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;

public class JoystickListener {
    private static JoystickListener instance;
    private ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
    private FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
    private ByteBuffer hats = glfwGetJoystickHats(GLFW_JOYSTICK_1);
    private int[] buttonsValues = convertToIntArray(buttons);
    private Float[] axisValues = convertToFloatArray(axes);
    private int[] hatValues = convertToIntArray(hats);
    private String joystickName;
    private int[] convertToIntArray(ByteBuffer arr){
        // Check if arr is null before accessing its methods
        if (arr != null) {
            int[] intArray = new int[arr.limit() / 4];
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = arr.getInt();
            }
            return intArray;
        } else {
            // Handle the case where arr is null
            System.err.println("ByteBuffer arr is null!");
            return new int[0]; // Return an empty array or handle as appropriate
        }
    }
    private Float[] convertToFloatArray(FloatBuffer arr){
        if (arr != null){
            Float[] FlArr = new Float[arr.limit()/4];
            for (int i = 0; i < FlArr.length; i++) {
                FlArr[i] = arr.get();
            }
            return FlArr;
        }
        else{
            System.err.println("Buffer arr is null!");
            return new Float[0]; // Return an empty array or handle as appropriate
        }
    }
    public static void updateJoystickStates(){
        get().buttonsValues = get().convertToIntArray(glfwGetJoystickButtons(GLFW_JOYSTICK_1));
        get().axisValues = get().convertToFloatArray(glfwGetJoystickAxes(GLFW_JOYSTICK_1));
        get().hatValues = get().convertToIntArray(glfwGetJoystickHats(GLFW_JOYSTICK_1));
        get().joystickName = glfwGetJoystickName(GLFW_JOYSTICK_1);
    }
    public JoystickListener(){
        joystickName = glfwGetJoystickName(GLFW_JOYSTICK_1);
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
        }else if (event == GLFW_DISCONNECTED) {
            System.out.println("Joystick "+ get().joystickName +" Disconnected.");
        }
    }
    public int[] getButtonsValues() {
        return buttonsValues;
    }

    public Float[] getAxisValues() {
        return axisValues;
    }

    public int[] getHatValues() {
        return hatValues;
    }
    public static Boolean buttonDown(int b){
        if(get().buttonsValues[b] == GLFW_PRESS && b<4){
            //System.out.println(Arrays.toString(get().getButtonsValues()));
            return true;
        }
        return false;
    }
}
