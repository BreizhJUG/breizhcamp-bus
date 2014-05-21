package fr.ybonnel.modele;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private StopLine stopline;
	
	private String baseurl;
	
	private List<Line> line;

	public StopLine getStopline() {
		return stopline;
	}

    public void setStopline(StopLine stopline) {
        this.stopline = stopline;
    }

    public String getBaseurl() {
		return baseurl;
	}
	
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	
	public List<Line> getLine() {
		if (line == null) {
			return new ArrayList<Line>();
		}
		return line;
	}
	
	@Override
	public String toString() {
		return "Data [stopline=" + stopline + "]";
	}
	
}
