import java.nio.FloatBuffer

import org.lwjgl.opengl.ContextAttribs
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.PixelFormat

package graphics {

  trait GraphicsApi {
    type Display <: DisplayLike
    type Graphics <: GraphicsLike

    def display(): Display
    def graphics(): Graphics
  }

  trait DisplayLike {
    def isCloseRequested: Boolean
    def sync(time: Int)
    def setDisplayMode(displayMode: DisplayMode)
    def setTitle(name: String)
    def update
    def create(pixelFormat: PixelFormat, contextAtrributes: ContextAttribs)
  }

  trait GraphicsLike {
    def glViewport(x: Int, y: Int, w: Int, h: Int)
    def glClearColor(r: Float, g: Float, b: Float, a: Float)
    def glGenVertexArrays: Int
    def glBindVertexArray(vaoId: Int)
    def glGenBuffers: Int
    def glBindBuffer(mode: Int, vboId: Int)
    def glBufferData(mode: Int, buffer: FloatBuffer, typ: Int)
    def glVertexAttribPointer(a: Int, b: Int, c: Int, d: Boolean, e: Int, f: Long)
    def glClear(mode: Int)
    def glEnableVertexAttribArray(typ: Int)
    def glDrawArrays(mode: Int, buffer: Int, count: Int)
    def glDisableVertexAttribArray(id: Int)
  }
}