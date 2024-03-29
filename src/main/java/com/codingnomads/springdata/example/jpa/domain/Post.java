/* CodingNomads (C)2024 */
package com.codingnomads.springdata.example.jpa.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String body;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "posts_tags",
            joinColumns = {@JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_posts_tags_post_id"))},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_posts_tags_tag_id"))
            })
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
