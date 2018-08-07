package com.gcml.common.repository;

import com.gcml.common.repository.di.RepositoryComponent;
import com.gcml.common.repository.di.RepositoryModule;

public interface IRepositoryApp {

    RepositoryComponent repositoryComponent();

    RepositoryModule repositoryModule();
}
