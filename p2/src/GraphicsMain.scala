
import graphics._
import org.lwjgl.LWJGLException
import org.lwjgl.opengl.PixelFormat
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.ContextAttribs

object GraphicsMain {
  trait Lifecycle {
    def setup()
    def destroy()
  }

  trait Runnable {
    def run(): Boolean
  }

  trait Runstep {
    def step(time: Float)
  }

  trait Renderable {
    type Graphics <: GraphicsLike
    def draw(g: Graphics)
  }

  trait Scene extends Lifecycle with Runstep with Renderable {
    def goal(): Boolean
  }

  trait Renderer extends Lifecycle with Runnable with Runstep { this: GraphicsApi =>
    val WIDTH = 800
    val HEIGHT = 600
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
    }

    abstract override def run(): Boolean = {
      val superCondition = super.run()
      step(0)

      // Force a maximum FPS of about 60
      display.sync(60);
      // Let the CPU synchronize with the GPU if GPU is tagging behind
      display.update
      superCondition && !display.isCloseRequested
    }
    
    def step(time: Float) {}
    abstract override def destroy() {
      
      println("destroy renderer")
      super.destroy()
    }
  }

  trait App extends Lifecycle with Runnable

  class AppClient extends App { 
    def setup() {println("setup app")}
    var t = 1
    def run() = {
      t = t + 1
      t < 300
    }
    def destroy {println("destroy app")}
  }

  def main(args: Array[String]) {
    val app = new AppClient with Renderer with OpenglApi

    app.setup()
    while (app.run) {}
    app.destroy()
  }

}