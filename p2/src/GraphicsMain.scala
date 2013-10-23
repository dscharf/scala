
import graphics._
import org.lwjgl.LWJGLException
import org.lwjgl.opengl.PixelFormat
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.ContextAttribs
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL11

object GraphicsMain {
  trait Lifecycle {
    def setup()
    def destroy()
  }

  trait Runnable {
    def run(): Boolean
  }

  trait Runstep {
    def step(g: GraphicsLike, time: Float)
  }

  trait Renderable {
    // type Graphics <: GraphicsLike
    def draw(g: GraphicsLike)
  }

  trait Scene extends Lifecycle with Runstep with Renderable {
    def goal(): Boolean
  }

  trait Renderer extends Lifecycle with Runnable with Runstep { this: GraphicsApi =>
    val WIDTH = 800
    val HEIGHT = 600

    var container: List[Module] = List()
    def x(container: List[Module]) {
      this.container = container
    }

    abstract override def setup {
      super.setup()
      println("setup renderer")
      try {
        val pixelFormat = new PixelFormat();
        val contextAtrributes = (new ContextAttribs(3, 2))
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
      graphics.glClearColor(0.4f, 0.6f, 0.9f, 0f);
      graphics.glViewport(0, 0, WIDTH, HEIGHT);

      for (module <- container)
        module.setup

    }

    abstract override def run(): Boolean = {
      val superCondition = super.run()
      step(graphics, 0)

      // Force a maximum FPS of about 60
      display.sync(60);
      // Let the CPU synchronize with the GPU if GPU is tagging behind
      display.update
      superCondition && !display.isCloseRequested
    }

    def step(graphics: GraphicsLike, time: Float) {
      for (module <- container)
        module.draw(this.graphics)
    }

    abstract override def destroy() {

      println("destroy renderer")
      super.destroy()
    }
  }

  trait App extends Lifecycle with Runnable

  trait Module extends Lifecycle with Renderable { this: GraphicsApi => }

  class ModuleBase(x: Int, y: Int) extends Module { this: GraphicsApi =>
    def setup() {}

    def draw(g: GraphicsLike) {}

    def destroy() {}
  }

  trait Module1 extends Module { this: GraphicsApi =>

    var vaoId = 0;
    var vboId = 0;
    var vertexCount = 6;

    abstract override def setup() {
      super.setup();
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

    abstract override def draw(g: GraphicsLike) {
      super.draw(g)
      g.glClear(GL11.GL_COLOR_BUFFER_BIT);

      // Bind to the VAO tha4t has all the information about the quad vertices
      g.glBindVertexArray(vaoId);
      g.glEnableVertexAttribArray(0);

      // Draw the vertices
      g.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

      // Put everything back to default (deselect)
      g.glDisableVertexAttribArray(0);
      g.glBindVertexArray(0);
    }

    abstract override def destroy() {
      super.destroy
    }
  }

  class AppClient extends App {
    def setup() { println("setup app") }
    var t = 1
    def run() = {
      t = t + 1
      if(t % 100 == 0)
        System.err.println(t);
      t < 300
    }
    def destroy { println("destroy app") }
  }

  def main(args: Array[String]) {

    val app = new AppClient with Renderer with OpenglApi 
    
    val m1 = new ModuleBase(0, 0) with Module1 with OpenglApi

    val container: List[Module] = List(m1);
    app.x(container);

    app.setup()
    while (app.run) {}
    app.destroy()
  }

}