/* CodingNomads (C)2024 */
package com.codingnomads.springdata.example.dml.derivedquerymethods.plantexample;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Entity
@Table(name = "plants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Plant {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    private SoilType favoriteSoilType;

    private String sunType;

    @Column(nullable = false)
    private boolean fruitBearing;

    private Integer numDaysTillRipeFruit;

    @PrePersist
    @PreUpdate
    private void checkForIllegalState() {
        if (!fruitBearing && !Objects.isNull(numDaysTillRipeFruit)) {
            throw new IllegalStateException("If the plant is not fruit bearing you cannot set numDaysTillRipeFruit");
        }
    }
}
