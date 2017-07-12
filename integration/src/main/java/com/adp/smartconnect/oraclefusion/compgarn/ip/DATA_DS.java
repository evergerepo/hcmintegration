package com.adp.smartconnect.oraclefusion.compgarn.ip;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DATA_DS")
@XmlAccessorType (XmlAccessType.FIELD)
public class DATA_DS {
	
	@XmlElement(name="G_5")
	private List<G_5> G_5;

	public List<G_5> getG_5() {
		return G_5;
	}

	public void setG_5(List<G_5> g_5) {
		G_5 = g_5;
	}

	
	

}
