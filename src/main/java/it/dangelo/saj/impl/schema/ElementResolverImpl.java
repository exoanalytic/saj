package it.dangelo.saj.impl.schema;

import static it.dangelo.saj.impl.schema.SchemaQNameConsts.TYPE;
import static it.dangelo.saj.impl.schema.SchemaQNameConsts.FORMAT;
import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.schema.elements.AbstractElement;
import it.dangelo.saj.impl.schema.elements.AnyElementImpl;
import it.dangelo.saj.impl.schema.elements.ArrayElementImpl;
import it.dangelo.saj.impl.schema.elements.BooleanElementImpl;
import it.dangelo.saj.impl.schema.elements.DateElementImpl;
import it.dangelo.saj.impl.schema.elements.DateTimeElementImpl;
import it.dangelo.saj.impl.schema.elements.FloatElementImpl;
import it.dangelo.saj.impl.schema.elements.IntegerElementImpl;
import it.dangelo.saj.impl.schema.elements.NullElementImpl;
import it.dangelo.saj.impl.schema.elements.ObjectElementImpl;
import it.dangelo.saj.impl.schema.elements.StringElementImpl;
import it.dangelo.saj.impl.schema.elements.UnionElementImpl;
import it.dangelo.saj.validation.schemas.types.SchemaElement;

import java.util.HashMap;
import java.util.List;

public class ElementResolverImpl implements ElementResolver {

	@SuppressWarnings("unchecked")
	public SchemaElement resolve(Object type) throws SAJException {
		SchemaElement element = null;
		if (type instanceof String) {
			String string = (String) type;
			element = this.resolve(string);
		} else if (type instanceof HashMap) {
			HashMap<String, Object> map = (HashMap<String, Object>) type;
			element = this.resolve(map);
		}else if (type instanceof List) {
			List<Object> list = (List<Object>) type;
			element = this.resolve(list);
		}
		return element;
	}
	
	private SchemaElement resolve(List<Object> list) throws SAJException {
		int size = list.size();
		SchemaElement element = null;
		if(size == 0) {
			element = new ArrayElementImpl();
		} else if (size == 1) {
			SchemaElement item = this.resolve(list.get(0));
			element = new ArrayElementImpl((AbstractElement) item);
		} else {
			element = new UnionElementImpl();
//			for (Object object : list) {
//				((UnionElementImpl)element).addElement((AbstractElement ) this.resolve(object));
//			}
		}
		return element;
	}
	
	@SuppressWarnings("unchecked")
	private SchemaElement resolve(HashMap<String, Object> map) throws SAJException {
		Object type = map.get(TYPE);
		String format = (String)map.get(FORMAT);
		SchemaElement element = null;
		if (type instanceof String) {
			String string = (String) type;
			element = this.resolve(this.translateType(string, format));
			((AbstractElement) element).popolateByMap(map, this);
		} else if (type instanceof List) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) type;
			element = this.resolve(list);
			((AbstractElement) element).popolateByMap(map, this);
		} else if (type instanceof HashMap) {
			element = new ObjectElementImpl();
			((AbstractElement) element).popolateByMap(map, this);
		}
		return element;
	}
	private String translateType(String type, String format) {
		if(format == null) return type;
		String result = type;
		if(type.equals("string")) {
			if(
					format.equals("date")
					|| format.equals("date-time")
				)
				result = format;
		} else if(type.equals("number")) {
			if(format.equals("integer"))
				result = format;
		}
		return result;
	}
	
	private SchemaElement resolve(String type) throws SAJException {
		SchemaElement element = null;
		if(type.equals("string"))
			element = new StringElementImpl();
		else if(type.equals("any"))
			element = new AnyElementImpl();
		else if(type.equals("number"))
			element = new FloatElementImpl();
		else if(type.equals("boolean"))
			element = new BooleanElementImpl();
		else if(type.equals("object"))
			element = new ObjectElementImpl();
		else if(type.equals("array"))
			element = new ArrayElementImpl();
		else if(type.equals("null"))
			element = new NullElementImpl();
		else if(type.equals("date"))
			element = new DateElementImpl();
		else if(type.equals("date-time"))
			element = new DateTimeElementImpl();
		else if(type.equals("integer"))
			element = new IntegerElementImpl();
		return element;
		
	}
	
	public HashMap<String, Object> cloneMap(HashMap<String, Object> map, Object type) {
		HashMap<String, Object> map2 = new HashMap<String, Object>(map);
		map2.put(TYPE, type);
		return map2;
	}


}
