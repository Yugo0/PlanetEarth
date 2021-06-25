package rs.ac.bg.etf.jj203218m.rg2.dz1;

import org.joml.Vector3f;

public class FloatKey
{
	private static final float TRESHOLD = 0.00001f;

	public float first;
	public float second;
	public float third;
	public float fourth;
	public float fifth;

	public FloatKey(float first, float second, float third, float fourth, float fifth)
	{
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
	}

	public FloatKey(Vector3f vector, float fourth, float fifth)
	{
		this.first = vector.x;
		this.second = vector.y;
		this.third = vector.z;
		this.fourth = fourth;
		this.fifth = fifth;
	}

	@Override
	public int hashCode()
	{
		return Float.hashCode(first) ^ Float.hashCode(second) ^ Float.hashCode(third) ^ Float.hashCode(fourth)
				^ Float.hashCode(fifth);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof FloatKey))
		{
			return false;
		}
		if (Math.abs(this.first - ((FloatKey) obj).first) < TRESHOLD
				&& Math.abs(this.second - ((FloatKey) obj).second) < TRESHOLD
				&& Math.abs(this.third - ((FloatKey) obj).third) < TRESHOLD
				&& Math.abs(this.fourth - ((FloatKey) obj).fourth) < TRESHOLD
				&& Math.abs(this.fifth - ((FloatKey) obj).fifth) < TRESHOLD)
		{
			return true;
		}
		return false;
	}

}
