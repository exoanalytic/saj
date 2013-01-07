package it.dangelo.saj.impl;

import it.dangelo.saj.SAJException;
import it.dangelo.saj.impl.parser.Component;
import it.dangelo.saj.impl.parser.EventPipeline;
import it.dangelo.saj.impl.parser.Handlers;
import it.dangelo.saj.parser.ContentHandler;
import it.dangelo.saj.parser.ErrorHandler;
import it.dangelo.saj.parser.ResourceResolver;

import java.net.URI;
import java.util.HashMap;

public class ParserConfiguration {
	
	public  static final String ERROR_HANDLER_PROPERTY = ErrorHandler.class.getName();
	public  static final String CONTENT_HANDLER_PROPERTY = ContentHandler.class.getName();
	public  static final String RESOURCE_RESOLVER_PROPERTY = ResourceResolver.class.getName();
	private static final String[] COMPONENTS = {
			"it.dangelo.saj.impl.ContentHandlerCaller",
			"it.dangelo.saj.impl.schema.ValidatorComponent"
		};
	private static final ContentHandler DEFAULT_CONTENT_HANDLER = new DefaultContentHandler();
	private static final ErrorHandler DEFAULT_ERROR_HANDLER = new DefaultErrorHandler();
	private static final ResourceResolver DEFAULT_RESOURCE_RESOLVER = new DefaultResourceResolver();

	private static final HashMap<String, Class<Component>> CACHE_CLASS_COMPONENT = new HashMap<String, Class<Component>>();
	
	public EventPipeline buildEventPipeline(HashMap<String, Object> properties, URI uri) throws SAJException{
		EventPipeline pipeline = new EventPipeline();
		Handlers handlers = this.resolveHandlers(properties);
		pipeline.setHandlers(handlers);
		for (String string : COMPONENTS) {
			Component component = this.resolveComponent(string);
			if(!component.enabled(properties)) continue;
			component.init(handlers, uri, properties);
			pipeline.addComponent(component);
		}
		return pipeline;
	}
	
	@SuppressWarnings("unchecked")
	private Component resolveComponent(String name) throws SAJException{
		try {
			Class<Component> c = CACHE_CLASS_COMPONENT.get(name);
			if(c == null) {
				c = (Class<Component>) Class.forName(name);
				CACHE_CLASS_COMPONENT.put(name, c);
			}
			return c.newInstance();
		} catch (Exception e) {
			throw new SAJException("Error to create the component", e);
		}
			
	}
	
	public Handlers resolveHandlers(HashMap<String, Object> properties) {
		ContentHandler contentHandler = (ContentHandler) properties.get(CONTENT_HANDLER_PROPERTY);
		ErrorHandler errorHandler = (ErrorHandler) properties.get(ERROR_HANDLER_PROPERTY);
		ResourceResolver resolver = (ResourceResolver) properties.get(RESOURCE_RESOLVER_PROPERTY);
		if(contentHandler == null)
			contentHandler = DEFAULT_CONTENT_HANDLER;
		if(errorHandler == null)
			errorHandler = DEFAULT_ERROR_HANDLER;
		if(resolver == null)
			resolver = DEFAULT_RESOURCE_RESOLVER;
		return new Handlers(contentHandler, errorHandler, resolver);
	}
	
}
