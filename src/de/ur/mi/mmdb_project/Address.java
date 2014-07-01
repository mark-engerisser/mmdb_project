package de.ur.mi.mmdb_project;

public class Address {
	public int m_Avenue;
	public int m_Street;
	public int m_Id;
	
	public Address(int id, int avenue, int street) {
		m_Id = id;
		m_Avenue = avenue;
		m_Street = street;
	}
	
	public String toAddressString() {
		String finalString = "";
		
		finalString += String.valueOf(m_Avenue);
		
		switch (m_Avenue) {
		case 1:
			finalString += "st";
			break;
		case 2:
			finalString += "nd";
			break;
		case 3:
			finalString += "rd";
			break;
		default:
			finalString += "th";
		}
		
		finalString += " Av./";
		finalString += String.valueOf(m_Street);
		
		switch (m_Street) {
		case 1:
			finalString += "st";
			break;
		case 2:
			finalString += "nd";
			break;
		case 3:
			finalString += "rd";
			break;
		default:
			finalString += "th";
		}
		
		finalString += " St.";
		
		return finalString;
	}
	
	
}
