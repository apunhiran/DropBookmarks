package com.udemy.dropbookmarks;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DropBookmarksConfiguration extends Configuration {


    // TODO: implement service configuration
    @NotEmpty
    private String password;
    @JsonProperty
    public String getPassword() {
        return password;
    }

    @NotNull
    @Valid
    private DataSourceFactory dataSourceFactory =
            new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

}
