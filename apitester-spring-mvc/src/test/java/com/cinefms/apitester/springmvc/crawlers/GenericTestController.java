package com.cinefms.apitester.springmvc.crawlers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class GenericTestController<T> implements IGenericTestController<T> {
	
	/* (non-Javadoc)
	 * @see com.cinefms.apitester.springmvc.crawlers.IGenericTestController#get()
	 */
	@Override
	@RequestMapping(value="",method=RequestMethod.GET)
	public T get() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cinefms.apitester.springmvc.crawlers.IGenericTestController#put(java.lang.String, T)
	 */
	@Override
	@RequestMapping(value="",method=RequestMethod.PUT)
	public T put(@PathVariable String id, @RequestBody T t) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.cinefms.apitester.springmvc.crawlers.IGenericTestController#put(java.lang.String, T)
	 */
	@Override
	@RequestMapping(value="",method=RequestMethod.POST)
	public T put(@PathVariable int id, @RequestBody T t) {
		return null;
	}
	
}
