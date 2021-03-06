package com.base.engine;

public class Camera
{
	public static final Vector3f yAxis = new Vector3f(0,1,0);
	
	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;
	
	private float sensitivity= 0.03f;
	private float movAmt = (float)(10 * Time.getDelta());
	
	private Vector2f desiredAngle;
	private Vector2f angle;
	
	public Camera()
	{
		this(new Vector3f(0,0,0), new Vector3f(0,0,1), new Vector3f(0,1,0));
	}
	
	public Camera(Vector3f pos, Vector3f forward, Vector3f up)
	{
		this.pos = pos;
		this.forward = forward.normalized();
		this.up = up.normalized();
		
		desiredAngle = new Vector2f(0, 0);
		angle = new Vector2f(0, 0);
	}

	boolean mouseLocked = false;
	Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
	
	public void input()
	{
		if(Input.getKey(Input.KEY_ESCAPE))
			grabMouse(false	);
		if(Input.getMouseDown(0))
			grabMouse(true);
		
		if(Input.getKey(Input.KEY_W))
			move(getForward(), movAmt);
		if(Input.getKey(Input.KEY_S))
			move(getForward(), -movAmt);
		if(Input.getKey(Input.KEY_A))
			move(getLeft(), movAmt);
		if(Input.getKey(Input.KEY_D))
			move(getRight(), movAmt);
		if(Input.getKey(Input.KEY_SPACE))
			move(yAxis, movAmt);
		if(Input.getKey(Input.KEY_LSHIFT))
			move(yAxis, -movAmt);
		
		if(mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			desiredAngle = desiredAngle.add(deltaPos);
			
			if(desiredAngle.getY() < -190)
				desiredAngle.setY(-190);
			else if(desiredAngle.getY() > 190)
				desiredAngle.setY(190);
			
			angle = angle.add(desiredAngle.mul(2).sub(angle)).mul(0.1f).mul(sensitivity);
//			
//			System.out.println("Angle" + angle.toString());
//			System.out.println(desiredAngle.toString());
			
			
			forward = new Vector3f((float)Math.cos(angle.getY()) * (float)Math.sin(angle.getX()), (float)Math.sin(angle.getY()), (float)Math.cos(angle.getY()) * (float)Math.cos(angle.getX()));
			Vector3f Haxis = yAxis.cross(forward).normalized();
			up = forward.cross(Haxis).normalized();
				
			if(rotY || rotX)
				Input.setMousePosition(centerPosition);
		}
	}
	
	public void grabMouse(boolean flag)
	{
		mouseLocked = flag;
		
		Input.setMousePosition(centerPosition);
		Input.setCursor(!flag);
	}
	
	public void move(Vector3f dir, float amt)
	{
		pos = pos.add(dir.mul(amt));
	}
	
	public void rotateY(float angle)
	{
		Vector3f Haxis = yAxis.cross(forward).normalized();
		
		forward = forward.rotate(angle, yAxis).normalized();
		
		up = forward.cross(Haxis).normalized();
	}
	
	public void rotateX(float angle)
	{
		Vector3f Haxis = yAxis.cross(forward).normalized();
		
		forward = forward.rotate(angle, Haxis).normalized();
		
		up = forward.cross(Haxis).normalized();
	}
	
	public Vector3f getLeft()
	{
		return forward.cross(up).normalized();
	}
	
	public Vector3f getRight()
	{
		return up.cross(forward).normalized();
	}
	
	public Vector3f getPos()
	{
		return pos;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public Vector3f getForward()
	{
		return forward;
	}

	public void setForward(Vector3f forward)
	{
		this.forward = forward;
	}

	public Vector3f getUp()
	{
		return up;
	}

	public void setUp(Vector3f up)
	{
		this.up = up;
	}

	public float getSensitivity()
	{
		return sensitivity;
	}

	public void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}

	public float getMovAmt()
	{
		return movAmt;
	}

	public void setMovAmt(float movAmt)
	{
		this.movAmt = movAmt;
	}
}
