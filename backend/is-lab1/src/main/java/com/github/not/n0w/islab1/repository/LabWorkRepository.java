package com.github.not.n0w.islab1.repository;

import com.github.not.n0w.islab1.model.core.LabWork;
import com.github.not.n0w.islab1.model.MinimalPointGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabWorkRepository extends CrudRepository<LabWork, Integer> {

    @Query(value = "SELECT get_minimal_point_sum()", nativeQuery = true)
    Double getMinimalPointSum();

    @Query(value = """
        SELECT minimal_point AS "minimalPoints",
               counted AS "count"
        FROM count_lab_works_by_minimal_point()
        """, nativeQuery = true)
    List<MinimalPointGroup> countByMinimalPoint();

    @Query(value = "SELECT author_id FROM get_unique_authors()", nativeQuery = true)
    List<Integer> getUniqueAuthorIds();

    @Query(value = "SELECT 1 FROM add_lab_to_discipline(:labId, :disciplineId)", nativeQuery = true)
    Integer addLabToDiscipline(@Param("labId") Integer labId, @Param("disciplineId") Integer disciplineId);

    @Query(value = "SELECT 1 FROM add_top_lab_to_discipline(:disciplineId)", nativeQuery = true)
    Integer addTopLabsToDiscipline(@Param("disciplineId") Integer disciplineId);

    List<LabWork> findAllByAuthor_Id(Integer authorId);
    List<LabWork> findAllByDiscipline_Id(Integer disciplineId);
    List<LabWork> findAllByCoordinates_Id(Integer coordinatesId);
}