package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public abstract class GraphicObject
{
	protected ShaderProgram shaderProgram;

	protected IntBuffer vao, vbo, ebo;
	protected int vaoId, vboId, eboId;
	
	private static int textureNumber = 0;
	
	public GraphicObject(GLAutoDrawable drawable, String vertexPath, String fragmentPath)
	{
		shaderProgram = new ShaderProgram(drawable, vertexPath, fragmentPath);
	}
	
	public abstract void initialize(GLAutoDrawable drawable);

	public abstract void display(GLAutoDrawable drawable);

	public static int getTextureNumber()
	{
		return textureNumber++;
	}
	
	public void init(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		vao = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, vao);
		vaoId = vao.get(0);
		gl.glBindVertexArray(vaoId);

		vbo = IntBuffer.allocate(1);
		gl.glGenBuffers(1, vbo);
		vboId = vbo.get(0);
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vboId);

		ebo = IntBuffer.allocate(1);
		gl.glGenBuffers(1, ebo);
		eboId = ebo.get(0);
		gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, eboId);

		initialize(drawable);

		shaderProgram.use(drawable);
	}

	public void dispose(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		if (vao != null)
		{
			gl.glDeleteVertexArrays(1, vao);
		}
		if (vbo != null)
		{
			gl.glDeleteBuffers(1, vbo);
		}
		if (ebo != null)
		{
			gl.glDeleteBuffers(1, ebo);
		}
		shaderProgram.delete(drawable);
	}

	public ShaderProgram getShaderProgram()
	{
		return shaderProgram;
	}
}
