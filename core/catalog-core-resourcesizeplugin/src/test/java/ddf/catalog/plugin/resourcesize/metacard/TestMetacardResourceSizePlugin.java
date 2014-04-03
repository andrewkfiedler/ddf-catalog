/**
 * Copyright (c) Codice Foundation
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 * 
 **/
package ddf.catalog.plugin.resourcesize.metacard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ddf.cache.Cache;
import ddf.cache.CacheManager;
import ddf.catalog.cache.CachedResource;
import ddf.catalog.data.Attribute;
import ddf.catalog.data.Metacard;
import ddf.catalog.data.Result;
import ddf.catalog.data.impl.MetacardImpl;
import ddf.catalog.data.impl.ResultImpl;
import ddf.catalog.operation.QueryResponse;

public class TestMetacardResourceSizePlugin {

    @Test
    public void testMetacardResourceSizePopulated() throws Exception {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        CachedResource cachedResource = mock(CachedResource.class);
        when(cachedResource.getSize()).thenReturn(999L);
        when(cache.get(anyObject())).thenReturn(cachedResource);
        
        MetacardImpl metacard = new MetacardImpl();
        metacard.setId("abc123");
        metacard.setSourceId("ddf-1");
        metacard.setResourceSize("N/A");
        
        Result result = new ResultImpl(metacard);
        List<Result> results = new ArrayList<Result>();
        results.add(result);

        QueryResponse input = mock(QueryResponse.class);
        when(input.getResults()).thenReturn(results);
        
        MetacardResourceSizePlugin plugin = new MetacardResourceSizePlugin(cacheManager);
        QueryResponse queryResponse = plugin.process(input);
        assertThat(queryResponse.getResults().size(), is(1));
        Metacard resultMetacard = queryResponse.getResults().get(0).getMetacard();
        assertThat(metacard, is(notNullValue()));
        // Since using Metacard vs. MetacardImpl have to get resource-size as an
        // Attribute vs. Long
        Attribute resourceSizeAttr = resultMetacard.getAttribute(Metacard.RESOURCE_SIZE);
        assertThat((Long)resourceSizeAttr.getValue(), is(999L));
    }
    
    @Test
    public void testNullMetacard() throws Exception {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        
        Result result = mock(Result.class);
        when(result.getMetacard()).thenReturn(null);
        List<Result> results = new ArrayList<Result>();
        results.add(result);

        QueryResponse input = mock(QueryResponse.class);
        when(input.getResults()).thenReturn(results);
        
        MetacardResourceSizePlugin plugin = new MetacardResourceSizePlugin(cacheManager);
        QueryResponse queryResponse = plugin.process(input);
        assertThat(queryResponse, equalTo(input));
    }

    @Test
    public void testWhenNoCachedResourceFound() throws Exception {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        when(cache.get(anyObject())).thenReturn(null);
        
        MetacardImpl metacard = new MetacardImpl();
        metacard.setId("abc123");
        metacard.setSourceId("ddf-1");
        metacard.setResourceSize("N/A");
        
        Result result = new ResultImpl(metacard);
        List<Result> results = new ArrayList<Result>();
        results.add(result);

        QueryResponse input = mock(QueryResponse.class);
        when(input.getResults()).thenReturn(results);
        
        MetacardResourceSizePlugin plugin = new MetacardResourceSizePlugin(cacheManager);
        QueryResponse queryResponse = plugin.process(input);
        assertThat(queryResponse.getResults().size(), is(1));
        Metacard resultMetacard = queryResponse.getResults().get(0).getMetacard();
        assertThat(metacard, is(notNullValue()));
        // Since using Metacard vs. MetacardImpl have to get resource-size as an
        // Attribute vs. Long
        Attribute resourceSizeAttr = resultMetacard.getAttribute(Metacard.RESOURCE_SIZE);
        assertThat((String)resourceSizeAttr.getValue(), equalTo("N/A"));
    }

    @Test
    public void testWhenCachedResourceSizeIsZero() throws Exception {
        CacheManager cacheManager = mock(CacheManager.class);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache(anyString())).thenReturn(cache);
        CachedResource cachedResource = mock(CachedResource.class);
        when(cachedResource.getSize()).thenReturn(0L);
        when(cache.get(anyObject())).thenReturn(cachedResource);
        
        MetacardImpl metacard = new MetacardImpl();
        metacard.setId("abc123");
        metacard.setSourceId("ddf-1");
        metacard.setResourceSize("N/A");
        
        Result result = new ResultImpl(metacard);
        List<Result> results = new ArrayList<Result>();
        results.add(result);

        QueryResponse input = mock(QueryResponse.class);
        when(input.getResults()).thenReturn(results);
        
        MetacardResourceSizePlugin plugin = new MetacardResourceSizePlugin(cacheManager);
        QueryResponse queryResponse = plugin.process(input);
        assertThat(queryResponse.getResults().size(), is(1));
        Metacard resultMetacard = queryResponse.getResults().get(0).getMetacard();
        assertThat(metacard, is(notNullValue()));
        // Since using Metacard vs. MetacardImpl have to get resource-size as an
        // Attribute vs. Long
        Attribute resourceSizeAttr = resultMetacard.getAttribute(Metacard.RESOURCE_SIZE);
        assertThat((String)resourceSizeAttr.getValue(), equalTo("N/A"));
    }

}