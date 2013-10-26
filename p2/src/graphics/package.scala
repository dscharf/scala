import org.lwjgl.LWJGLException
import org.lwjgl.opengl.PixelFormat
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.ContextAttribs
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL15
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20

package object graphics {
  def main(args: Array[String]) {
	val app = new App with OpenglApi
	
    app.setupOpengl
    app.setupQuads
    while (app.run) {}
    
  }

  class App { this : GraphicsApi =>

    val WIDTH = 640
    val HEIGHT = 480
    var vaoId = 0
    var vboId = 0
    var vertexCount = 0

    def setupOpengl = {
      try {
        val pixelFormat = new PixelFormat();
        val contextAtrributes = new ContextAttribs(3, 2)
          .withForwardCompatible(true)
          .withProfileCore(true);

        display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        display.setTitle("Test");
        display.create(pixelFormat, contextAtrributes);

        graphics.glViewport(0, 0, WIDTH, HEIGHT);
      } catch {
        case e: LWJGLException => {
          e.printStackTrace();
          System.exit(-1);
        }
      }

      // Setup an XNA like background color
      graphics.glClearColor(0.4f, 0.6f, 0.9f, 0f);

      // Map the internal OpenGL coordinate system to the entire screen
      graphics.glViewport(0, 0, WIDTH, HEIGHT);
    }

    def setupQuads = {
      // OpenGL expects vertices to be defined counter clockwise by default
      val vertices = Array(
        // Left bottom triangle
        -0.5f, 0.5f, 0f,
        -0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        // Right top triangle
        0.5f, -0.5f, 0f,
        0.5f, 0.5f, 0f,
        -0.5f, 0.5f, 0f)
      // Sending data to OpenGL requires the usage of (flipped) byte buffers
      val verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
      verticesBuffer.put(vertices);
      verticesBuffer.flip();

      vertexCount = 6;
 
      // Create a new Vertex Array Object in memory and select it (bind)
      // A VAO can have up to 16 attributes (VBO's) assigned to it by default
      vaoId = graphics.glGenVertexArrays;
      graphics.glBindVertexArray(vaoId);

      // Create a new Vertex Buffer Object in memory and select it (bind)
      // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
      vboId = graphics.glGenBuffers;
      graphics.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
      graphics.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
      // Put the VBO in the attributes list at index 0
      graphics.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
      // Deselect (bind to 0) the VBO
      graphics.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

      // Deselect (bind to 0) the VAO
      graphics.glBindVertexArray(0);
    }
    
    def run = {
      loop

      // Force a maximum FPS of about 60
      display.sync(60);
      // Let the CPU synchronize with the GPU if GPU is tagging behind
      display.update
      !display.isCloseRequested
    }
    
    def loop = {
      graphics.glClear(GL11.GL_COLOR_BUFFER_BIT);

      // Bind to the VAO tha4t has all the information about the quad vertices
      graphics.glBindVertexArray(vaoId);
      graphics.glEnableVertexAttribArray(0);

      // Draw the vertices
      graphics.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

      // Put everything back to default (deselect)
      graphics.glDisableVertexAttribArray(0);
      graphics.glBindVertexArray(0);

    }
  }

}