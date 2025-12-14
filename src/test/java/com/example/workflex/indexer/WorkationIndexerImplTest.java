package com.example.workflex.indexer;

import com.example.workflex.config.WorkationDataLoader;
import com.example.workflex.data.Workation;
import com.example.workflex.repository.WorkationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkationIndexerImplTest {

    @Mock
    private WorkationRepository mockRepository;

    @Mock
    private WorkationDataLoader mockLoader;

    private WorkationIndexerImpl workationIndexer;

    @BeforeEach
    void setUp() {
        workationIndexer = new WorkationIndexerImpl(mockRepository, mockLoader);
    }

    @Test
    void index_WhenRepositoryHasExistingData_ShouldSkipIndexing() {
        // arrange
        when(mockRepository.count()).thenReturn(5L);

        // act
        workationIndexer.index();

        // assert
        verify(mockRepository, times(1)).count();
        verify(mockLoader, never()).load();
        verify(mockRepository, never()).saveAll(any());
    }

    @Test
    void index_WhenRepositoryCountIsZero_ShouldLoadAndSaveWorkations() {
        // arrange
        when(mockRepository.count()).thenReturn(0L);

        Workation workation1 = Workation.builder()
                .id(1L)
                .workationId("W001")
                .employee("ABC")
                .build();

        Workation workation2 = Workation.builder()
                .id(2L)
                .workationId("W002")
                .employee("XYZ")
                .build();

        List<Workation> workations = Arrays.asList(workation1, workation2);

        when(mockLoader.load()).thenReturn(workations);
        when(mockRepository.saveAll(workations)).thenReturn(workations);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(workation1));

        // act
        workationIndexer.index();

        // assert
        verify(mockRepository, times(1)).count();
        verify(mockLoader, times(1)).load();
        verify(mockRepository, times(1)).saveAll(workations);
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    void index_WhenLoaderReturnsNull_ShouldSkipSaving() {
        // arrange
        when(mockRepository.count()).thenReturn(0L);
        when(mockLoader.load()).thenReturn(null);

        // act
        workationIndexer.index();

        // assert
        verify(mockRepository, times(1)).count();
        verify(mockLoader, times(1)).load();
        verify(mockRepository, never()).saveAll(any());
        verify(mockRepository, never()).findById(any());
    }
}