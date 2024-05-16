package jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{
    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "void main()\n" +
            "{\n" +
            "    fColor=aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color =  fColor;\n" +
            "}";
    private float[] vertexArray = {
      //pos                  //color                                    // vertex number
      0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, //bottom right     0
      -0.5f, 0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, //top left         1
      0.5f, 0.5f, 0.0f ,     0.0f, 0.0f, 1.0f, 1.0f, //top right        2
      -0.5f, -0.5f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f, //bottom left      3
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
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        //=========================
        //Compile and link shaders
        //=========================

        //First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);

        //Checking for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderi(vertexID,len));
            assert false : "";
        }

        //First load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader source to the GPU
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);

        //Checking for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderi(fragmentID,len));
            assert false : "";
        }

        //Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //Check for errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shaders compilation failed.");
            System.out.println(glGetProgrami(shaderProgram,len));
            assert false:"";
        }
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
        //Bind shader program
        glUseProgram(shaderProgram);
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

        glUseProgram(0);
    }
}
