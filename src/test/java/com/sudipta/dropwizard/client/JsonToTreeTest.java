package com.sudipta.dropwizard.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Write assertions for JsonToTree")
@ExtendWith(MockitoExtension.class)
public class JsonToTreeTest {
    @InjectMocks
    public JsonToTree mockJsonToTree;

    public Set<String> mockPathSet;

    @BeforeEach
    public void init() {
        mockPathSet = new HashSet<>(4);
        mockPathSet.add("start -> fetch_submission_info -> review_request -> end");
        mockPathSet.add("fetch_submission_info -> select_test -> check_free_space -> check_imge -> skip_build_nxox -> prepare_for_test");
        mockPathSet.add("check_imge -> build_nxos -> prepare_for_test");
        mockPathSet.add("prepare_for_test -> select_ez_best_tier1_sanity -> submit_ez_best_tier1_sanity -> check_ez_best_tier1_sanity");
        mockPathSet.add("select_ez_best_tier1_sanity -> skip_ez_best_tier1_sanity -> check_ez_best_tier1_sanity");
        mockPathSet.add("check_ez_best_tier1_sanity -> cleanup -> end");
    }


    @DisplayName("GetJsonTreePaths test successful")
    @Test
    public void testGetJsonTreePaths() {
        Set<String> pathSet = mockJsonToTree.getJsonTreePaths();

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

    @DisplayName("testGetUniqueNodes test successful")
    @Test
    public void testGetNodeSet() {
        mockJsonToTree.getJsonTreePaths();
        Set<String> nodeSet = mockJsonToTree.getNodeSet();

        assertNotNull(nodeSet);
        assertEquals(15, nodeSet.size());

        List<String> list = new ArrayList<>(nodeSet);
        Collections.sort(list);
        for (String nodeName : list) {
            assertNotNull(nodeName);
        }
    }

}
