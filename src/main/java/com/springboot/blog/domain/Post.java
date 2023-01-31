package com.springboot.blog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = "title")})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;
}
