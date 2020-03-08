package sk409.youtube.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;

@MappedSuperclass
public class Model {

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date createdAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date updatedAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @PostUpdate
    private void onPostUpdate() {
        this.updatedAt = new Date();
    }

}