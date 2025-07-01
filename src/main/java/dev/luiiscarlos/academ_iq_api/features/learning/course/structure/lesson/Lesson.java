package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.Section;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "video_id")
    private File video;

    private String name;

}
