package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public abstract class GraphicObject
{
	protected ShaderProgram shaderProgram;

	protected IntBuffer vao, vbo, ebo;
	
	public GraphicObject(GLAutoDrawable drawable, String vertexPath, String fragmentPath)
	{
		shaderProgram = new ShaderProgram(drawable, vertexPath, fragmentPath);
	}

	public abstract void init(GLAutoDrawable drawable);

	public abstract void display(GLAutoDrawable drawable);

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
