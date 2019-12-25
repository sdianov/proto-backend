package com.example.demo.entities

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@CompileStatic
@Entity
@Table(name = "generic_items")
@SequenceGenerator(name = "seq_generic_items", initialValue = 1, allocationSize = 100)
class GenericItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generic_items")
    Long id;

    @Column(name = "item_id")
    String itemId;

    @Column(name = "item_type")
    String itemType;

    @Column(name = "json_data")
    String jsonData;
}
