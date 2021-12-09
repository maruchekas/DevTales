package org.skillbox.devtales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag2post")
public class TagToPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

}
