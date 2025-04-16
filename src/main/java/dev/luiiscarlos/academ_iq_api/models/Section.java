package dev.luiiscarlos.academ_iq_api.models;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sections")
public class Section {

    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NonNull
    private String name;

    @Nullable
    @Builder.Default
    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration = LocalTime.of(0, 0, 0);

    @Nullable
    @ManyToMany
    @Builder.Default
    @JoinTable(
            name = "section_file_junction",
            joinColumns = { @JoinColumn(name = "section_id") },
            inverseJoinColumns = { @JoinColumn(name = "file_id") })
    private List<File> videos = new ArrayList<>();

}
