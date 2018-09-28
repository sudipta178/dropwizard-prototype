package com.sudipta.dropwizard.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Write assertions for JsonToDAG")
@ExtendWith(MockitoExtension.class)
public class JsonToDAGTest {

    @InjectMocks
    private JsonToDAG mockJsonToDAG;

    private Set<String> mockPathSet = new HashSet<>();

    @BeforeEach
    public void doBeforeEachTest() {
        MockitoAnnotations.initMocks(this);

        mockPathSet.clear();
        mockPathSet.add("START -> FETCH_SUBMISSION_INFO -> REVIEW_REQUEST -> end");
        mockPathSet.add("START -> FETCH_SUBMISSION_INFO -> SELECT_TEST -> CHECK_SPACE -> CHECK_IMAGE");
        mockPathSet.add("CHECK_IMAGE -> SKIP_BUILD -> PREPARE_FOR_TEST -> select_ez_best_tier1_sanity");
        mockPathSet.add("CHECK_IMAGE -> BUILD -> PREPARE_FOR_TEST -> select_ez_best_tier1_sanity");
        mockPathSet.add("select_ez_best_tier1_sanity -> skip_ez_best_tier1_sanity -> check_ez_best_tier1_sanity -> cleanup -> end");
        mockPathSet.add("select_ez_best_tier1_sanity -> submit_ez_best_tier1_sanity -> check_ez_best_tier1_sanity -> cleanup -> end");
    }


    @DisplayName("GetJsonTreePaths test successful")
    @Test
    public void testGetJsonDAGPaths() {
        Set<String> pathSet = mockJsonToDAG.getJsonDAGPaths();

        assertNotNull(pathSet);
        assertFalse(pathSet.isEmpty());
        assertEquals(6, pathSet.size());

        assertEquals(6, mockPathSet.size());
        for (String path : pathSet) {
            assertNotNull(path);
            assertTrue(mockPathSet.remove(path));
        }
        assertEquals(0, mockPathSet.size());
    }

}
