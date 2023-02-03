package com.springboot.blog.payload;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String name;
    private String email;
    private String body;
}
