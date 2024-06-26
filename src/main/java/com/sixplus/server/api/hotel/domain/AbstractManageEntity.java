package com.sixplus.server.api.hotel.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import com.sixplus.server.api.hotel.server.UserIdHolder;

import java.time.ZonedDateTime;

@Slf4j
@MappedSuperclass
@Getter
public abstract class AbstractManageEntity {

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    public AbstractManageEntity() {
        this.createdAt = ZonedDateTime.now();
        this.createdBy = UserIdHolder.getUserId();
    }
}
