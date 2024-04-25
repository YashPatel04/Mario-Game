package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.opengl.GL;

import java.util.Arrays;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //This will give the error if we have one
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize Game Library Frame-Work
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the Window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GFLW window");
        }

        //make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetJoystickCallback(JoystickListener::JoystickCallback);

        //Enable v-sync
        glfwSwapInterval(1);

        //make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)) {
            //poll events
            glfwPollEvents();
            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

            if(glfwJoystickPresent(GLFW_JOYSTICK_1)){
                JoystickListener.updateJoystickStates();
                if(JoystickListener.buttonDown(0)){
                    System.out.println("button pressed.");
                }
            }
            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                System.out.println("Right Click pressed");
            }
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);
        }
    }
}
