package com.store.backend.data.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseActionLog {

    @Field(type = FieldType.Keyword)
    private String performedBy;

    @Field(type = FieldType.Keyword)
    private String performedOn;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date creationTime;

}

