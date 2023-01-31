package com.springboot.blog.payload;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private String content;
}
