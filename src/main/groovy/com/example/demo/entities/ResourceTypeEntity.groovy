package com.example.demo.entities

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "resource_type")
@SequenceGenerator(name = "seq_resource_type", initialValue = 1, allocationSize = 100)
class ResourceTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_resource_type")
    Long id;

    @Column(name = "name")
    String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    ResourceTypeEntity parent;

    @OneToMany(mappedBy = "parent")
    List<ResourceTypeEntity> children;

    @OneToMany(mappedBy = "resourceType")
    List<ResourceFieldEntity> fieldList;


}
