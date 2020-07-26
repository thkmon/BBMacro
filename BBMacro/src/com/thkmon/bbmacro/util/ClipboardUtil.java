package com.thkmon.bbmacro.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardUtil {
	
	
	public static void setClipboard(String str) {
		if (str == null) {
			str = "";
		}

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String copyString = str;
		if (copyString != null) {
			StringSelection contents = new StringSelection(copyString);
			clipboard.setContents(contents, null);
		}
	}
	
	
	public static String getClipboard() {
		String result = "";
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(clipboard);

		if (contents != null) {

			try {
				String pasteString = (String) (contents.getTransferData(DataFlavor.stringFlavor));
				if (pasteString != null && pasteString.length() > 0) {
					result = pasteString;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}