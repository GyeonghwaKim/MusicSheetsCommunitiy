package com.example.youtubeSheet.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostImageDto
{
    private Long id;
    private String originalFileName;
    private String storedFileName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostImageDto that = (PostImageDto) o;
        return Objects.equals(id, that.id) && Objects.equals(originalFileName, that.originalFileName) && Objects.equals(storedFileName, that.storedFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, originalFileName, storedFileName);
    }

}
