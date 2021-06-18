package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Skybox extends GraphicObject
{
	private int texNum;
	private int textureId;

	private float[] vertices = { -2f, -2f, -2f, 2f / 4, 1f / 3, 2f, -2f, -2f, 1f / 4, 1f / 3, 2f, 2f, -2f, 1f / 4,
			2f / 3, -2f, 2f, -2f, 2f / 4, 2f / 3, -2f, -2f, 2f, 3f / 4, 1f / 3, 2f, -2f, 2f, 4f / 4, 1f / 3, 2f, 2f, 2f,
			4f / 4, 2f / 3, -2f, 2f, 2f, 3f / 4, 2f / 3, -2f, -2f, 2f, 2f / 4, 0f / 3, 2f, -2f, 2f, 1f / 4, 0f / 3, 2f,
			2f, 2f, 1f / 4, 3f / 3, -2f, 2f, 2f, 2f / 4, 3f / 3, 2f, -2f, 2f, 0f / 4, 1f / 3, 2f, 2f, 2f, 0f / 4,
			2f / 3 };
	private int[] indices = { 0, 3, 2, 1, 4, 5, 6, 7, 0, 1, 9, 8, 2, 3, 11, 10, 1, 2, 13, 12, 0, 4, 7, 3 };

	public Skybox(GLAutoDrawable drawable)
	{
		super(drawable, "shaders/skybox_vertex.shader", "shaders/skybox_fragment.shader");
	}

	@Override
	public void initialize(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices, 0);
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertices.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);

		gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 5 * Float.BYTES, 0);
		gl.glEnableVertexAttribArray(0);

		gl.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
		gl.glEnableVertexAttribArray(1);

		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(indices, 0);
		gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indices.length * Integer.BYTES, indexBuffer, GL4.GL_STATIC_DRAW);

		File texFile = new File("img/stars.png");

		Texture texture = null;

		try
		{
			texture = TextureIO.newTexture(texFile, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		textureId = texture.getTextureObject();

		texNum = GraphicObject.getTextureNumber();

		shaderProgram.setInt(drawable, "texture", texNum);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glActiveTexture(GL.GL_TEXTURE + texNum);
		gl.glBindTexture(GL4.GL_TEXTURE_2D, textureId);

		gl.glBindVertexArray(vaoId);
		gl.glDrawElements(GL4.GL_QUADS, 4 * indices.length, GL4.GL_UNSIGNED_INT, 0);
	}

}
