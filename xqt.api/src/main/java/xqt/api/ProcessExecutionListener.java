package xqt.api;

import xqt.model.data.Variable;

public interface ProcessExecutionListener {
	public void executing();
	public void executed(StringBuilder report);
	public void present(Variable v);
	public void draw(Variable v);
}
