package com.example.demo.entities

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "resource_field")
@SequenceGenerator(name = "seq_resource_field", initialValue = 1, allocationSize = 100)
class ResourceFieldEntity {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "data_type")
    String dataType;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    @JsonIgnore
    ResourceTypeEntity resourceType;
}
