package com.arraigntech.service;

import java.util.List;

import org.springframework.data.domain.Page;

public interface IVSService<T, ID> {

	public Page<T> getPaginated(Integer page, Integer limit);

	public List<T> getAll();

	public T create(T entity);

	public T update(T entity);

	public boolean delete(ID id);

	public T getById(String id);
}
