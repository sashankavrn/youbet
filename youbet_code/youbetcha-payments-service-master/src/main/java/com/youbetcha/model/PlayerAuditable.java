package com.youbetcha.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class PlayerAuditable<U> {
    protected U createdBy;
    protected Date createdDate;
    protected U lastModifiedBy;
    protected Date lastModifiedDate;
}
