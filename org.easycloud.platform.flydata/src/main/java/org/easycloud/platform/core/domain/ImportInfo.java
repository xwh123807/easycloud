package org.easycloud.platform.core.domain;

import java.util.ArrayList;
import java.util.List;

public class ImportInfo {
	private int totalCount;
	private int successCount;
	private int usedTime;
	private List<String> errorLines = new ArrayList<>();
	private String message;

	public ImportInfo() {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getErrorCount() {
		return getErrorLines().size();
	}

	public List<String> getErrorLines() {
		return errorLines;
	}

	public void setErrorLines(List<String> errorLines) {
		this.errorLines = errorLines;
	}

	public void addErrorLine(String line, String message) {
		errorLines.add(line + "," + message);
	}

	public void incSuccess() {
		successCount++;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(int usedTime) {
		this.usedTime = usedTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
