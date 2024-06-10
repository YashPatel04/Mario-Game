package jade;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{
    private float[] vertexArray = {

            // position               // color
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
            100.5f, 0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0.5f,  100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100.5f,  100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3

    };
    //IMPORTANT: Must be in Anti-clockwise order when writing triangles in element array.
    private int[] elementArray = {
            /*
                x-0       x-1


                x-3       x-2
             */
            2, 1, 0, //top right triangle
            0, 1, 3  //bottom left triangle
    };
    private int vertexID, fragmentID, shaderProgram;
    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        this.camera = new Camera(new Vector2f(-200,-300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        //=============================================================
        //Generate VAO, VBO and EBO buffer objects, and send to the GPU
        //=============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();


        //create vbo
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex and attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeInBytes = 4;
        int vertexSizeBytes = (positionSize+colorSize)*floatSizeInBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes,0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT,false, vertexSizeBytes, positionSize*floatSizeInBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt){
        //change the animation with the object in our coordinate space
        camera.position.x -=dt * 50.0f;
        camera.position.y -=dt * 20.0f;

        //Bind Shader program
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        //bind the VAO that we're using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }
}
