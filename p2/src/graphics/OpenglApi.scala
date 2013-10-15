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
import java.nio.ByteBuffer
import java.nio.FloatBuffer

package graphics {

  trait OpenglApi extends GraphicsApi {
    def display = new Display
    def graphics = new Graphics
 
    class Display extends DisplayLike {
      def isCloseRequested: Boolean = Display.isCloseRequested()
      def sync(time: Int) = Display.sync(time);
      def setDisplayMode(displayMode: DisplayMode) = Display.setDisplayMode(displayMode);
      def setTitle(name: String) = Display.setTitle(name);
      def update = Display.update()
      def create(pixelFormat: PixelFormat, contextAtrributes: ContextAttribs) = Display.create(pixelFormat, contextAtrributes);
    }

    class Graphics extends GraphicsLike {

      def glViewport(x: Int, y: Int, w: Int, h: Int) = GL11.glViewport(x, y, w, h);
      def glClearColor(r: Float, g: Float, b: Float, a: Float) = GL11.glClearColor(r, g, b, a);

      def glGenVertexArrays: Int = GL30.glGenVertexArrays();
      def glBindVertexArray(vaoId: Int) = GL30.glBindVertexArray(vaoId);

      // Create a new Vertex Buffer Object in memory and select it (bind)
      // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
      def glGenBuffers: Int = GL15.glGenBuffers();
      def glBindBuffer(mode: Int, vboId: Int) = GL15.glBindBuffer(mode, vboId);
      def glBufferData(mode: Int, buffer: FloatBuffer, typ: Int) = GL15.glBufferData(mode, buffer, typ);
      // Put the VBO in the attributes list at index 0
      def glVertexAttribPointer(a: Int, b: Int, c: Int, d: Boolean, e: Int, f: Long) = GL20.glVertexAttribPointer(a, b, c, d, e, f);

      def glClear(mode: Int) = GL11.glClear(mode);

      // Bind to the VAO tha4t has all the information about the quad vertices
      def glEnableVertexAttribArray(typ: Int) = GL20.glEnableVertexAttribArray(typ);

      // Draw the vertices
      def glDrawArrays(mode: Int, buffer: Int, count: Int) = GL11.glDrawArrays(mode, buffer, count);

      // Put everything back to default (deselect)
      def glDisableVertexAttribArray(id: Int) = GL20.glDisableVertexAttribArray(id);

    }
  }

}