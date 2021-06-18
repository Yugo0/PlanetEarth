package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public class Skybox extends GraphicObject
{
	float[] vertices = { -3f, -3f, -3f, 3f, -3f, -3f, 3f, 3f, -3f, -3f, 3f, -3f, -3f, -3f, 3f, 3f, -3f, 3f, 3f, 3f, 3f,
			-3f, 3f, 3f };
	int[] indices = { 0, 3, 2, 1, 4, 5, 6, 7, 0, 1, 5, 4, 2, 3, 7, 6, 1, 2, 6, 5, 0, 4, 7, 3 };

	public Skybox(GLAutoDrawable drawable)
	{
		super(drawable, "shaders/skybox_vertex.shader", "shaders/skybox_fragment.shader");
	}

	@Override
	public void initializeBuffers(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices, 0);
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

		gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 3 * Float.BYTES, 0);
		gl.glEnableVertexAttribArray(0);

		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(indices, 0);
		gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * Integer.BYTES, indexBuffer, GL4.GL_STATIC_DRAW);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		shaderProgram.use(drawable);

		gl.glBindVertexArray(vaoId);
		gl.glDrawElements(GL4.GL_QUADS, 4 * indices.length, GL4.GL_UNSIGNED_INT, 0);
	}

}
