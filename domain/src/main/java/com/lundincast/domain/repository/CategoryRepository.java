package com.lundincast.domain.repository;

import com.lundincast.domain.Category;

import java.util.List;

import rx.Observable;

/**
 * Public interface that represents a Repository for getting {@link Category} related data.
 */
public interface CategoryRepository {

    /**
     * Get an {@link rx.Observable} which will emit a list of {@link Category}.
     */
    Observable<List<Category>> categories();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Category}.
     *
     * @param categoryId The category id used to retrieve user data.
     */
    Observable<Category> category(final int categoryId);
}
