package com.example.demo.entities

import groovy.transform.CompileStatic
import org.springframework.lang.NonNull

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotNull

@CompileStatic
@Entity
@Table(name = "generic_items")
@SequenceGenerator(name = "seq_generic_items", initialValue = 1, allocationSize = 100)
class GenericItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generic_items")
    Long id;

    @Column(name= "parent_id")
    Long parentId;

    @Column(name = "item_id")
    @NotNull
    String itemId;

    @Column(name = "resource_type_id")
    @NonNull
    Long resourceTypeId;

    @Column(name = "json_data")
    String jsonData;
}
