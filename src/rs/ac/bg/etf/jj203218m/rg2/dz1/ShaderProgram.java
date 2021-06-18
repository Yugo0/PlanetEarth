package rs.ac.bg.etf.jj203218m.rg2.dz1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

public class ShaderProgram
{
	private int id;

	public ShaderProgram(GLAutoDrawable drawable, String vertexPath, String fragmentPath)
	{
		GL4 gl = drawable.getGL().getGL4();

		String vertexSource = "";
		String fragmentSource = "";

		try
		{
			vertexSource = Files.readString(Paths.get(vertexPath), StandardCharsets.US_ASCII);
			fragmentSource = Files.readString(Paths.get(fragmentPath), StandardCharsets.US_ASCII);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String[] vertexShaderSource = { vertexSource };
		String[] fragmentShaderSource = { fragmentSource };

		IntBuffer status = Buffers.newDirectIntBuffer(1);

		int vertex, fragment;

		vertex = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
		gl.glShaderSource(vertex, 1, vertexShaderSource, null);
		gl.glCompileShader(vertex);

		gl.glGetShaderiv(vertex, GL4.GL_COMPILE_STATUS, status);
		if (status.get(0) == GL4.GL_FALSE)
		{
			System.err.println("FAIL: vertex compilation");

			IntBuffer infoLogLength = Buffers.newDirectIntBuffer(1);
			gl.glGetShaderiv(vertex, GL4.GL_INFO_LOG_LENGTH, infoLogLength);

			ByteBuffer bufferInfoLog = Buffers.newDirectByteBuffer(infoLogLength.get(0));
			gl.glGetShaderInfoLog(vertex, infoLogLength.get(0), null, bufferInfoLog);
			byte[] bytes = new byte[infoLogLength.get(0)];
			bufferInfoLog.get(bytes);
			String strInfoLog = new String(bytes);
			System.err.println(strInfoLog);
		}

		fragment = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
		gl.glShaderSource(fragment, 1, fragmentShaderSource, null);
		gl.glCompileShader(fragment);

		gl.glGetShaderiv(fragment, GL4.GL_COMPILE_STATUS, status);
		if (status.get(0) == GL4.GL_FALSE)
		{
			System.err.println("FAIL: fragment compilation");

			IntBuffer infoLogLength = Buffers.newDirectIntBuffer(1);
			gl.glGetShaderiv(fragment, GL4.GL_INFO_LOG_LENGTH, infoLogLength);

			ByteBuffer bufferInfoLog = Buffers.newDirectByteBuffer(infoLogLength.get(0));
			gl.glGetShaderInfoLog(fragment, infoLogLength.get(0), null, bufferInfoLog);
			byte[] bytes = new byte[infoLogLength.get(0)];
			bufferInfoLog.get(bytes);
			String strInfoLog = new String(bytes);
			System.err.println(strInfoLog);
		}

		id = gl.glCreateProgram();
		gl.glAttachShader(id, vertex);
		gl.glAttachShader(id, fragment);
		gl.glLinkProgram(id);

		gl.glGetShaderiv(id, GL4.GL_LINK_STATUS, status);
		if (status.get(0) == GL4.GL_FALSE)
		{
			System.err.println("FAIL: program linking");

			IntBuffer infoLogLength = Buffers.newDirectIntBuffer(1);
			gl.glGetShaderiv(id, GL4.GL_INFO_LOG_LENGTH, infoLogLength);

			ByteBuffer bufferInfoLog = Buffers.newDirectByteBuffer(infoLogLength.get(0));
			gl.glGetShaderInfoLog(id, infoLogLength.get(0), null, bufferInfoLog);
			byte[] bytes = new byte[infoLogLength.get(0)];
			bufferInfoLog.get(bytes);
			String strInfoLog = new String(bytes);
			System.err.println(strInfoLog);
		}

		gl.glDeleteShader(vertex);
		gl.glDeleteShader(fragment);
	}

	public void setMatrix4f(GLAutoDrawable drawable, String name, Matrix4f matrix)
	{
		GL4 gl = drawable.getGL().getGL4();

		FloatBuffer matrixBuffer = Buffers.newDirectFloatBuffer(16);
		matrix.get(matrixBuffer);
		int location = gl.glGetUniformLocation(id, name);
		gl.glUniformMatrix4fv(location, 1, false, matrixBuffer);
	}

	public void setInt(GLAutoDrawable drawable, String name, int value)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glUniform1i(gl.glGetUniformLocation(id, name), value);
	}

	public void delete(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glDeleteProgram(id);
	}

	public void use(GLAutoDrawable drawable)
	{
		GL4 gl = drawable.getGL().getGL4();

		gl.glUseProgram(id);
	}

	public int getId()
	{
		return id;
	}
}
