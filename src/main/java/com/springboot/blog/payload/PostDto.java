package com.springboot.blog.payload;

import lombok.*;

import java.util.Set;

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
    private Set<CommentDto> comments;
}
