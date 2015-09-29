package com.cinefms.apitester.springmvc.crawlers;

public interface IGenericTestController<T> {

	T get();

	T put(String id, T t);

	T put(int id, T t);

}