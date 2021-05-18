package com.cn.cd.ruidiculous.company.speark_version_2_application;

public class AngleBean {
	
	int  Angle;//角度值
	
	boolean	Direction; // 正true   负false  方向

	public int getAngle() {
		return Angle;
	}

	public void setAngle(int angle) {
		Angle = angle;
	}

	public boolean isDirection() {
		return Direction;
	}

	public void setDirection(boolean direction) {
		Direction = direction;
	}

	@Override
	public String toString() {
		return "AngleBean [Angle=" + Angle + ", Direction=" + Direction + "]";
	}
	

}
