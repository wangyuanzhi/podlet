package dnspod.podlet.util;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.ScriptableObject;

public class JsonParser {

	public static Map<String, Object> json2java(ScriptableObject json) {
		final Object[] ids = json.getIds();
		final Map<String, Object> map = new HashMap<String, Object>();

		for (Object object : ids) {
			final String key = object.toString();
			Object value = json.get(key, json);

			map.put(key, evalObj(value));
		}

		return map;
	}

	public static Object[] array2java(NativeArray json) {
		final Object[] ids = json.getIds();
		final Object[] result = new Object[ids.length];

		for (int i = 0; i < ids.length; i++) {
			Object value = json.get((Integer) ids[i], json);

			result[i] = evalObj(value);
		}

		return result;
	}

	public static Object evalObj(Object value) {
		Object result;

		if (NativeArray.class.isInstance(value)) {
			result = array2java((NativeArray) value);
		} else if (ScriptableObject.class.isInstance(value)) {
			result = json2java((ScriptableObject) value);
		} else {
			result = value;
		}

		return result;
	}

	public static Object evalJsStr(String str) throws ScriptException {
		ScriptEngineManager m = new ScriptEngineManager();
		ScriptEngine engine = m.getEngineByExtension("js");
		engine.eval(String.format("var jsObj=eval(%s)", str));

		Object jsObj = engine.get("jsObj");

		return evalObj(jsObj);
	}
}
