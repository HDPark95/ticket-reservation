package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.core.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "tb_concert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<ConcertSchedule> concertSchedules = new ArrayList<>();

    public void addConcertSchedule(LocalDate date) {
        this.concertSchedules.add(ConcertSchedule.builder().concert(this).date(date).build());
    }
}
