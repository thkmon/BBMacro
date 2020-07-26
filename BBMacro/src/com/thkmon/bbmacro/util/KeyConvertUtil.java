package com.thkmon.bbmacro.util;

import java.awt.event.KeyEvent;

public class KeyConvertUtil {
	
	public static int getKeycode(String str) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		
		int result = -1;
		
		if (checkIsSameKeycode(str, "VK_ENTER")) {result = KeyEvent.VK_ENTER;}
	    else if (checkIsSameKeycode(str, "VK_BACK_SPACE")) {result = KeyEvent.VK_BACK_SPACE;}
	    else if (checkIsSameKeycode(str, "VK_TAB")) {result = KeyEvent.VK_TAB;}
	    else if (checkIsSameKeycode(str, "VK_CANCEL")) {result = KeyEvent.VK_CANCEL;}
	    else if (checkIsSameKeycode(str, "VK_CLEAR")) {result = KeyEvent.VK_CLEAR;}
		else if (checkIsSameKeycode(str, "VK_SHIFT")) {result = KeyEvent.VK_SHIFT;}
		else if (checkIsSameKeycode(str, "VK_CONTROL")) {result = KeyEvent.VK_CONTROL;}
		else if (checkIsSameKeycode(str, "VK_ALT")) {result = KeyEvent.VK_ALT;}
		else if (checkIsSameKeycode(str, "VK_PAUSE")) {result = KeyEvent.VK_PAUSE;}
		else if (checkIsSameKeycode(str, "VK_CAPS_LOCK")) {result = KeyEvent.VK_CAPS_LOCK;}
		else if (checkIsSameKeycode(str, "VK_ESCAPE")) {result = KeyEvent.VK_ESCAPE;}
		else if (checkIsSameKeycode(str, "VK_SPACE")) {result = KeyEvent.VK_SPACE;}
		else if (checkIsSameKeycode(str, "VK_PAGE_UP")) {result = KeyEvent.VK_PAGE_UP;}
		else if (checkIsSameKeycode(str, "VK_PAGE_DOWN")) {result = KeyEvent.VK_PAGE_DOWN;}
		else if (checkIsSameKeycode(str, "VK_END")) {result = KeyEvent.VK_END;}
		else if (checkIsSameKeycode(str, "VK_HOME")) {result = KeyEvent.VK_HOME;}
		else if (checkIsSameKeycode(str, "VK_LEFT")) {result = KeyEvent.VK_LEFT;}
		else if (checkIsSameKeycode(str, "VK_UP")) {result = KeyEvent.VK_UP;}
		else if (checkIsSameKeycode(str, "VK_RIGHT")) {result = KeyEvent.VK_RIGHT;}
		else if (checkIsSameKeycode(str, "VK_DOWN")) {result = KeyEvent.VK_DOWN;}
		else if (checkIsSameKeycode(str, "VK_COMMA")) {result = KeyEvent.VK_COMMA;}
		else if (checkIsSameKeycode(str, "VK_MINUS")) {result = KeyEvent.VK_MINUS;}
		else if (checkIsSameKeycode(str, "VK_PERIOD")) {result = KeyEvent.VK_PERIOD;}
		else if (checkIsSameKeycode(str, "VK_SLASH")) {result = KeyEvent.VK_SLASH;}
		else if (checkIsSameKeycode(str, "VK_0")) {result = KeyEvent.VK_0;}
		else if (checkIsSameKeycode(str, "VK_1")) {result = KeyEvent.VK_1;}
		else if (checkIsSameKeycode(str, "VK_2")) {result = KeyEvent.VK_2;}
		else if (checkIsSameKeycode(str, "VK_3")) {result = KeyEvent.VK_3;}
		else if (checkIsSameKeycode(str, "VK_4")) {result = KeyEvent.VK_4;}
		else if (checkIsSameKeycode(str, "VK_5")) {result = KeyEvent.VK_5;}
		else if (checkIsSameKeycode(str, "VK_6")) {result = KeyEvent.VK_6;}
		else if (checkIsSameKeycode(str, "VK_7")) {result = KeyEvent.VK_7;}
		else if (checkIsSameKeycode(str, "VK_8")) {result = KeyEvent.VK_8;}
		else if (checkIsSameKeycode(str, "VK_9")) {result = KeyEvent.VK_9;}
		else if (checkIsSameKeycode(str, "VK_SEMICOLON")) {result = KeyEvent.VK_SEMICOLON;}
		else if (checkIsSameKeycode(str, "VK_EQUALS")) {result = KeyEvent.VK_EQUALS;}
		else if (checkIsSameKeycode(str, "VK_A")) {result = KeyEvent.VK_A;}
		else if (checkIsSameKeycode(str, "VK_B")) {result = KeyEvent.VK_B;}
		else if (checkIsSameKeycode(str, "VK_C")) {result = KeyEvent.VK_C;}
		else if (checkIsSameKeycode(str, "VK_D")) {result = KeyEvent.VK_D;}
		else if (checkIsSameKeycode(str, "VK_E")) {result = KeyEvent.VK_E;}
		else if (checkIsSameKeycode(str, "VK_F")) {result = KeyEvent.VK_F;}
		else if (checkIsSameKeycode(str, "VK_G")) {result = KeyEvent.VK_G;}
		else if (checkIsSameKeycode(str, "VK_H")) {result = KeyEvent.VK_H;}
		else if (checkIsSameKeycode(str, "VK_I")) {result = KeyEvent.VK_I;}
		else if (checkIsSameKeycode(str, "VK_J")) {result = KeyEvent.VK_J;}
		else if (checkIsSameKeycode(str, "VK_K")) {result = KeyEvent.VK_K;}
		else if (checkIsSameKeycode(str, "VK_L")) {result = KeyEvent.VK_L;}
		else if (checkIsSameKeycode(str, "VK_M")) {result = KeyEvent.VK_M;}
		else if (checkIsSameKeycode(str, "VK_N")) {result = KeyEvent.VK_N;}
		else if (checkIsSameKeycode(str, "VK_O")) {result = KeyEvent.VK_O;}
		else if (checkIsSameKeycode(str, "VK_P")) {result = KeyEvent.VK_P;}
		else if (checkIsSameKeycode(str, "VK_Q")) {result = KeyEvent.VK_Q;}
		else if (checkIsSameKeycode(str, "VK_R")) {result = KeyEvent.VK_R;}
		else if (checkIsSameKeycode(str, "VK_S")) {result = KeyEvent.VK_S;}
		else if (checkIsSameKeycode(str, "VK_T")) {result = KeyEvent.VK_T;}
		else if (checkIsSameKeycode(str, "VK_U")) {result = KeyEvent.VK_U;}
		else if (checkIsSameKeycode(str, "VK_V")) {result = KeyEvent.VK_V;}
		else if (checkIsSameKeycode(str, "VK_W")) {result = KeyEvent.VK_W;}
		else if (checkIsSameKeycode(str, "VK_X")) {result = KeyEvent.VK_X;}
		else if (checkIsSameKeycode(str, "VK_Y")) {result = KeyEvent.VK_Y;}
		else if (checkIsSameKeycode(str, "VK_Z")) {result = KeyEvent.VK_Z;}
		else if (checkIsSameKeycode(str, "VK_OPEN_BRACKET")) {result = KeyEvent.VK_OPEN_BRACKET;}
		else if (checkIsSameKeycode(str, "VK_BACK_SLASH")) {result = KeyEvent.VK_BACK_SLASH;}
		else if (checkIsSameKeycode(str, "VK_CLOSE_BRACKET")) {result = KeyEvent.VK_CLOSE_BRACKET;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD0")) {result = KeyEvent.VK_NUMPAD0;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD1")) {result = KeyEvent.VK_NUMPAD1;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD2")) {result = KeyEvent.VK_NUMPAD2;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD3")) {result = KeyEvent.VK_NUMPAD3;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD4")) {result = KeyEvent.VK_NUMPAD4;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD5")) {result = KeyEvent.VK_NUMPAD5;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD6")) {result = KeyEvent.VK_NUMPAD6;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD7")) {result = KeyEvent.VK_NUMPAD7;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD8")) {result = KeyEvent.VK_NUMPAD8;}
		else if (checkIsSameKeycode(str, "VK_NUMPAD9")) {result = KeyEvent.VK_NUMPAD9;}
		else if (checkIsSameKeycode(str, "VK_MULTIPLY")) {result = KeyEvent.VK_MULTIPLY;}
		else if (checkIsSameKeycode(str, "VK_ADD")) {result = KeyEvent.VK_ADD;}
		else if (checkIsSameKeycode(str, "VK_SEPARATER")) {result = KeyEvent.VK_SEPARATER;}
		else if (checkIsSameKeycode(str, "VK_SEPARATOR")) {result = KeyEvent.VK_SEPARATOR;}
		else if (checkIsSameKeycode(str, "VK_SUBTRACT")) {result = KeyEvent.VK_SUBTRACT;}
		else if (checkIsSameKeycode(str, "VK_DECIMAL")) {result = KeyEvent.VK_DECIMAL;}
		else if (checkIsSameKeycode(str, "VK_DIVIDE")) {result = KeyEvent.VK_DIVIDE;}
		else if (checkIsSameKeycode(str, "VK_DELETE")) {result = KeyEvent.VK_DELETE;}
		else if (checkIsSameKeycode(str, "VK_NUM_LOCK")) {result = KeyEvent.VK_NUM_LOCK;}
		else if (checkIsSameKeycode(str, "VK_SCROLL_LOCK")) {result = KeyEvent.VK_SCROLL_LOCK;}
		else if (checkIsSameKeycode(str, "VK_F1")) {result = KeyEvent.VK_F1;}
		else if (checkIsSameKeycode(str, "VK_F2")) {result = KeyEvent.VK_F2;}
		else if (checkIsSameKeycode(str, "VK_F3")) {result = KeyEvent.VK_F3;}
		else if (checkIsSameKeycode(str, "VK_F4")) {result = KeyEvent.VK_F4;}
		else if (checkIsSameKeycode(str, "VK_F5")) {result = KeyEvent.VK_F5;}
		else if (checkIsSameKeycode(str, "VK_F6")) {result = KeyEvent.VK_F6;}
		else if (checkIsSameKeycode(str, "VK_F7")) {result = KeyEvent.VK_F7;}
		else if (checkIsSameKeycode(str, "VK_F8")) {result = KeyEvent.VK_F8;}
		else if (checkIsSameKeycode(str, "VK_F9")) {result = KeyEvent.VK_F9;}
		else if (checkIsSameKeycode(str, "VK_F10")) {result = KeyEvent.VK_F10;}
		else if (checkIsSameKeycode(str, "VK_F11")) {result = KeyEvent.VK_F11;}
		else if (checkIsSameKeycode(str, "VK_F12")) {result = KeyEvent.VK_F12;}
		else if (checkIsSameKeycode(str, "VK_F13")) {result = KeyEvent.VK_F13;}
		else if (checkIsSameKeycode(str, "VK_F14")) {result = KeyEvent.VK_F14;}
		else if (checkIsSameKeycode(str, "VK_F15")) {result = KeyEvent.VK_F15;}
		else if (checkIsSameKeycode(str, "VK_F16")) {result = KeyEvent.VK_F16;}
		else if (checkIsSameKeycode(str, "VK_F17")) {result = KeyEvent.VK_F17;}
		else if (checkIsSameKeycode(str, "VK_F18")) {result = KeyEvent.VK_F18;}
		else if (checkIsSameKeycode(str, "VK_F19")) {result = KeyEvent.VK_F19;}
		else if (checkIsSameKeycode(str, "VK_F20")) {result = KeyEvent.VK_F20;}
		else if (checkIsSameKeycode(str, "VK_F21")) {result = KeyEvent.VK_F21;}
		else if (checkIsSameKeycode(str, "VK_F22")) {result = KeyEvent.VK_F22;}
		else if (checkIsSameKeycode(str, "VK_F23")) {result = KeyEvent.VK_F23;}
		else if (checkIsSameKeycode(str, "VK_F24")) {result = KeyEvent.VK_F24;}
		else if (checkIsSameKeycode(str, "VK_PRINTSCREEN")) {result = KeyEvent.VK_PRINTSCREEN;}
		else if (checkIsSameKeycode(str, "VK_INSERT")) {result = KeyEvent.VK_INSERT;}
		else if (checkIsSameKeycode(str, "VK_HELP")) {result = KeyEvent.VK_HELP;}
		else if (checkIsSameKeycode(str, "VK_META")) {result = KeyEvent.VK_META;}
		else if (checkIsSameKeycode(str, "VK_BACK_QUOTE")) {result = KeyEvent.VK_BACK_QUOTE;}
		else if (checkIsSameKeycode(str, "VK_QUOTE")) {result = KeyEvent.VK_QUOTE;}
		else if (checkIsSameKeycode(str, "VK_KP_UP")) {result = KeyEvent.VK_KP_UP;}
		else if (checkIsSameKeycode(str, "VK_KP_DOWN")) {result = KeyEvent.VK_KP_DOWN;}
		else if (checkIsSameKeycode(str, "VK_KP_LEFT")) {result = KeyEvent.VK_KP_LEFT;}
		else if (checkIsSameKeycode(str, "VK_KP_RIGHT")) {result = KeyEvent.VK_KP_RIGHT;}
		else if (checkIsSameKeycode(str, "VK_DEAD_GRAVE")) {result = KeyEvent.VK_DEAD_GRAVE;}
		else if (checkIsSameKeycode(str, "VK_DEAD_ACUTE")) {result = KeyEvent.VK_DEAD_ACUTE;}
		else if (checkIsSameKeycode(str, "VK_DEAD_CIRCUMFLEX")) {result = KeyEvent.VK_DEAD_CIRCUMFLEX;}
		else if (checkIsSameKeycode(str, "VK_DEAD_TILDE")) {result = KeyEvent.VK_DEAD_TILDE;}
		else if (checkIsSameKeycode(str, "VK_DEAD_MACRON")) {result = KeyEvent.VK_DEAD_MACRON;}
		else if (checkIsSameKeycode(str, "VK_DEAD_BREVE")) {result = KeyEvent.VK_DEAD_BREVE;}
		else if (checkIsSameKeycode(str, "VK_DEAD_ABOVEDOT")) {result = KeyEvent.VK_DEAD_ABOVEDOT;}
		else if (checkIsSameKeycode(str, "VK_DEAD_DIAERESIS")) {result = KeyEvent.VK_DEAD_DIAERESIS;}
		else if (checkIsSameKeycode(str, "VK_DEAD_ABOVERING")) {result = KeyEvent.VK_DEAD_ABOVERING;}
		else if (checkIsSameKeycode(str, "VK_DEAD_DOUBLEACUTE")) {result = KeyEvent.VK_DEAD_DOUBLEACUTE;}
		else if (checkIsSameKeycode(str, "VK_DEAD_CARON")) {result = KeyEvent.VK_DEAD_CARON;}
		else if (checkIsSameKeycode(str, "VK_DEAD_CEDILLA")) {result = KeyEvent.VK_DEAD_CEDILLA;}
		else if (checkIsSameKeycode(str, "VK_DEAD_OGONEK")) {result = KeyEvent.VK_DEAD_OGONEK;}
		else if (checkIsSameKeycode(str, "VK_DEAD_IOTA")) {result = KeyEvent.VK_DEAD_IOTA;}
		else if (checkIsSameKeycode(str, "VK_DEAD_VOICED_SOUND")) {result = KeyEvent.VK_DEAD_VOICED_SOUND;}
		else if (checkIsSameKeycode(str, "VK_DEAD_SEMIVOICED_SOUND")) {result = KeyEvent.VK_DEAD_SEMIVOICED_SOUND;}
		else if (checkIsSameKeycode(str, "VK_AMPERSAND")) {result = KeyEvent.VK_AMPERSAND;}
		else if (checkIsSameKeycode(str, "VK_ASTERISK")) {result = KeyEvent.VK_ASTERISK;}
		else if (checkIsSameKeycode(str, "VK_QUOTEDBL")) {result = KeyEvent.VK_QUOTEDBL;}
		else if (checkIsSameKeycode(str, "VK_LESS")) {result = KeyEvent.VK_LESS;}
		else if (checkIsSameKeycode(str, "VK_GREATER")) {result = KeyEvent.VK_GREATER;}
		else if (checkIsSameKeycode(str, "VK_BRACELEFT")) {result = KeyEvent.VK_BRACELEFT;}
		else if (checkIsSameKeycode(str, "VK_BRACERIGHT")) {result = KeyEvent.VK_BRACERIGHT;}
		else if (checkIsSameKeycode(str, "VK_AT")) {result = KeyEvent.VK_AT;}
		else if (checkIsSameKeycode(str, "VK_COLON")) {result = KeyEvent.VK_COLON;}
		else if (checkIsSameKeycode(str, "VK_CIRCUMFLEX")) {result = KeyEvent.VK_CIRCUMFLEX;}
		else if (checkIsSameKeycode(str, "VK_DOLLAR")) {result = KeyEvent.VK_DOLLAR;}
		else if (checkIsSameKeycode(str, "VK_EURO_SIGN")) {result = KeyEvent.VK_EURO_SIGN;}
		else if (checkIsSameKeycode(str, "VK_EXCLAMATION_MARK")) {result = KeyEvent.VK_EXCLAMATION_MARK;}
		else if (checkIsSameKeycode(str, "VK_INVERTED_EXCLAMATION_MARK")) {result = KeyEvent.VK_INVERTED_EXCLAMATION_MARK;}
		else if (checkIsSameKeycode(str, "VK_LEFT_PARENTHESIS")) {result = KeyEvent.VK_LEFT_PARENTHESIS;}
		else if (checkIsSameKeycode(str, "VK_NUMBER_SIGN")) {result = KeyEvent.VK_NUMBER_SIGN;}
		else if (checkIsSameKeycode(str, "VK_PLUS")) {result = KeyEvent.VK_PLUS;}
		else if (checkIsSameKeycode(str, "VK_RIGHT_PARENTHESIS")) {result = KeyEvent.VK_RIGHT_PARENTHESIS;}
		else if (checkIsSameKeycode(str, "VK_UNDERSCORE")) {result = KeyEvent.VK_UNDERSCORE;}
		else if (checkIsSameKeycode(str, "VK_WINDOWS")) {result = KeyEvent.VK_WINDOWS;}
		else if (checkIsSameKeycode(str, "VK_CONTEXT_MENU")) {result = KeyEvent.VK_CONTEXT_MENU;}
		else if (checkIsSameKeycode(str, "VK_FINAL")) {result = KeyEvent.VK_FINAL;}
		else if (checkIsSameKeycode(str, "VK_CONVERT")) {result = KeyEvent.VK_CONVERT;}
		else if (checkIsSameKeycode(str, "VK_NONCONVERT")) {result = KeyEvent.VK_NONCONVERT;}
		else if (checkIsSameKeycode(str, "VK_ACCEPT")) {result = KeyEvent.VK_ACCEPT;}
		else if (checkIsSameKeycode(str, "VK_MODECHANGE")) {result = KeyEvent.VK_MODECHANGE;}
		else if (checkIsSameKeycode(str, "VK_KANA")) {result = KeyEvent.VK_KANA;}
		else if (checkIsSameKeycode(str, "VK_KANJI")) {result = KeyEvent.VK_KANJI;}
		else if (checkIsSameKeycode(str, "VK_ALPHANUMERIC")) {result = KeyEvent.VK_ALPHANUMERIC;}
		else if (checkIsSameKeycode(str, "VK_KATAKANA")) {result = KeyEvent.VK_KATAKANA;}
		else if (checkIsSameKeycode(str, "VK_HIRAGANA")) {result = KeyEvent.VK_HIRAGANA;}
		else if (checkIsSameKeycode(str, "VK_FULL_WIDTH")) {result = KeyEvent.VK_FULL_WIDTH;}
		else if (checkIsSameKeycode(str, "VK_HALF_WIDTH")) {result = KeyEvent.VK_HALF_WIDTH;}
		else if (checkIsSameKeycode(str, "VK_ROMAN_CHARACTERS")) {result = KeyEvent.VK_ROMAN_CHARACTERS;}
		else if (checkIsSameKeycode(str, "VK_ALL_CANDIDATES")) {result = KeyEvent.VK_ALL_CANDIDATES;}
		else if (checkIsSameKeycode(str, "VK_PREVIOUS_CANDIDATE")) {result = KeyEvent.VK_PREVIOUS_CANDIDATE;}
		else if (checkIsSameKeycode(str, "VK_CODE_INPUT")) {result = KeyEvent.VK_CODE_INPUT;}
		else if (checkIsSameKeycode(str, "VK_JAPANESE_KATAKANA")) {result = KeyEvent.VK_JAPANESE_KATAKANA;}
		else if (checkIsSameKeycode(str, "VK_JAPANESE_HIRAGANA")) {result = KeyEvent.VK_JAPANESE_HIRAGANA;}
		else if (checkIsSameKeycode(str, "VK_JAPANESE_ROMAN")) {result = KeyEvent.VK_JAPANESE_ROMAN;}
		else if (checkIsSameKeycode(str, "VK_KANA_LOCK")) {result = KeyEvent.VK_KANA_LOCK;}
		else if (checkIsSameKeycode(str, "VK_INPUT_METHOD_ON_OFF")) {result = KeyEvent.VK_INPUT_METHOD_ON_OFF;}
		else if (checkIsSameKeycode(str, "VK_CUT")) {result = KeyEvent.VK_CUT;}
		else if (checkIsSameKeycode(str, "VK_COPY")) {result = KeyEvent.VK_COPY;}
		else if (checkIsSameKeycode(str, "VK_PASTE")) {result = KeyEvent.VK_PASTE;}
		else if (checkIsSameKeycode(str, "VK_UNDO")) {result = KeyEvent.VK_UNDO;}
		else if (checkIsSameKeycode(str, "VK_AGAIN")) {result = KeyEvent.VK_AGAIN;}
		else if (checkIsSameKeycode(str, "VK_FIND")) {result = KeyEvent.VK_FIND;}
		else if (checkIsSameKeycode(str, "VK_PROPS")) {result = KeyEvent.VK_PROPS;}
		else if (checkIsSameKeycode(str, "VK_STOP")) {result = KeyEvent.VK_STOP;}
		else if (checkIsSameKeycode(str, "VK_COMPOSE")) {result = KeyEvent.VK_COMPOSE;}
		else if (checkIsSameKeycode(str, "VK_ALT_GRAPH")) {result = KeyEvent.VK_ALT_GRAPH;}
		else if (checkIsSameKeycode(str, "VK_BEGIN")) {result = KeyEvent.VK_BEGIN;}
		else if (checkIsSameKeycode(str, "VK_UNDEFINED")) {result = KeyEvent.VK_UNDEFINED;}
		
		// 새로 추가
		else if (checkIsSameKeycode(str, "CTRL")) {result = KeyEvent.VK_CONTROL;}
		else if (checkIsSameKeycode(str, "ESC")) {result = KeyEvent.VK_ESCAPE;}
		else if (checkIsSameKeycode(str, "WINDOW")) {result = KeyEvent.VK_WINDOWS;}
		
		return result;
	}
	
	
	private static boolean checkIsSameKeycode(String str, String keyCode) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		if (keyCode == null || keyCode.length() == 0) {
			return false;
		}
		
		str = str.replace("VK_", "").replace("_", "").toLowerCase();
		keyCode = keyCode.replace("VK_", "").replace("_", "").toLowerCase();
		
		if (str.length() > 0 && keyCode.length() > 0 && str.equals(keyCode)) {
			return true;
		}
		
		return false;
	}
}