/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lrz.apigateway.integrationtests.vo.PersonVO;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lara
 */
public class PersonEmbeddedVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("personVOList")
    private List<PersonVO> persons;

    public PersonEmbeddedVO() {
    }

    public List<PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonVO> persons) {
        this.persons = persons;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.persons);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonEmbeddedVO other = (PersonEmbeddedVO) obj;
        return Objects.equals(this.persons, other.persons);
    }

}
