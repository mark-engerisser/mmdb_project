package de.ur.mi.mmdb_project;

public class Task {
	public int m_Id;
	public int m_Driver;
	public int m_Src;
	public int m_Dest;
	public int m_Status;
	
	public Task(int id, int driver, int src, int dest, int status) {
		m_Id = id;
		m_Driver = driver;
		m_Src = src;
		m_Dest = dest;
		m_Status = status;
	}
}
